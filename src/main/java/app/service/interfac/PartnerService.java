/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.service.interfac;

import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.UserDto;

public interface PartnerService {

    public void createGuest(GuestDto guestDto) throws Exception;

    public void deletePartner() throws Exception;

    public void changeRol(PartnerDto partnerDto) throws Exception;

    public void showGuestsForPartner(PartnerDto partnerDto) throws Exception;

    GuestDto getGuestById(long guestId) throws Exception;

    public void updateGuestStatus(GuestDto guestDto) throws Exception;

    public void checkVipLimit(PartnerDto partnerDto) throws Exception;

    public void checkGuestLimit(PartnerDto partnerDto) throws Exception;

    public void vipPromocion () throws Exception;
    
    public int countActiveGuestsByPartner(long partnerId) throws Exception;
    
    public void updateMoney() throws Exception;
     
    public void createInvoice() throws Exception;
    
    public void payInvoice() throws Exception;
    
    public void showInvoiceForPartner() throws  Exception;
    
    public void showInvoiceForAdmin() throws Exception;
    
    public void guestInvoice()throws Exception;
}
