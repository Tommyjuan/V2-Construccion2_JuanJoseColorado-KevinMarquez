/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import app.controller.validator.UserValidator;
import app.dto.UserDto;
import app.service.interfac.LoginService;
import app.service.x.ServiceClub;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Camilo
 */
@Controller
@Getter
@Setter
public class LoginController implements ControllerInterface {
    
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private LoginService service;
    private static final String MENU = "ingrese la opcion que desea: \n 1. para iniciar sesion. \n 2. para detener la ejecucion.";
    private Map<String, ControllerInterface> role;
    public LoginController(AdminController adminController, PartnerController partnerController, GuestController guestController) {
        this.role = new HashMap<String, ControllerInterface>();
        role.put("admin", adminController);
        role.put("partner", partnerController);
        role.put("guest", guestController);
    }

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = menu();
        }

    }

    private boolean menu() {
        try {
            System.out.println(MENU);
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
                this.login();
                return true;
            }
            case "2": {
                System.out.println("se detiene el programa");;
                return false;
            }
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    private void login() throws Exception {
        System.out.println("ingrese el usuario");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        UserDto userDto = new UserDto();
        userDto.setPassword(password);
        userDto.setUserName(userName);
        this.service.login(userDto);
        if (role.get(userDto.getRole()) == null) {
            throw new Exception("Rol invalido");
        }
        role.get(userDto.getRole()).session();

    }

}
