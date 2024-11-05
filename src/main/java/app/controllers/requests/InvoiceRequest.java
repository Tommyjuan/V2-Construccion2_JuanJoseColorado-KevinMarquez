/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers.requests;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Camilo
 */
@Getter
@Setter
@NoArgsConstructor
public class InvoiceRequest {
     private long userId;
    private List <InvoiceItem> items;
    public Long partnerId;
     public Long personId;
     public Long guestId;
}
