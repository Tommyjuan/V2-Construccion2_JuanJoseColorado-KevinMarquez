/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.interfaces;

import app.dto.PartnerDto;
import app.dto.PersonDto;

/**
 *
 * @author Camilo
 */
public interface PersonDao {

    public boolean existByDocument(PersonDto personDto) throws Exception;

    public void createPerson(PersonDto personDto) throws Exception;

    public void deletePerson(PersonDto personDto) throws Exception;

    public PersonDto findByDocument(PersonDto personDto) throws Exception;

    public PersonDto findById(long personId) throws Exception; 
}
