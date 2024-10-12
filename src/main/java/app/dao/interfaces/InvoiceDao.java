/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.interfaces;

import app.dto.InvoiceDto;
import app.model.Invoice;
import java.util.List;

/**
 *
 * @author Camilo
 */
public interface InvoiceDao {

    public void createInvoice(Invoice invoice) throws Exception;

    public List<InvoiceDto> statusInvoice(InvoiceDto invoiceDto) throws Exception;

    public InvoiceDto existsByIDInvoice(long invoiceId) throws Exception;

    public void changeStatus(InvoiceDto invoiceDto) throws Exception;

    public List<InvoiceDto> findAllInvoices() throws Exception;
}
