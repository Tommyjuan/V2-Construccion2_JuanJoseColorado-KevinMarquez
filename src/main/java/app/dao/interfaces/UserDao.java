/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.interfaces;

import app.dto.UserDto;

/**
 *
 * @author Camilo
 */
public interface UserDao {

    public UserDto findByUserName(UserDto userDto) throws Exception;

    public boolean existsByUserName(UserDto userDto) throws Exception;

    public void createUser(UserDto userDto) throws Exception;

    public void deleteUser(UserDto userDto) throws Exception;

    public void updateUserRole(UserDto userDto) throws Exception;
    
    public UserDto findById(long userId)throws Exception ;
}
