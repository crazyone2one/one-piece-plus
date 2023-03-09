package cn.master.backend.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/6-11:12
 */
@Data
public class UserGroupPermissionDTO {
    List<SysGroup> groups = new ArrayList<>();
    List<SysUserGroup> userGroups = new ArrayList<>();
}
