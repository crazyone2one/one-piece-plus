package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author create by 11's papa on 2023/1/7-15:36
 */
@Setter
@Getter
public class EditPassWordRequest {
    private String confirmPassword;
    private String newPassword;
    private String id;
}
