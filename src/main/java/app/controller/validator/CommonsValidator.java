/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller.validator;

/**
 *
 * @author Camilo
 */
public abstract class CommonsValidator {

    public CommonsValidator() {
    }

    public void isValidString(String element, String value) throws Exception {
        if (value.equals("")) {
            throw new Exception(element + " no puede ser un valor vacio");
        }
    }

    public int isValidInteger(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new Exception(element + " debe ser un valor valido");
        }
    }

    public long isValidLong(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new Exception(element + " debe ser un valor valido");
        }
    }

    public double isValidDouble(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new Exception(element + " debe ser un valor valido");
        }
    }
}
