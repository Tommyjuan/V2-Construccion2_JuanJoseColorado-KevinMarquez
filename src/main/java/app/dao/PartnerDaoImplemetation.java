package app.dao;

import app.config.MYSQLConnection;
import app.dao_interfaces.PartnerDao;
import app.dao_repositories.PartnerRepository;
import app.dto.PartnerDto;
import app.helpers.Helper;
import app.model.Partner;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Getter
@Setter
public class PartnerDaoImplemetation implements PartnerDao {

    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public void createPartner(PartnerDto partnerDto) throws Exception {
        Partner partner = Helper.parse(partnerDto);
        partnerRepository.save(partner);
    }

    @Override
    public void deletePartner(PartnerDto partnerDto) throws Exception {
        Partner partner = Helper.parse(partnerDto);
        partnerRepository.delete(partner);

    }

    @Override
    public boolean existByPartner(PartnerDto partnerDto) throws Exception {
        Partner partner = partnerRepository.findByUserId_Id(partnerDto.getId());
        return partner != null;
    }

    @Override
    public PartnerDto getMoneyByPartner(double money) throws Exception {
        Partner partner = partnerRepository.findByMoney(money);
        return Helper.parse(partner);

    }

    @Override
    @Transactional
    public void updateMoney(PartnerDto partnerDto) throws Exception {
        partnerRepository.updateMoneyByUserId(partnerDto.getMoney(), partnerDto.getUserId().getId());
    }

    @Override
    public PartnerDto getTypeByPartner(PartnerDto partnerDto) throws Exception {
        Partner partner = partnerRepository.findByType(partnerDto.getType());
        return Helper.parse(partner);
    }

    @Override
    public int countVipPartners() throws Exception {
        String query = "SELECT COUNT(*) FROM PARTNER WHERE TYPE = 'vip'";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    @Override
    @Transactional
    public void updatePartnerType(PartnerDto partnerDto) throws Exception {
        partnerRepository.updatePartnerType(partnerDto.getType(), partnerDto.getId());
    }

    @Override
    public PartnerDto findById(long partnerId) throws Exception {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new Exception("Socio no encontrado."));

        return Helper.parse(partner);
    }
}
