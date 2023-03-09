package cn.master.backend.request;

import cn.master.backend.entity.CustomFieldTemplate;
import cn.master.backend.entity.TestCaseTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/8-11:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCaseFieldTemplateRequest extends TestCaseTemplate {
    List<CustomFieldTemplate> customFields;
}
