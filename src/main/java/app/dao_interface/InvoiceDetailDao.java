package app.dao_interface;

import app.model.InvoiceDetail;

public interface InvoiceDetailDao {
  public void createInvoiceDetail(InvoiceDetail invoiceDetail)throws Exception;
}
