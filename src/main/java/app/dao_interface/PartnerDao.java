package app.dao_interface;

import app.dto.PartnerDto;
import app.dto.UserDto;

public interface PartnerDao {

    public void createPartner(PartnerDto partnerDto) throws Exception;

    public void deletePartner(PartnerDto partnerDto) throws Exception;

    public PartnerDto getMoneyByPartner(double getMoney) throws Exception;

    public PartnerDto existByPartner(UserDto userDto) throws Exception;

    public void updateMoney(PartnerDto partnerDto) throws Exception;

    public void updatePartnerType(PartnerDto partnerDto) throws Exception;

    public PartnerDto getTypeByPartner(PartnerDto partnerDto) throws Exception;

    public int vipcounter() throws Exception;

}
