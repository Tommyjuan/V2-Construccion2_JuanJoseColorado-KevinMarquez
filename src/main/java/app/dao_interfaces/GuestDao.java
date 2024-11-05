package app.dao_interfaces;

import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import java.util.List;

public interface GuestDao {

    public void createGuest(GuestDto guestDto) throws Exception;

    public void deleteGuest(GuestDto guestDto) throws Exception;

    public GuestDto existByGuest(UserDto userDto) throws Exception;

    public void changeStatus(GuestDto guestDto) throws Exception;

    public GuestDto getGuestById(long guestId) throws Exception;

    public List<GuestDto> statusGuest(PartnerDto partnerDto) throws Exception;

    public List<GuestDto> getGuestsByPartnerId(long partnerId) throws Exception;

    public int countGuestsByPartnerId(long partnerId) throws Exception;

    public GuestDto findById(long guestId) throws Exception;

}
