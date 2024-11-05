package app.dao_repositories;

import app.model.Guest;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE Guest p SET p.status = ?1 WHERE p.id = ?2")
    void updateGuestStatus(String status, Long id);

    public Guest findByUserId_Id(Long id);

    Optional<Guest> findById(Long guestId);

}
