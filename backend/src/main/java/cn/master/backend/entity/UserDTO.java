package cn.master.backend.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/3-15:14
 */
@Data
public class UserDTO extends SysUser {
    private List<SysUserGroup> userGroups = new ArrayList<>();
    private List<SysGroup> groups = new ArrayList<>();
}
