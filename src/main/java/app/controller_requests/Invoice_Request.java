package app.controller_requests;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Invoice_Request {

    private long userId;
    private List<InvoiceItem_Request> items;
    public Long partnerId;
    public Long personId;
    public Long guestId;
}
