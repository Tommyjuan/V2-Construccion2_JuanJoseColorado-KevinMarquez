package app.controller_requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUser_Request {

    public String name;
    public String celPhone;
    public String userName;
    public String document;
    public String password;
    public Long partnerId;
    public Long userId;

}
