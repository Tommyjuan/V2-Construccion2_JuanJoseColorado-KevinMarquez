/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
import app.controllers.requests.CreateUserRequest;
import app.dao.interfaces.InvoiceDao;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.model.Invoice;
import app.service.interfac.AdminService;
import app.service.interfac.PartnerService;
import app.service.x.ServiceClub;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Camilo
 */
@Getter
@Setter
@NoArgsConstructor
@RestController
public class AdminController implements ControllerInterface {

    private static final String MENU = "ingrese la opcion que desea ejecutar: \n 1. Para crear socio \n 2. Para ver lista de facturas \n 3. Para cerrar sesion\n";
    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AdminService service;
    @Autowired
    private PartnerValidator partnerValidator;
    @Autowired
    private PartnerService services;
    @Autowired
    private InvoiceDao invoiceDao;
    @Override
    public void session() throws Exception {
      

    }

  

    @PostMapping("/partner")
    public ResponseEntity createPartner(@RequestBody CreateUserRequest request) throws Exception {

        try {
            String name = request.getName();
            personValidator.validName(name);
            long document = personValidator.validDocument(request.getDocument());
            long celPhone = personValidator.validPhone(request.getCelPhone());
            String userName = request.getUserName();
            userValidator.validUserName(userName);
            String password = request.getPassword();
            userValidator.validUserName(password);
            PersonDto personDto = new PersonDto();
            personDto.setName(name);
            personDto.setDocument(document);
            personDto.setCelPhone(celPhone);
            UserDto userDto = new UserDto();
            userDto.setPersonId(personDto);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            userDto.setRole("partner");
            PartnerDto partnerDto = new PartnerDto();
            partnerDto.setUserId(userDto);
            partnerDto.setMoney(50000);
            partnerDto.setDateCreated(new Timestamp(System.currentTimeMillis()));
            partnerDto.setType("regular");
            this.service.createPartner(partnerDto);
            System.out.println("se ha creado el usuario exitosamente ");
            System.out.println("Tipo de socio: " + partnerDto.getType());
            System.out.println("Sus ingresos actuales son de:" + partnerDto.getMoney());
            System.out.println("Se creo el socio en el dia y hora: " + partnerDto.getDateCreated());
            return new ResponseEntity<>("se ha creado el socio de manera exitosa", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
   @GetMapping("/invoice")
    public ResponseEntity<List<InvoiceDto>> showInvoiceForAdmin() throws Exception {
    List<InvoiceDto> invoices = invoiceDao.findAllInvoices();
    if (invoices.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(invoices);
    } else {
        return ResponseEntity.ok(invoices);
    }
    }

}
