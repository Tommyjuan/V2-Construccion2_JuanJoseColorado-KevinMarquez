
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.Daoo;

import app.config.MYSQLConnection;
import app.dao.interfaces.UserDao;
import app.dao.repositores.UserRepository;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.Person;
import app.model.User;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class UserDaoImplementation implements UserDao {
  @Autowired
    UserRepository userRepository;

    @Override

    public UserDto findByUserName(UserDto userDto) throws Exception {
        User user = userRepository.findByUserName(userDto.getUserName());
        return Helper.parse(user);
    }

    @Override
    public boolean existsByUserName(UserDto userDto) throws Exception {
        return userRepository.existsByUserName(userDto.getUserName());
    }

    @Override
    public void createUser(UserDto userDto) throws Exception {
        User user = Helper.parse(userDto);
       userRepository.save(user);
    }

    @Override
    public void deleteUser(UserDto userDto) throws Exception {
        User user = Helper.parse(userDto);
        userRepository.delete(user);

    }
     
    @Override
    @Transactional  
    public void updateUserRole(UserDto userDto) throws Exception {
        userRepository.updateUserRole(userDto.getRole(), userDto.getUserName());
    }
}


