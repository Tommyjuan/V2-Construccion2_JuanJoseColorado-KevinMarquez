/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.Daoo;

import app.config.MYSQLConnection;
import app.dao.interfaces.PersonDao;
import app.dto.PersonDto;
import app.helpers.Helper;
import app.model.Person;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import app.dao.repositores.PersonRepository;
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
public class PersonDaoImplementation implements PersonDao {
    @Autowired
    PersonRepository personRepository;
    @Override
    public boolean existByDocument(PersonDto personDto) throws Exception {
        return personRepository.existsByDocument(Helper.parse(personDto).getDocument());
    }

    @Override
   public void createPerson(PersonDto personDto) throws Exception {
    Person person = Helper.parse(personDto);
        personRepository.save(person);
}

    @Override
    public void deletePerson(PersonDto personDto) throws Exception {
        Person person = Helper.parse(personDto);
        personRepository.delete(person);
    }

    @Override
    public PersonDto findByDocument(PersonDto personDto) throws Exception {
        Person person= personRepository.findByDocument(personDto.getDocument());
     return Helper.parse(person);
    
}

    @Override
    public PersonDto findById(long personId) throws Exception {
            Person person = personRepository.findById(personId)
                .orElseThrow(() -> new Exception("Socio no encontrado."));

        return Helper.parse(person); 
    }


}
