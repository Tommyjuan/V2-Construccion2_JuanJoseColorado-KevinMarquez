package app.controller_requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayInvoice_Request {

    public Long partnerId;
    public Long invoiceId;
}
