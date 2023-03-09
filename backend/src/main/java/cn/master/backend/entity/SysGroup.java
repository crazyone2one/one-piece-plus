package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-01
 */
@Data
@TableName("sys_group")
public class SysGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    /**
     * 是否是系统用户组
     */
    @TableField("system")
    private Boolean system;

    /**
     * 所属类型
     */
    @TableField("type")
    private String type;

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

    /**
     * 创建人(操作人）
     */
    @TableField("creator")
    private String creator;

    /**
     * 应用范围
     */
    @TableField("scope_id")
    private String scopeId;

    @TableField(exist = false)
    private String scopeName;
    @TableField(exist = false)
    private Long memberSize;
}
