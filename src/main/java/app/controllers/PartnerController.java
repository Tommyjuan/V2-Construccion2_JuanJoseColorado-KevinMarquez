/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.Daoo.GuestDaoImplemetation;
import app.Daoo.PartnerDaoImplemetation;
import app.Daoo.PersonDaoImplementation;
import app.Daoo.UserDaoImplementation;
import app.controller.validator.GuestValidator;
import app.controller.validator.InvoiceValidator;
import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.controllers.requests.AddFundsRequest;
import app.controllers.requests.ChangeStatusRequest;
import app.controllers.requests.CreateUserRequest;
import app.controllers.requests.InvoiceRequest;
import app.controllers.requests.ParnerInvoice;
import app.controllers.requests.PayInvoice;
import app.dao.interfaces.GuestDao;
import app.dao.interfaces.InvoiceDao;
import app.dao.interfaces.PartnerDao;
import app.dao.interfaces.PersonDao;
import app.dao.interfaces.UserDao;
import app.dto.GuestDto;
import app.dto.InvoiceDetailDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.model.Partner;
import app.service.interfac.PartnerService;
import app.service.x.ServiceClub;
import static app.service.x.ServiceClub.user;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Camilo
 */
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

    @PostMapping("/guest")
    public ResponseEntity<?> createGuest(@RequestBody CreateUserRequest request) {
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

    @DeleteMapping("/partner1")
    public ResponseEntity<String> deletePartner() {
        try {
            this.service.deletePartner();
            return new ResponseEntity<>("Su cuenta ha sido eliminada exitosamente.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrió un error al eliminar la cuenta.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("status-guest")
    public ResponseEntity<?> statusGuest(@RequestBody CreateUserRequest request) throws Exception {
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

    @PostMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatusRequest request) {
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

    @PostMapping("/add-funds")
    public ResponseEntity<?> addFunds(@RequestBody AddFundsRequest request) {
        try {
            service.updateMoney(request.getPartnerId(), request.getAmount());

            return ResponseEntity.ok("Fondos añadidos exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void vipPromocion() throws Exception {
        this.service.vipPromocion();
    }

    @PostMapping("/create-invoice")
    public ResponseEntity<?> createVoice(@RequestBody InvoiceRequest request) {
        try {
            service.createInvoice(request);
            return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/status-invoice")
    public ResponseEntity<?> statusInvoice(@RequestBody ParnerInvoice request) throws Exception {
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
    @PostMapping("/pay-voices")
    public ResponseEntity<?> payVoice(@RequestBody PayInvoice request) throws Exception {
     try {
         service.payInvoice(request);
         return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
