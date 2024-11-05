/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.Daoo;

import app.config.MYSQLConnection;
import app.dao.interfaces.GuestDao;
import app.dao.interfaces.PartnerDao;
import app.dao.repositores.GuestRepository;
import app.dto.GuestDto;
import app.dto.PartnerDto;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Guest;
import app.model.Partner;
import app.model.User;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Camilo
 */
@Service
@NoArgsConstructor
@Getter
@Setter
public class GuestDaoImplemetation implements GuestDao {
    @Autowired
    GuestRepository guestRepository;
    
    @Override
    public void createGuest(GuestDto guestDto) throws SQLException {
        Guest guest = Helper.parse(guestDto);
       guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(GuestDto guestDto) throws Exception {
        Guest guest = Helper.parse(guestDto);
        guestRepository.delete(guest);

    }

    @Override
    public GuestDto existByGuest(UserDto userDto) throws Exception {
        Optional<Guest> optionalGuest = guestRepository.findById(userDto.getId());
                return Helper.parse(optionalGuest.get());
    }

    @Override
    public List<GuestDto> statusGuest(PartnerDto partnerDto) throws Exception {
        List<GuestDto> guests = new ArrayList<>();
        String query = "SELECT ID,USERID,PARTNERID,STATUS FROM GUEST WHERE PARTNERID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partnerDto.getId());
        ResultSet resulSet = preparedStatement.executeQuery();
        while (resulSet.next()) {
            Guest guest = new Guest();
            guest.setId(resulSet.getLong("ID"));
            guest.setStatus(resulSet.getString("STATUS"));
            User user = new User();
            user.setId(resulSet.getLong("USERID"));
            guest.setUserId(user);
            Partner partner = new Partner();
            partner.setId(resulSet.getLong("PARTNERID"));
            guest.setPartnerId(partner);
            guests.add(Helper.parse(guest));
        }
        resulSet.close();
        preparedStatement.close();
        return guests;
    }
    @Transactional
    @Override
    public void changeStatus(GuestDto guestDto) throws Exception {
        guestRepository.updateGuestStatus(guestDto.getStatus(),guestDto.getId());
    }

    @Override
    public GuestDto getGuestById(long guestId) throws Exception {
           Optional<Guest> optionalGuest = guestRepository.findById(guestId);
               return Helper.parse(optionalGuest.get());
    }

    @Override
    public List<GuestDto> getGuestsByPartnerId(long partnerId) throws Exception {
        List<GuestDto> guests = new ArrayList<>();
        String query = "SELECT ID,USERID,PARTNERID,STATUS FROM GUEST WHERE PARTNERID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, partnerId);
        ResultSet resulSet = preparedStatement.executeQuery();
        while (resulSet.next()) {
            Guest guest = new Guest();
            guest.setId(resulSet.getLong("ID"));
            guest.setStatus(resulSet.getString("STATUS"));
            User user = new User();
            user.setId(resulSet.getLong("USERID"));
            guest.setUserId(user);
            Partner partner = new Partner();
            partner.setId(resulSet.getLong("PARTNERID"));
            guest.setPartnerId(partner);
            guests.add(Helper.parse(guest));
        }
        resulSet.close();
        preparedStatement.close();
        return guests;
    }

    @Override
    public int countGuestsByPartnerId(long partnerId) throws Exception {
         String query = "SELECT COUNT(*) AS count FROM GUEST WHERE PARTNERID = ? AND STATUS = 'activo'";
    PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
    preparedStatement.setLong(1, partnerId);
    ResultSet resultSet = preparedStatement.executeQuery();
    int count = 0;
    if (resultSet.next()) {
        count = resultSet.getInt("count");
    }
    resultSet.close();
    preparedStatement.close();
    return count;
    }
    @Override
    public GuestDto findById(long guestId) throws Exception {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new Exception("Socio no encontrado."));

        return Helper.parse(guest);
    }
   

    }

        
    
  

   
