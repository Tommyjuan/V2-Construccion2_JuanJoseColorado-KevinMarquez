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
public class PartnerValidator extends CommonsValidator {


    public double validMoney(String money) throws Exception {
        return super.isValidDouble("El valor ingresado", money);
    }

}
