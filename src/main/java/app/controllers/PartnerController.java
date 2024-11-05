package app.controllers;

import app.controller_validator.GuestValidator;
import app.controller_validator.InvoiceValidator;
import app.controller_validator.PartnerValidator;
import app.controller_validator.PersonValidator;
import app.controller_validator.UserValidator;
import app.controller_requests.AddFound_Request;
import app.controller_requests.ChangeStatus_Request;
import app.controller_requests.CreateUser_Request;
import app.controller_requests.Invoice_Request;
import app.controller_requests.ParnerInvoice_Request;
import app.controller_requests.PayInvoice_Request;
import app.dao_interfaces.GuestDao;
import app.dao_interfaces.InvoiceDao;
import app.dao_interfaces.PartnerDao;
import app.dao_interfaces.PersonDao;
import app.dao_interfaces.UserDao;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service_interfaces.PartnerService;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    }

    @PostMapping("/createG")
    public ResponseEntity<?> createGuest(@RequestBody CreateUser_Request request) {
        try {

            String name = request.getName();
            personValidator.validName(name);
            long document = personValidator.validDocument(request.getDocument());
            long celPhone = personValidator.validPhone(request.getCelPhone());
            String userName = request.getUserName();
            userValidator.validUserName(userName);
            String password = request.getPassword();
            userValidator.validPassword(password);
            long partnerId = request.getPartnerId();
            PersonDto personDto = new PersonDto();
            personDto.setName(name);
            personDto.setDocument(document);
            personDto.setCelPhone(celPhone);
            UserDto userDto = new UserDto();
            userDto.setPersonId(personDto);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            userDto.setRole("guest");
            PartnerDto partnerDto = new PartnerDto();
            partnerDto.setId(partnerId);
            PartnerDto Partner = partnerDao.findById(partnerId);
            if (Partner == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Socio no encontrado.");
            }
            GuestDto guestDto = new GuestDto();
            guestDto.setPartnerId(partnerDto);
            guestDto.setUserId(userDto);
            guestDto.setStatus("inactivo");
            this.service.createGuest(guestDto);
            System.out.println("Se ha creado el Invitado exitosamente.");
            return new ResponseEntity<>("El usuario se ha creado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteP")//paila
    public ResponseEntity<String> deletePartner() {
        try {
            this.service.deletePartner();
            return new ResponseEntity<>("Su cuenta ha sido eliminada exitosamente.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrió un error al eliminar la cuenta.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/statusG")
    public ResponseEntity<?> statusGuest(@RequestBody CreateUser_Request request) throws Exception {
        try {

            PartnerDto partnerDto = new PartnerDto();
            long partnerId = request.getPartnerId();
            partnerDto.setId(partnerId);
            partnerDto.setId(request.getPartnerId());

            PartnerDto Partner = partnerDao.findById(partnerId);
            if (Partner == null) {
                System.out.println("No se encontró un socio asociado al usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un socio asociado al usuario.");
            }

            List<GuestDto> guests = service.showGuestsForPartner(partnerDto);
            return ResponseEntity.ok(guests);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/changeStatusG")
    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatus_Request request) {
        try {
            long guestId = request.getGuestId();
            String status = request.getStatus();
            GuestDto guestDto = service.getGuestById(guestId);
            guestDto.setStatus(status);
            service.updateGuestStatus(guestDto);
            return ResponseEntity.ok("Estado del invitado actualizado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/addFound")
    public ResponseEntity<?> addFunds(@RequestBody AddFound_Request request) {
        try {
            service.updateMoney(request.getPartnerId(), request.getAmount());

            return ResponseEntity.ok("Fondos añadidos exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//no le se le hizo post, me rendi en este punto
    public void vipPromocion() throws Exception {
        this.service.vipPromocion();
    }

    @PostMapping("/createI")
    public ResponseEntity<?> createVoice(@RequestBody Invoice_Request request) {
        try {
            service.createInvoice(request);
            return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/statusI")
    public ResponseEntity<?> statusInvoice(@RequestBody ParnerInvoice_Request request) throws Exception {
        try {
            PartnerDto partnerDto = new PartnerDto();
            long partnerId = request.getPartnerId();

            partnerDto.setId(partnerId);
            PartnerDto Partner = partnerDao.findById(partnerId);
            if (Partner == null) {
                System.out.println("No se encontró un socio asociado al usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un socio asociado al usuario.");
            }
            List<InvoiceDto> invoices = service.showInvoiceForPartner(request);

            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/payV")
    public ResponseEntity<?> payVoice(@RequestBody PayInvoice_Request request) throws Exception {
        try {
            service.payInvoice(request);
            return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
