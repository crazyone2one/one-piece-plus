package cn.master.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author create by 11's papa on 2023/1/10-9:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomFieldDao extends CustomField {
    private Boolean required;

    private Integer order;

    private String defaultValue;

    private String textValue;

    private String value;

    private String customData;

    private String originGlobalId;

    private String key;
}
