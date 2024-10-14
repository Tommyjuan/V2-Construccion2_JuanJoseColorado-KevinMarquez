package app.controller_validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class InvoiceValidator extends CommonsValidator {

    public double validAmount(String amount) throws Exception {
        return super.isValidDouble("el monto de la factura ", amount);
    }

    public int validItem(String item) throws Exception {
        return super.isValidInteger("el monto de la factura ", item);
    }

    public long validId(String Id) throws Exception {
        return super.isValidLong("la cedula de la persona ", Id);
    }
}
