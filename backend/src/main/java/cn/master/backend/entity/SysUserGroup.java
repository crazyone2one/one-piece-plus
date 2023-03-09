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
 * @since 2022-12-31
 */
@Data
@TableName("sys_user_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("group_id")
    private String groupId;

    @TableField("source_id")
    private String sourceId;

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
}
