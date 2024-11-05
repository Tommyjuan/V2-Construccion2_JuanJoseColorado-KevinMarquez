/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.service.interfac;

import app.dto.UserDto;

public interface LoginService {

    public void login(UserDto userDto) throws Exception;

    public void logout();
}
