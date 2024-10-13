package app.controller;
import app.controller_validator.UserValidator;
import app.dto.UserDto;
import app.service_interface.LoginService;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
public class LoginController implements ControllerInterface {
    
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private LoginService service;
    private static final String MENU = "INGRESA: \n 1. iniciar sesion \n 2. terminar proceso \n";
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
