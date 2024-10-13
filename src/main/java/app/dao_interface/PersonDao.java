package app.dao_interface;

import app.dto.PersonDto;

public interface PersonDao {

    public boolean existByDocument(PersonDto personDto) throws Exception;

    public void createPerson(PersonDto personDto) throws Exception;

    public void deletePerson(PersonDto personDto) throws Exception;

    public PersonDto findByDocument(PersonDto personDto) throws Exception;

}
