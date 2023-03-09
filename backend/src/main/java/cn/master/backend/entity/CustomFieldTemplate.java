package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 字段模板关联表
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@Getter
@Setter
@TableName("custom_field_template")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomFieldTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Custom field  template related id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Custom field ID
     */
    @TableField("field_id")
    private String fieldId;

    /**
     * Field template ID
     */
    @TableField("template_id")
    private String templateId;

    /**
     * Use scene
     */
    @TableField("scene")
    private String scene;

    /**
     * Is required
     */
    @TableField("required")
    private Boolean required;

    /**
     * Item order
     */
    @TableField("order")
    private Integer order;

    /**
     * Default value
     */
    @TableField("default_value")
    private String defaultValue;

    /**
     * Custom data
     */
    @TableField("custom_data")
    private String customData;

    @TableField("key")
    private String key;
}
