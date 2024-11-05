/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package app.dao.interfaces;

import app.dto.InvoiceDetailDto;
import app.model.InvoiceDetail;

/**
 *
 * @author Camilo
 */
public interface InvoiceDetailDao {
  public void createInvoiceDetail(InvoiceDetail invoiceDetail)throws Exception;
}
