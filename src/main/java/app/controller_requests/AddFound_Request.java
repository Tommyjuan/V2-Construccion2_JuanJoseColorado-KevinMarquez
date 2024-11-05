package app.controller_requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddFound_Request {

    private long partnerId;
    private double amount;

}
