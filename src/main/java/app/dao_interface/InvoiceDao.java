package app.dao_interface;

import app.dto.InvoiceDto;
import app.model.Invoice;
import java.util.List;

public interface InvoiceDao {

    public void createInvoice(Invoice invoice) throws Exception;

    public List<InvoiceDto> statusInvoice(InvoiceDto invoiceDto) throws Exception;

    public InvoiceDto existsByIDInvoice(long invoiceId) throws Exception;

    public void changeStatus(InvoiceDto invoiceDto) throws Exception;

    public List<InvoiceDto> findAllInvoices() throws Exception;
}
