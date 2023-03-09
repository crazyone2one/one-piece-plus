package cn.master.backend.entity;

import lombok.Data;

/**
 * @author create by 11's papa on 2023/1/5-12:47
 */
@Data
public class UserGroupDTO {
    private String userId;
    private String groupId;
    private String sourceId;
    private String name;
    /**
     * 用户组所属类型
     */
    private String type;
}
