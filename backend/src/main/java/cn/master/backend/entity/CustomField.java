package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 自定义字段表
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-09
 */
@Getter
@Setter
@TableName("custom_field")
@AllArgsConstructor
@NoArgsConstructor
public class CustomField implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Custom field name
     */
    @TableField("`name`")
    private String name;

    /**
     * Custom field use scene
     */
    @TableField("scene")
    private String scene;

    /**
     * Custom field type
     */
    @TableField("`type`")
    private String type;

    /**
     * Custom field remark
     */
    @TableField("remark")
    private String remark;

    /**
     * Test resource pool status
     */
    @TableField("`options`")
    private String options;

    /**
     * Is system custom field
     */
    @TableField("`system`")
    private Boolean system;

    /**
     * Is global custom field
     */
    @TableField("`global`")
    private Boolean global;

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

    @TableField("project_id")
    private String projectId;

    @TableField("third_part")
    private Boolean thirdPart;

    @TableField("create_user")
    private String createUser;
}
