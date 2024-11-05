package app.dao_repositories;

import app.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE Invoice i SET i.status = :status WHERE i.id = :id")
    void changeStatus(@Param("status") String status, @Param("id") Long id);
}