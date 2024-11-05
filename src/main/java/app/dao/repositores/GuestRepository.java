/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.repositores;

import app.model.Guest;
import app.model.Partner;
import jakarta.transaction.Transactional;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Camilo
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE Guest p SET p.status = ?1 WHERE p.id = ?2")
    void updateGuestStatus(String status, Long id);
    public Guest findByUserId_Id(Long id);
    Optional<Guest>findById(Long guestId);
    
}
