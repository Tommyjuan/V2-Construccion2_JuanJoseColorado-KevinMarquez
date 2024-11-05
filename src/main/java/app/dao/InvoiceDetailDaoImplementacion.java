package app.dao;

import app.dao_interfaces.InvoiceDetailDao;
import app.dao_repositories.InvoiceDetailRepository;
import app.model.InvoiceDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
