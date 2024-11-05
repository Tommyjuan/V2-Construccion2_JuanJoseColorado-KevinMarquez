/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.Daoo;

import app.dao.interfaces.InvoiceDetailDao;
import app.dao.repositores.InvoiceDetailRepository;
import app.dto.InvoiceDetailDto;
import app.helpers.Helper;
import app.model.Invoice;
import app.model.InvoiceDetail;
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
public class InvoiceDetailDaoImplementacion implements InvoiceDetailDao {
    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public void createInvoiceDetail(InvoiceDetail invoiceDetail) throws Exception {
        invoiceDetailRepository.save(invoiceDetail);
    }
    
}

