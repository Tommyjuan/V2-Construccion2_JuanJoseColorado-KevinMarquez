package app.dao_repositories;

import app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    public boolean existsByDocument(Long document);

    public Person findByDocument(Long document);

}
