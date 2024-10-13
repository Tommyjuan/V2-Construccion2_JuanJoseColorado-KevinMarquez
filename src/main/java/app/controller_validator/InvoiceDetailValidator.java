package app.controller_validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class InvoiceDetailValidator extends CommonsValidator {

 
    

    public void validItems(String items) throws Exception {
        super.isValidString("items de la factura", items);
    }
    public long validId(String Id) throws Exception {
        return super.isValidLong("la cedula de la persona ", Id);
}
}
