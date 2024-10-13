
package app.dao;
import app.dao_interface.UserDao;
import app.dao_repositores.UserRepository;
import app.dto.UserDto;
import app.helpers.Helper;
import app.model.User;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Transactional  
    public void updateUserRole(UserDto userDto) throws Exception {
        userRepository.updateUserRole(userDto.getRole(), userDto.getUserName());
    }
    
    @Override
    public void deleteUser(UserDto userDto) throws Exception {
        User user = Helper.parse(userDto);
        userRepository.delete(user);

    }
     
}
