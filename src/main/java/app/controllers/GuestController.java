/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.controller.validator.PartnerValidator;
import app.controller.validator.PersonValidator;
import app.controller.validator.UserValidator;
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
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
@NoArgsConstructor
public class GuestController implements ControllerInterface {

    private static final String MENU = "ingrese la opcion que desea ejecutar:  \n 1. Pasar a Socio \n 2. Para crear factura (No me dio el metodo profe) \n 3. Para cerrar sesion \n";
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

  
    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = menu();
        }
    }

    private boolean menu() {
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
            case "": {
                System.out.println("");
                return true;
            }
            case "1": {
                this.createPartner();
                return true;
            }
            case "2": {
                this.guestInvoice();
                return true;
            }
            case "3": {
                System.out.println("se ha cerrado sesion");
                return false;
            }

            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    public void createPartner() throws Exception {
        UserDto userDto = ServiceClub.user;
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
    }

    public void guestInvoice() throws Exception {
    this.servic.guestInvoice();
    }
    
}
