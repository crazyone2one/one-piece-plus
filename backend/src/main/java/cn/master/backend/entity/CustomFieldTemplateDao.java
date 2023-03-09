package cn.master.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author create by 11's papa on 2023/1/10-8:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomFieldTemplateDao extends CustomFieldTemplate{
    private String name;

    private String scene;

    private String type;

    private String remark;

    private String options;

    private Boolean system;
}
