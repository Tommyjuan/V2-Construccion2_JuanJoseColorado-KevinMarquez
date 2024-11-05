package app.service_interfaces;

import app.controller_requests.Invoice_Request;
import app.controller_requests.ParnerInvoice_Request;
import app.controller_requests.PayInvoice_Request;
import app.dto.GuestDto;
import app.dto.InvoiceDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import java.util.List;

public interface PartnerService {

    public void createGuest(GuestDto guestDto) throws Exception;

    public void deletePartner() throws Exception;

    public void changeRol(PartnerDto partnerDto) throws Exception;

    public List<GuestDto> showGuestsForPartner(PartnerDto partnerDto) throws Exception;

    GuestDto getGuestById(long guestId) throws Exception;

    public void updateGuestStatus(GuestDto guestDto) throws Exception;

    public void checkVipLimit(PartnerDto partnerDto) throws Exception;

    public void checkGuestLimit(PartnerDto partnerDto) throws Exception;

    public void vipPromocion() throws Exception;

    public int countActiveGuestsByPartner(long partnerId) throws Exception;

    public void updateMoney(long partnerId, double amount) throws Exception;

    public void createInvoice(Invoice_Request request) throws Exception;

    public void payInvoice(PayInvoice_Request request) throws Exception;

    public List<InvoiceDto> showInvoiceForPartner(ParnerInvoice_Request request) throws Exception;

    public void showInvoiceForAdmin() throws Exception;

    public void guestInvoice(Invoice_Request request) throws Exception;

}
