package app.dao_repositories;

import app.model.Partner;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    public Partner findByUserId_Id(Long id);

    public Partner findByMoney(Double money);

    @Modifying
    @Transactional
    @Query("UPDATE Partner p SET p.money = :money WHERE p.userId.id = :userId")
    void updateMoneyByUserId(@Param("money") Double money, @Param("userId") Long userId);

    public Partner findByType(String type);

    @Modifying
    @Query("UPDATE Partner p SET p.type = ?1 WHERE p.id = ?2")
    void updatePartnerType(String type, Long id);

}