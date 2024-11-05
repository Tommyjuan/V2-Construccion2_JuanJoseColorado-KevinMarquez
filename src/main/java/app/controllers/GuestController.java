/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.controllers.requests.ChangeRol;
import app.controllers.requests.CreateUserRequest;
import app.controllers.requests.InvoiceRequest;
import app.dao.interfaces.UserDao;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.service.interfac.AdminService;
import app.service.interfac.PartnerService;
import app.service.x.ServiceClub;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
@Setter
@NoArgsConstructor
public class GuestController implements ControllerInterface {

    private static final String MENU = "ingrese la opcion que desea ejecutar:  \n 1. Pasar a Socio \n 2. Para crear factura \n 3. Para cerrar sesion \n";
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AdminService service;
    @Autowired
    private PartnerService servic;
    @Autowired
    private PartnerValidator partnerValidator;
    @Autowired
    private UserDao userDao;

    @Override
    public void session() throws Exception {
    }

    @PostMapping("/changerol")
    public ResponseEntity <?> createPartner(@RequestBody ChangeRol request) throws Exception {
        try {

            UserDto userDto = userDao.findById(request.getUserId());
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
            userDto.setRole("partner");
            PartnerDto partnerDto = new PartnerDto();
            partnerDto.setUserId(userDto);
            partnerDto.setMoney(50000);
            partnerDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
            partnerDto.setType("regular");

            System.out.println("se ha creado el usuario exitosamente ");
            System.out.println("Tipo de socio: " + partnerDto.getType());
            System.out.println("Sus ingresos actuales son de:" + partnerDto.getMoney());
            System.out.println("Se creo el socio en el dia y hora: " + partnerDto.getDateCreated());
            this.servic.changeRol(partnerDto);
            return ResponseEntity.ok("El usuario ha sido convertido en socio exitosamente.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/guest-invoice")
    public ResponseEntity<?> guestInvoice(@RequestBody InvoiceRequest request) throws Exception {
        try {
            servic.guestInvoice(request);
             return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        }
        
    }


