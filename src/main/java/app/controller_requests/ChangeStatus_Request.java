package app.controller_requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeStatus_Request {

    private long guestId;
    private String status;
}
