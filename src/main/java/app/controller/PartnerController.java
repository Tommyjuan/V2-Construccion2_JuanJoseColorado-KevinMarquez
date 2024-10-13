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
    private static final String MENU = "INGRESE DIGITO: \n 1.crear invitado \n 2.mostrar invitados \n 3.activar/desactivar invitado \n 4. promocion VIP \n 5.agregar fondos.  \n 6.crear factura \n 7.ver facturas \n 8.pagar factura   \n 9.abandonar club(baja)  \n 10.para cerrar sesion \n";
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
                this.guestStatus();
                return true;

            }
            case "3": {
                this.guestchangeStatus();
                return true;

            }
            case "4": {
                this.promotionVip();
                return true;

            }
            case "5": {
                this.addFunds();
                return true;
            }
            case "6": {
                this.createInvoice();
                return true;

            }
            case "7": {
                this.statusInvoice();
                return true;
            }
            case "8": {
                this.payInvoice();
                return true;
            }
            case "9": {
                this.deletePartner();
                return false;
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


    public void guestchangeStatus() throws Exception {

        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        System.out.println("Ingrese el ID del invitado:");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        GuestDto guestDto = service.getGuestById(guestId);
        
        System.out.println("Ingrese estado (activo/inactivo):");
        String Status = Utils.getReader().nextLine();
        guestDto.setStatus(Status);
        
        service.updateStatus(guestDto);
        System.out.println("Estado actualizado exitosamente");
        service.guestLimit(partnerDto);
    }

    
    
    
    public void guestStatus() throws Exception {
        PartnerDto partnerDto = partnerDao.existByPartner(ServiceClub.user);
        if (partnerDto == null) {
            System.out.println("No se encontró un socio que tenga relacion con el usuario.");
            return;
        }
        service.watchGuests(partnerDto);

    }

    public void promotionVip() throws Exception {
        this.service.promotionVip();
    }

    public void addFunds() throws Exception {
        this.service.updateMoney();
    }

    public void createInvoice() throws Exception {
        this.service.createInvoice();
    }

    public void statusInvoice() throws Exception {
        this.service.showInvoice();
    }

    public void payInvoice() throws Exception {
        this.service.payInvoice();

    }
    
    public void deletePartner() throws Exception {
        this.service.deletePartner();

    }
}
