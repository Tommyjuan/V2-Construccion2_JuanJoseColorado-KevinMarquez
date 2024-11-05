/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.repositores;

import app.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Camilo
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    public User findByUserName(String userName);

    public boolean existsByUserName(String userName);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = :role WHERE u.userName = :userName")
    public void updateUserRole(String role, String userName);
    
    
}
