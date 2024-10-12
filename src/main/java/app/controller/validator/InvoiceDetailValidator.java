/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Camilo
 */
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
