package app.controllers;

import app.controller_validator.PartnerValidator;
import app.controller_validator.PersonValidator;
import app.controller_validator.UserValidator;
import app.controller_requests.ChangeRol_Request;
import app.controller_requests.Invoice_Request;
import app.dao_interfaces.UserDao;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.service_interfaces.AdminService;
import app.service_interfaces.PartnerService;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/changerolG")
    public ResponseEntity<?> createPartner(@RequestBody ChangeRol_Request request) throws Exception {
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

    @PostMapping("/guestI")
    public ResponseEntity<?> guestInvoice(@RequestBody Invoice_Request request) throws Exception {
        try {
            servic.guestInvoice(request);
            return ResponseEntity.ok("Factura creada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
