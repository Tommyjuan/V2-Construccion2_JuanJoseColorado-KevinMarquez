package app.dao_interfaces;

import app.dto.PartnerDto;

public interface PartnerDao {

    public void createPartner(PartnerDto partnerDto) throws Exception;

    public void deletePartner(PartnerDto partnerDto) throws Exception;

    public boolean existByPartner(PartnerDto partnerDto) throws Exception;

    public PartnerDto getMoneyByPartner(double getMoney) throws Exception;

    public void updateMoney(PartnerDto partnerDto) throws Exception;

    public PartnerDto getTypeByPartner(PartnerDto partnerDto) throws Exception;

    public int countVipPartners() throws Exception;

    public void updatePartnerType(PartnerDto partnerDto) throws Exception;

    PartnerDto findById(long partnerId) throws Exception;
}
