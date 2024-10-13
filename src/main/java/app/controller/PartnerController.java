package app.controller;
import app.controller_validator.GuestValidator;
import app.controller_validator.InvoiceValidator;
import app.controller_validator.PartnerValidator;
import app.controller_validator.PersonValidator;
import app.controller_validator.UserValidator;
import app.dao_interface.GuestDao;
import app.dao_interface.InvoiceDao;
import app.dao_interface.PartnerDao;
import app.dao_interface.PersonDao;
import app.dao_interface.UserDao;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service_interface.PartnerService;
import app.service.ServiceClub;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
@NoArgsConstructor
public class PartnerController implements ControllerInterface {

    @Autowired
    private PartnerValidator partnerValidator;
    private static final String MENU = "ingrese la opcion que desea ejecutar: \n 1. para crear invitado. \n 2. para agragar fondos. \n 3. para mostrar invitados. \n 4. para activar/descativar invitado. \n 5. para solicitar promocion. \n 6. para solicitar baja.  \n 7. para crear factura \n 8. para ver facturas \n 9. para pagar factura  \n 10.para cerrar sesion \n";
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private GuestValidator guestValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PartnerService service;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GuestDao guestDao;
    @Autowired
    private InvoiceValidator invoiceValidator;
    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = partnerSession();
        }

    }

    private boolean partnerSession() {
        try {
            System.out.println("bienvenido " + ServiceClub.user.getUserName());
            System.out.print(MENU);
            String option = Utils.getReader().nextLine();
            return options(option);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private boolean options(String option) throws Exception {
        switch (option) {
            case "1": {
                this.createGuest();
                return true;
            }
            case "2": {
                this.addFouns();
                return true;
            }
            case "3": {
                this.statusGuest();
                return true;
            }
            case "4": {
                this.changeStatus();
                return true;
            }
            case "5": {
                this.vipPromocion();
                return true;
            }
            case "6": {

                this.deletePartner();
                return false;
            }
            case "7": {
                this.createVoice();
                return true;
            }
            case "8": {
                this.statusInvoice();
                return true;
            }
            case "9": {
                this.payVoice();
                return true;
            }

            case "10": {
                System.out.println("se ha cerrado sesion");
                return false;
            }
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    public void createGuest() throws Exception {
        System.out.println("Ingrese el nombre del invitado");
        String name = Utils.getReader().nextLine();
        personValidator.validName(name);
        System.out.println("ingrese la cedula");
        long document = personValidator.validDocument(Utils.getReader().nextLine());
        long celPhone = ValidPhoneNumber();
        System.out.println("ingrese el usuario del invitado");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña del invitado ");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        PersonDto personDto = new PersonDto();
        personDto.setName(name);
        personDto.setDocument(document);
        personDto.setCelPhone(celPhone);
        UserDto userDto = new UserDto();
        userDto.setPersonId(personDto);
        userDto.setUserName(userName);
        userDto.setPassword(password);
        userDto.setRole("guest");
        GuestDto guestDto = new GuestDto();
        guestDto.setUserId(userDto);
        guestDto.setStatus("inactivo");
        System.out.println("se ha creado el usuario exitosamente ");
        this.service.createGuest(guestDto);
    }

    private long ValidPhoneNumber() throws NumberFormatException {
        while (true) {
            System.out.println("Ingrese el número de celular (mínimo 10 dígitos):");
            String cellPhoneInput = Utils.getReader().nextLine();
            if (cellPhoneInput.matches("\\d{10,}")) { // Verifica que el input tenga al menos 10 dígitos
                return Long.parseLong(cellPhoneInput);
            } else {
                System.out.println("El número de celular debe tener al menos 10 dígitos. Inténtelo nuevamente.");
            }
        }
    }

    public void deletePartner() throws Exception {
        this.service.deletePartner();

    }

    public void statusGuest() throws Exception {
        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        if (partnerDto == null) {
            System.out.println("No se encontró un socio asociado al usuario.");
            return;
        }
        service.showGuestsForPartner(partnerDto);

    }

    public void changeStatus() throws Exception {

        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        System.out.println("Ingrese el ID del invitado cuyo estado desea cambiar:");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        GuestDto guestDto = service.getGuestById(guestId);

        System.out.println("Ingrese el nuevo estado (activo/inactivo):");
        String Status = Utils.getReader().nextLine();
        guestDto.setStatus(Status);

        service.updateGuestStatus(guestDto);
        System.out.println("Estado del invitado actualizado exitosamente.");
        service.checkGuestLimit(partnerDto);
    }

    public void addFouns() throws Exception {
        this.service.updateMoney();
    }

    public void vipPromocion() throws Exception {
        this.service.vipPromocion();
    }

    public void createVoice() throws Exception {
        this.service.createInvoice();
    }

    public void statusInvoice() throws Exception {
        this.service.showInvoiceForPartner();
        
    }
    public void payVoice() throws Exception {
      
        this.service.payInvoice();
        
    }
    }

 

