package app.service_interface;

import app.dto.GuestDto;
import app.dto.PartnerDto;

public interface PartnerService {

    public void createGuest(GuestDto guestDto) throws Exception;

    public void deletePartner() throws Exception;

    public void changeRol(PartnerDto partnerDto) throws Exception;

    public void watchGuests(PartnerDto partnerDto) throws Exception;

    GuestDto getGuestById(long guestId) throws Exception;

    public void updateStatus(GuestDto guestDto) throws Exception;

    public void vipLimit(PartnerDto partnerDto) throws Exception;

    public void guestLimit(PartnerDto partnerDto) throws Exception;

    public void promotionVip () throws Exception;
    
    public int countActiveGuest(long partnerId) throws Exception;
    
    public void updateMoney() throws Exception;
     
    public void createInvoice() throws Exception;
    
    public void payInvoice() throws Exception;
    
    public void showInvoice() throws  Exception;
    
    public void ad_showInvoice() throws Exception;
    
    public void guestInvoice()throws Exception;
}
