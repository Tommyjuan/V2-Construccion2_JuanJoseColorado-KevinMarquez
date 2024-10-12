package app.service.x;

import app.Daoo.GuestDaoImplemetation;
import app.Daoo.PartnerDaoImplemetation;
import app.Daoo.PersonDaoImplementation;
import app.Daoo.UserDaoImplementation;
import app.controller.validator.InvoiceValidator;
import app.controllers.Utils;
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
        this.createUser(partnerDto.getUserId());
        UserDto userDto = userDao.findByUserName(partnerDto.getUserId());
        partnerDto.setUserId(userDto);
        try {
            this.partnerDao.createPartner(partnerDto);
        } catch (SQLException e) {
            this.userDao.deleteUser(userDto);
            throw new Exception("error al crear el partner");
        }
    }

    @Override
    public void createGuest(GuestDto guestDto) throws Exception {
        this.createUser(guestDto.getUserId());
        UserDto userDto = userDao.findByUserName(guestDto.getUserId());
        guestDto.setUserId(userDto);
        PartnerDto partnerDto = partnerDao.existByPartner(user);
        guestDto.setPartnerId(partnerDto);

        try {
            this.guestDao.createGuest(guestDto);
        } catch (SQLException e) {
            this.userDao.deleteUser(userDto);

            throw new Exception("error al crear el invitador", e);
        }
    }

    @Override
    public void deletePartner() throws Exception {

        UserDto users = ServiceClub.user;
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
        }
    }

    @Override
    public void changeRol(PartnerDto partnerDto) throws Exception {

        UserDto users = ServiceClub.user;
        GuestDto guestDto = this.guestDao.existByGuest(users);
        this.guestDao.deleteGuest(guestDto);
        UserDto userDto = userDao.findByUserName(users);
        partnerDto.setUserId(userDto);
        userDto.setRole("partner");
        this.userDao.updateUserRole(userDto);
        try {
            this.partnerDao.createPartner(partnerDto);
            System.out.println("Se ha convertido el Invitado en  exitosamente.");

        } catch (SQLException e) {
            System.out.println("El usuario no existe en la base de datos.");
        }
    }

    @Override
    public void showGuestsForPartner(PartnerDto partnerDto) throws Exception {
        UserDto users = ServiceClub.user;
        List<GuestDto> guests = guestDao.statusGuest(partnerDto);

        System.out.println("Invitados registrados para el socio con el user name " + users.getUserName() + ":");
        for (GuestDto guest : guests) {

            System.out.println("ID: " + guest.getId() + "\n UserID: " + guest.getUserId().getId() + "\n Status: " + guest.getStatus());

        }
    }

    @Override
    public GuestDto getGuestById(long guestId) throws Exception {
        return guestDao.getGuestById(guestId);
    }

    @Override
    public void updateGuestStatus(GuestDto guestDto) throws Exception {

        guestDao.changeStatus(guestDto);
    }

    @Override
    public void updateMoney() throws Exception {
        UserDto users = ServiceClub.user;
        PartnerDto partnerDto = partnerDao.existByPartner(users);
        System.out.println("su tipo es :" + partnerDto.getType());
        System.out.println("El dinero con el cuenta ahora mismo es:" + partnerDto.getMoney());
        System.out.println("Cuanto desea ingresar : ");
        double getMoney = Double.parseDouble(Utils.getReader().nextLine());
        addFound = partnerDto.getMoney() + getMoney;
        if ("regular".equals(partnerDto.getType()) && addFound >= 1000000) {

            System.out.println("No puedes tener mas de 1000000");
            addFound = addFound - getMoney;

        }

        if ("vip".equals(partnerDto.getType()) && addFound >= 5000000) {
            System.out.println("No puedes tener mas de 5000000");
            addFound = addFound - getMoney;
        }
        partnerDto.setMoney(addFound);
        this.partnerDao.getMoneyByPartner(addFound);
        this.partnerDao.updateMoney(partnerDto);

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

        }

    }

    @Override
    public int countActiveGuestsByPartner(long partnerId) throws Exception {
        return guestDao.countGuestsByPartnerId(partnerId);
    }

    @Override
    public void createInvoice() throws Exception {
        UserDto userDto = ServiceClub.user;
        System.out.println("ingrese la cantidad de items");
        int items = invoiceValidator.validItem(Utils.getReader().nextLine());

        InvoiceDto invoiceDto = new InvoiceDto();

        PartnerDto partnerDto = partnerDao.existByPartner(userDto);
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(partnerDto);
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("Sin pagar");
        List<InvoiceDetailDto> invoices = new ArrayList<InvoiceDetailDto>();
        double total = 0;

        for (int i = 0; i < items; i++) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();

            invoiceDetailDto.setItem(i + 1);
            invoiceDetailDto.setDescription("Descripcion" + (i + 1));
            System.out.println("Ingrese el precio del ítem " + (i + 1));
            invoiceDetailDto.setAmount(invoiceValidator.validAmount(Utils.getReader().nextLine()));
            total += invoiceDetailDto.getAmount();
            invoices.add(invoiceDetailDto);

        }

        invoiceDto.setAmount(total);

        Invoice invoice = Helper.parse(invoiceDto);
        invoiceDao.createInvoice(invoice);
        for (InvoiceDetailDto detail : invoices) {
            detail.setInvoiceId(invoiceDto);
            InvoiceDetail invoiceDetail = Helper.parse(detail);
            invoiceDetail.setInvoiceId(invoice);
            invoiceDetailDao.createInvoiceDetail(invoiceDetail);
        }
    }

    @Override
    public void payInvoice() throws Exception {

        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        System.out.println("Ingrese el ID de la factura que desea pagar:");
        long invoiceId = Long.parseLong(Utils.getReader().nextLine());
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
    public void showInvoiceForPartner() throws Exception {
        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        UserDto users = ServiceClub.user;
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(partnerDto.getId());
        List<InvoiceDto> invoices = invoiceDao.statusInvoice(invoiceDto);

        System.out.println("Facturas del socio " + users.getUserName() + ":");
        if (invoices.isEmpty()) {
            System.out.println("No hay facturas disponibles para este socio.");
        }
        for (InvoiceDto invoice : invoices) {
            System.out.println("ID: " + invoice.getId() + "\n Status: " + invoice.getStatus() + "\n Valor: " + invoice.getAmount());
        }

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
    public void guestInvoice() throws Exception {

        UserDto userDto = ServiceClub.user;
        System.out.println("ingrese la cantidad de items");
        int items = invoiceValidator.validItem(Utils.getReader().nextLine());
        GuestDto guestDto = guestDao.existByGuest(userDto);
        InvoiceDto invoiceDto = new InvoiceDto();
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(guestDto.getPartnerId());
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("Sin pagar");
        List<InvoiceDetailDto> invoices = new ArrayList<InvoiceDetailDto>();
        double total = 0;

        for (int i = 0; i < items; i++) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();

            invoiceDetailDto.setItem(i + 1);
            invoiceDetailDto.setDescription("Descripcion" + (i + 1));
            System.out.println("Ingrese el precio del ítem " + (i + 1));
            invoiceDetailDto.setAmount(invoiceValidator.validAmount(Utils.getReader().nextLine()));
            total += invoiceDetailDto.getAmount();
            invoices.add(invoiceDetailDto);

        }

        invoiceDto.setAmount(total);
        Invoice invoice = Helper.parse(invoiceDto);
        invoiceDao.createInvoice(invoice);
        for (InvoiceDetailDto detail : invoices) {
            detail.setInvoiceId(invoiceDto);
            InvoiceDetail invoiceDetail = Helper.parse(detail);
            invoiceDetail.setInvoiceId(invoice);
            invoiceDetailDao.createInvoiceDetail(invoiceDetail);
        }
    }
}
