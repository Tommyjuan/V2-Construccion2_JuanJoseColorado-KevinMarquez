package app.controller_validator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class PersonValidator extends CommonsValidator {

  
    public void validName(String name) throws Exception {
        super.isValidString("el nombre de la persona ", name);
    }

    public long validDocument(String document) throws Exception {
        return super.isValidLong("la cedula de la persona ", document);
    }

    public long validPhone(String celPhone) throws Exception {
        return super.isValidLong("El numero celular de la persona  ", celPhone);
    }
}
