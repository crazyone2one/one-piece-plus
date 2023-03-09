package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
@Data
@TableName("sys_user")
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * User name
     */
    @TableField("name")
    private String name;

    /**
     * E-Mail address
     */
    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    /**
     * User status
     */
    @TableField("status")
    private String status;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("language")
    private String language;

    @TableField("last_workspace_id")
    private String lastWorkspaceId;

    @TableField("last_project_id")
    private String lastProjectId;

    /**
     * Phone number of user
     */
    @TableField("phone")
    private String phone;
    @TableField("source")
    private String source;
}
