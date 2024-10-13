package app.controller_validator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class PartnerValidator extends CommonsValidator {


    public double validMoney(String money) throws Exception {
        return super.isValidDouble("El valor ingresado", money);
    }

}
