package cn.master.backend.request;

import lombok.Data;

/**
 * @author create by 11's papa on 2022/12/29-10:50
 */
@Data
public class AuthenticateRequest {
    private String name;
    private String email;
    private String password;
}
