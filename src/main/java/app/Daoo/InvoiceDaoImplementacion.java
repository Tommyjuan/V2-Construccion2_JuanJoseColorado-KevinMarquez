/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.Daoo;

import app.config.MYSQLConnection;
import app.dao.interfaces.InvoiceDao;
import app.dao.repositores.InvoiceRepository;
import app.dto.InvoiceDto;
import app.helpers.Helper;
import app.model.Invoice;
import app.model.Partner;
import app.model.Person;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class InvoiceDaoImplementacion implements InvoiceDao{
    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public void createInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
                
    }
    @Override
   public InvoiceDto existsByIDInvoice(long invoiceId) throws Exception {
    Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        return Helper.parse(optionalInvoice.get());
    
           
}

    @Override
    public List<InvoiceDto> statusInvoice(InvoiceDto invoiceDto) throws Exception {
         List<InvoiceDto> invoices = new ArrayList<>();
        String query = "SELECT ID,PERSONID,PARTNERID,CREATIONDATE, AMOUNT,STATUS FROM INVOICE WHERE PARTNERID = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, invoiceDto.getId());
        ResultSet resulSet = preparedStatement.executeQuery();
        while (resulSet.next()) {
            Invoice invoice= new Invoice();
            invoice.setId(resulSet.getLong("ID"));
            invoice.setStatus(resulSet.getString("STATUS"));
            invoice.setDateCreate(resulSet.getTimestamp("CREATIONDATE"));
            invoice.setAmount(resulSet.getDouble("AMOUNT"));
            Person person = new Person();
            person.setId(resulSet.getLong("PERSONID"));
            person.setDocument(person.getDocument());
            Partner partner = new Partner();
            partner.setId(resulSet.getLong("PARTNERID"));
            invoice.setPartnerId(partner);
            invoices.add(Helper.parse(invoice));
        }
        resulSet.close();
        preparedStatement.close();
        return invoices;
    }
    public List<InvoiceDto> findAllInvoices() throws Exception {
        List<InvoiceDto> invoices = new ArrayList<>();
        String query = "SELECT ID, PERSONID, PARTNERID, CREATIONDATE, AMOUNT, STATUS FROM INVOICE";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getLong("ID"));
            invoice.setStatus(resultSet.getString("STATUS"));
            invoice.setDateCreate(resultSet.getTimestamp("CREATIONDATE"));
            invoice.setAmount(resultSet.getDouble("AMOUNT"));
            Person person = new Person();
            person.setId(resultSet.getLong("PERSONID"));
            Partner partner = new Partner();
            partner.setId(resultSet.getLong("PARTNERID"));
            invoice.setPartnerId(partner);
            invoices.add(Helper.parse(invoice));
        }
        resultSet.close();
        preparedStatement.close();
        return invoices;
    }

    
    @Override
    @Transactional
    public void changeStatus(InvoiceDto invoiceDto) throws Exception {
         invoiceRepository.changeStatus(invoiceDto.getStatus(), invoiceDto.getId());
    }
    }


