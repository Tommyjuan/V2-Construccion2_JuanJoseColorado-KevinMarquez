/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.service.interfac;

import app.controllers.requests.InvoiceRequest;
import app.controllers.requests.ParnerInvoice;
import app.controllers.requests.PayInvoice;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import java.util.List;

public interface PartnerService {

    public void createGuest(GuestDto guestDto) throws Exception;

    public void deletePartner() throws Exception;

    public void changeRol(PartnerDto partnerDto) throws Exception;

    public List<GuestDto>  showGuestsForPartner(PartnerDto partnerDto) throws Exception;

    GuestDto getGuestById(long guestId) throws Exception;

    public void updateGuestStatus(GuestDto guestDto) throws Exception;

    public void checkVipLimit(PartnerDto partnerDto) throws Exception;

    public void checkGuestLimit(PartnerDto partnerDto) throws Exception;

    public void vipPromocion () throws Exception;
    
    public int countActiveGuestsByPartner(long partnerId) throws Exception;
    
    public void updateMoney(long partnerId, double amount) throws Exception;
     
    public void createInvoice(InvoiceRequest request) throws Exception;
    
    public void payInvoice(PayInvoice request) throws Exception;
    
    public List<InvoiceDto> showInvoiceForPartner(ParnerInvoice request) throws  Exception;
    
    public void showInvoiceForAdmin() throws Exception;
    
    public void guestInvoice(InvoiceRequest request)throws Exception;
    
}
