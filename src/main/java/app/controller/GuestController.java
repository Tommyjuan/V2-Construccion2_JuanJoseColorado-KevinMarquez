package app.controller;
import app.controller_validator.PartnerValidator;
import app.controller_validator.PersonValidator;
import app.controller_validator.UserValidator;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.service_interface.AdminService;
import app.service_interface.PartnerService;
import app.service.ServiceClub;
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

    private static final String MENU = "ingrese la opcion que desea ejecutar:  \n 1. Pasar a Socio(tiene un problema y si lo ejecuta va asaber cual es, no entendi la razon de que no cambie a partner :(  ) \n 2. (ni camilo supo, y el era el que me estaba asesorando, espero explicacion de esto jsjs) \n 3. Para cerrar sesion \n";
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
        System.out.println("Sus ingresos actuales son:" + partnerDto.getMoney());
        System.out.println("tiempo de creacion: " + partnerDto.getDateCreated());
        this.servic.changeRol(partnerDto);
    }

    public void guestInvoice() throws Exception {
    this.servic.guestInvoice();
    }
    
}
