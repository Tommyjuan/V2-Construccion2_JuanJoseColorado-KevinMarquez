package app.service;

import app.controller_validator.InvoiceValidator;
import app.controller.Utils;
import app.dao_interface.GuestDao;
import app.dao_interface.InvoiceDao;
import app.dao_interface.InvoiceDetailDao;
import app.dao_interface.PartnerDao;
import app.dao_interface.PersonDao;
import app.dao_interface.UserDao;
import app.dto.GuestDto;
import app.dto.InvoiceDetailDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Invoice;
import app.model.InvoiceDetail;
import app.service_interface.AdminService;
import app.service_interface.LoginService;
import app.service_interface.PartnerService;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
            throw new Exception("alparecer ese documento ya hace parte del club");
        }

        this.personDao.createPerson(personDto);
    }

    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception {
        vipLimit(partnerDto);
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
    public void watchGuests(PartnerDto partnerDto) throws Exception {
        UserDto users = ServiceClub.user;
        List<GuestDto> guests = guestDao.statusGuest(partnerDto);

        System.out.println("Id de invitados registrados para el socio actual - " + users.getUserName() + "- son :");

        for (GuestDto guest : guests) {
            System.out.println("ID: " + guest.getId() + "\n UserID: " + guest.getUserId().getId() + "\n Status: " + guest.getStatus());

        }
    }

    @Override
    public GuestDto getGuestById(long guestId) throws Exception {
        return guestDao.getGuestById(guestId);
    }

    @Override
    public void updateStatus(GuestDto guestDto) throws Exception {

        guestDao.changeStatus(guestDto);
    }

    @Override
    public void updateMoney() throws Exception {
        UserDto users = ServiceClub.user;
        PartnerDto partnerDto = partnerDao.existByPartner(users);
        System.out.println("su tipo es :" + partnerDto.getType());
        System.out.println("El dinero con el cuenta actual:" + partnerDto.getMoney());
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
    public void vipLimit(PartnerDto partnerDto) throws Exception {
        if ("vip".equals(partnerDto.getType())) {
            int vipcounter = this.partnerDao.vipcounter();
            final int vip = 5;
            if (vipcounter >= vip) {
                throw new Exception("Máximo de socios VIP alcanzado.");
            }
        }
    }

    @Override
    public int countActiveGuest(long partnerId) throws Exception {
        return guestDao.countGuests(partnerId);
    }

    @Override
    public void guestLimit(PartnerDto partnerDto) throws Exception {
        if ("regular".equals(partnerDto.getType())) {
            int guestCount = this.guestDao.countGuests(partnerDto.getId());
            final int guest = 3;
            if (guestCount >= guest) {
                throw new Exception("Maximo de invitados alcanzado.");
            }
        }
    }

    @Override
    public void promotionVip() throws Exception {

        PartnerDto partnerDto = this.partnerDao.existByPartner(user);
        vipLimit(partnerDto);
        if ("regular".equals(partnerDto.getType())) {
            int vipcounter = partnerDao.vipcounter();
            final int vip = 5;
            partnerDto.setType("vip");

            if (vipcounter >= vip) {
                throw new Exception("Máximo de socios VIP alcanzado.");
            }

            partnerDto.setType("vip");
            this.partnerDao.updatePartnerType(partnerDto);
            System.out.println("Solicitud a VIP ha sido aceptada.");
        } else {
            System.out.println("tu estado -VIP- ha sido actualizado exitosamente");

        }

    }

    @Override
    public void createInvoice() throws Exception {
        UserDto userDto = ServiceClub.user;
        System.out.println("ingrese la cantidad de objetos/items por comprar:");
        int items = invoiceValidator.validItem(Utils.getReader().nextLine());

        InvoiceDto invoiceDto = new InvoiceDto();

        PartnerDto partnerDto = partnerDao.existByPartner(userDto);
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(partnerDto);
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("Sin pagar");//predeterminado obvioo
        List<InvoiceDetailDto> invoices = new ArrayList<InvoiceDetailDto>();
        double total = 0;

        for (int i = 0; i < items; i++) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();

            invoiceDetailDto.setItem(i + 1);
            invoiceDetailDto.setDescription("Descripcion" + (i + 1));
            System.out.println("Ingrese el precio del objeto/item" + (i + 1));
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
    public void showInvoice() throws Exception {
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
    public void ad_showInvoice() throws Exception {
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
        System.out.println("numero de objetos por comprar");
        int items = invoiceValidator.validItem(Utils.getReader().nextLine());
        GuestDto guestDto = guestDao.existByGuest(userDto);
        InvoiceDto invoiceDto = new InvoiceDto();
        PersonDto personDto = personDao.findByDocument(userDto.getPersonId());
        invoiceDto.setPersonId(personDto);
        invoiceDto.setPartnerId(guestDto.getPartnerId());
        invoiceDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
        invoiceDto.setStatus("sin pagar");
        List<InvoiceDetailDto> invoices = new ArrayList<InvoiceDetailDto>();
        double total = 0;

        for (int i = 0; i < items; i++) {
            InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();

            invoiceDetailDto.setItem(i + 1);
            invoiceDetailDto.setDescription("Descripcion" + (i + 1));
            System.out.println("definir precio del objeto/item: " + (i + 1));
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
