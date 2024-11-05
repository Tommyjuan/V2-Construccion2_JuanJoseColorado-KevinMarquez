package app.service.x;

import app.Daoo.GuestDaoImplemetation;
import app.Daoo.PartnerDaoImplemetation;
import app.Daoo.PersonDaoImplementation;
import app.Daoo.UserDaoImplementation;
import app.controller.validator.InvoiceValidator;
import app.controllers.Utils;
import app.controllers.requests.InvoiceItem;
import app.controllers.requests.InvoiceRequest;
import app.controllers.requests.ParnerInvoice;
import app.controllers.requests.PayInvoice;
import app.dao.interfaces.GuestDao;
import app.dao.interfaces.InvoiceDao;
import app.dao.interfaces.InvoiceDetailDao;
import app.dao.interfaces.PartnerDao;
import app.dao.interfaces.PersonDao;
import app.dao.interfaces.UserDao;
import app.dto.GuestDto;
import app.dto.InvoiceDetailDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Invoice;
import app.model.InvoiceDetail;
import app.model.Guest;
import app.model.Partner;
import app.model.Person;
import app.service.interfac.AdminService;
import app.service.interfac.LoginService;
import app.service.interfac.PartnerService;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class ServiceClub implements AdminService, LoginService, PartnerService {

    @Autowired
    private InvoiceValidator invoiceValidator;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private InvoiceDetailDao invoiceDetailDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private GuestDao guestDao;
    @Autowired
    public static UserDto user;
    @Autowired
    public static PersonDto person;

    private double addFound;
    private double pay;

    @Override
    public void login(UserDto userDto) throws Exception {
        UserDto validateDto = userDao.findByUserName(userDto);
        if (validateDto == null) {
            throw new Exception("no existe usuario registrado");
        }
        if (!userDto.getPassword().equals(validateDto.getPassword())) {
            throw new Exception("usuario o contraseña incorrecto");
        }
        userDto.setRole(validateDto.getRole());
        user = validateDto;

    }

    @Override
    public void logout() {
        user = null;
        System.out.println("se ha cerrado sesion");
    }

    private void createUser(UserDto userDto) throws Exception {
        this.createPerson(userDto.getPersonId());
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        userDto.setPersonId(personDto);
        if (this.userDao.existsByUserName(userDto)) {
            this.personDao.deletePerson(userDto.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userDao.createUser(userDto);
        } catch (SQLException e) {
            this.personDao.deletePerson(userDto.getPersonId());
            throw new Exception("error al crear el usuario");
        }
    }

    private void createPerson(PersonDto personDto) throws Exception {

        if (this.personDao.existByDocument(personDto)) {
            throw new Exception("Ya existe una persona con este documento");
        }

        this.personDao.createPerson(personDto);
    }

    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception {
        checkVipLimit(partnerDto);
        createUser(partnerDto.getUserId());

        UserDto userDto = userDao.findByUserName(partnerDto.getUserId());
        partnerDto.setUserId(userDto);

        try {
            partnerDao.createPartner(partnerDto);
        } catch (SQLException e) {
            userDao.deleteUser(userDto);
            throw new Exception("Error al crear el partner", e);
        }
    }

    @Override
    public void createGuest(GuestDto guestDto) throws Exception {
        this.createUser(guestDto.getUserId());

        UserDto userDto = userDao.findByUserName(guestDto.getUserId());
        guestDto.setUserId(userDto);
        PartnerDto partnerDto = partnerDao.findById(guestDto.getPartnerId().getId());
        guestDto.setPartnerId(partnerDto);

        try {
            this.guestDao.createGuest(guestDto);
        } catch (SQLException e) {
            this.userDao.deleteUser(userDto);
            throw new Exception("Error al crear el invitado", e);
        }
    }

    @Override
    public void deletePartner() throws Exception {

        /* UserDto users = ServiceClub.user;
        try {

            PartnerDto partnerDto = this.partnerDao.existByPartner(users);
            UserDto userDto = this.userDao.findByUserName(users);
            this.partnerDao.deletePartner(partnerDto);
            this.userDao.deleteUser(userDto);

            this.personDao.deletePerson(userDto.getPersonId());
            System.out.println("Su cuenta ha sido eliminada exitosamente.");
            this.logout();
        } catch (SQLException e) {
            System.out.println("El usuario no existe en la base de datos.");
        }*/
    }

    @Override
    public void changeRol(PartnerDto partnerDto) throws Exception {

        UserDto userDto = userDao.findByUserName(partnerDto.getUserId());
         if (userDto == null) {
        throw new Exception("Usuario no encontrado.");
    }
        GuestDto guestDto = guestDao.existByGuest(userDto);
    if (guestDto != null) {
        // Si el usuario es un invitado, eliminamos su registro
        guestDao.deleteGuest(guestDto);
        System.out.println("Se eliminó el invitado con éxito.");
    }


        userDto.setRole("partner");
        this.userDao.updateUserRole(userDto);

        try {
            this.partnerDao.createPartner(partnerDto);
            System.out.println("Se ha convertido el Invitado en socio exitosamente.");
        } catch (SQLException e) {
            throw new Exception("Error al crear el socio: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GuestDto> showGuestsForPartner(PartnerDto partnerDto) throws Exception {

        List<GuestDto> guests = guestDao.statusGuest(partnerDto);

        for (GuestDto guest : guests) {
            System.out.println("ID: " + guest.getId() + "\n UserID: " + guest.getUserId().getId() + "\n Status: " + guest.getStatus());
        }

        return guests;
    }

    @Override
    public GuestDto getGuestById(long guestId) throws Exception {
        return guestDao.getGuestById(guestId);
    }

    @Override
    public void updateGuestStatus(GuestDto guestDto) throws Exception {
        System.out.println("error 2");
        guestDao.changeStatus(guestDto);
        System.out.println("error 3");
    }

    @Override
    public void updateMoney(long partnerId, double amount) throws Exception {

        PartnerDto existingPartner = partnerDao.findById(partnerId);

        if (existingPartner == null) {
            throw new Exception("Socio no encontrado.");
        }

        double totalMoney = existingPartner.getMoney() + amount;

        if ("regular".equals(existingPartner.getType()) && totalMoney > 1000000) {
            throw new Exception("No puedes tener más de 1,000,000.");
        }

        if ("vip".equals(existingPartner.getType()) && totalMoney > 5000000) {
            throw new Exception("No puedes tener más de 5,000,000.");
        }

        existingPartner.setMoney(totalMoney);
        this.partnerDao.updateMoney(existingPartner); // Asegúrate de tener este método en tu repositorio
        System.out.println("Fondos actualizados exitosamente. Nuevo saldo: " + existingPartner.getMoney());
    }

    @Override
    public void checkVipLimit(PartnerDto partnerDto) throws Exception {
        if ("vip".equals(partnerDto.getType())) {
            int vipCount = this.partnerDao.countVipPartners();
            final int vip = 5;
            if (vipCount >= vip) {
                throw new Exception("El número máximo de socios VIP ya ha sido alcanzado.");
            }
        }
    }

    @Override
    public void checkGuestLimit(PartnerDto partnerDto) throws Exception {
        if ("regular".equals(partnerDto.getType())) {
            int guestCount = this.guestDao.countGuestsByPartnerId(partnerDto.getId());
            final int guest = 3;
            if (guestCount >= guest) {
                throw new Exception("El numero maximo de invitados a sido alcanzado.");
            }
        }
    }

    @Override
    public void vipPromocion() throws Exception {
        /*
        PartnerDto partnerDto = this.partnerDao.existByPartner(user);
        checkVipLimit(partnerDto);
        if ("regular".equals(partnerDto.getType())) {

            int vipCount = partnerDao.countVipPartners();
            final int vip = 5;
            partnerDto.setType("vip");
            if (vipCount >= vip) {
                throw new Exception("El número máximo de socios VIP ya ha sido alcanzado.");
            }
            partnerDto.setType("vip");
            this.partnerDao.updatePartnerType(partnerDto);
            System.out.println("Tu solicitud de promoción a VIP ha sido procesada.");
        } else {
            System.out.println("Ya eres un socio VIP o no eres un socio regular.");

        }*/

    }

    @Override
    public int countActiveGuestsByPartner(long partnerId) throws Exception {
        return guestDao.countGuestsByPartnerId(partnerId);
    }

    @Override
    public void createInvoice(InvoiceRequest request) throws Exception {
        long partnerId = request.getPartnerId();
        PartnerDto partnerDto = partnerDao.findById(partnerId);
        long personId = request.getPersonId();
        PersonDto personDto = personDao.findById(personId);
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(partnerDto);
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("Sin pagar");
        double total = 0;
        List<InvoiceDetailDto> invoiceDetails = new ArrayList<>();
        for (InvoiceItem item : request.getItems()) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
            invoiceDetailDto.setDescription(item.getDescription());
            invoiceDetailDto.setAmount(item.getAmount());
            total += item.getAmount();
            invoiceDetails.add(invoiceDetailDto);
        }
        invoiceDto.setAmount(total);
        Invoice invoice = Helper.parse(invoiceDto);
        invoiceDao.createInvoice(invoice);
        for (InvoiceDetailDto detail : invoiceDetails) {
            detail.setInvoiceId(invoiceDto);
            InvoiceDetail invoiceDetail = Helper.parse(detail);
            invoiceDetail.setInvoiceId(invoice);
            invoiceDetailDao.createInvoiceDetail(invoiceDetail);
        }
    }

    @Override
    public void payInvoice(PayInvoice request) throws Exception {

        long partnerId = request.getPartnerId();
        PartnerDto partnerDto = partnerDao.findById(partnerId);
        long invoiceId = request.getInvoiceId();
        InvoiceDto invoiceDto = invoiceDao.existsByIDInvoice(invoiceId);

        if (partnerDto.getMoney() < invoiceDto.getAmount()) {
            System.out.println("Fondos insuficientes para pagar la factura.");
        } else {
            pay = partnerDto.getMoney() - invoiceDto.getAmount();
            invoiceDto.setStatus("Pagada");
            invoiceDao.changeStatus(invoiceDto);
            System.out.println("Factura pagada exitosamente el pago total fue de:" + invoiceDto.getAmount() + ". Nuevo saldo: " + pay);
            partnerDto.setMoney(pay);
            partnerDao.updateMoney(partnerDto);
        }
        if ("Pagada".equals(invoiceDto.getStatus())) {
            throw new Exception("La factura ya ha sido pagada.");
        }

    }

    @Override
    public List<InvoiceDto> showInvoiceForPartner(ParnerInvoice request) throws Exception {
        long partnerId = request.getPartnerId();
        PartnerDto partnerDto = partnerDao.findById(partnerId);
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(partnerDto.getId());
        List<InvoiceDto> invoices = invoiceDao.statusInvoice(invoiceDto);

        if (invoices.isEmpty()) {
            System.out.println("No hay facturas disponibles para este socio.");
        }
        for (InvoiceDto invoice : invoices) {
            System.out.println("ID: " + invoice.getId() + "\n Status: " + invoice.getStatus() + "\n Valor: " + invoice.getAmount());
        }
        return invoices;
    }

    @Override
    public void showInvoiceForAdmin() throws Exception {
        List<InvoiceDto> invoices = invoiceDao.findAllInvoices();

        System.out.println("Facturas registradas en el sistema:");
        if (invoices.isEmpty()) {
            System.out.println("No hay facturas disponibles en el sistema.");
        }
        for (InvoiceDto invoice : invoices) {
            System.out.println("ID: " + invoice.getId() + "\n Socio: " + invoice.getPartnerId().getId() + "\n Status: " + invoice.getStatus() + "\n Valor: " + invoice.getAmount());
        }
    }

    @Override
    public void guestInvoice(InvoiceRequest request) throws Exception {
        long partnerId = request.getPartnerId();
        PartnerDto partnerDto = partnerDao.findById(partnerId);
        long personId = request.getPersonId();
        PersonDto personDto = personDao.findById(personId);
        InvoiceDto invoiceDto = new InvoiceDto();
        long guestId = request.getGuestId();
        GuestDto guestDto = guestDao.findById(guestId);
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(guestDto.getPartnerId());
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("Sin pagar");
         double total = 0;
        List<InvoiceDetailDto> invoiceDetails = new ArrayList<>();
        for (InvoiceItem item : request.getItems()) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
            invoiceDetailDto.setDescription(item.getDescription());
            invoiceDetailDto.setAmount(item.getAmount());
            total += item.getAmount();
            invoiceDetails.add(invoiceDetailDto);
        }
        invoiceDto.setAmount(total);
        Invoice invoice = Helper.parse(invoiceDto);
        invoiceDao.createInvoice(invoice);
        for (InvoiceDetailDto detail : invoiceDetails) {
            detail.setInvoiceId(invoiceDto);
            InvoiceDetail invoiceDetail = Helper.parse(detail);
            invoiceDetail.setInvoiceId(invoice);
            invoiceDetailDao.createInvoiceDetail(invoiceDetail);
        }
    }
}
