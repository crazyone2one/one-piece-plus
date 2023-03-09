package cn.master.backend.request;

import cn.master.backend.entity.SysUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/3-15:38
 */
@Data
public class UserRequest extends SysUser {
    private List<Map<String, Object>> roles = new ArrayList<>();
    private List<Map<String, Object>> groups = new ArrayList<>();
}
