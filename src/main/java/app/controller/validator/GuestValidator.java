package app.controller.validator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class GuestValidator extends CommonsValidator {

 

    public void validUserName(String userName) throws Exception {
        super.isValidString("El nombre de usuario ", userName);
    }

    public long validGuestStatus(String id) throws Exception {
        return super.isValidLong("La id esquita ", id);
    }
}
