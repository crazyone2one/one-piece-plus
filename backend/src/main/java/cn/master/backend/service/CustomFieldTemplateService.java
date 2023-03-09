package cn.master.backend.service;

import cn.master.backend.entity.CustomField;
import cn.master.backend.entity.CustomFieldDao;
import cn.master.backend.entity.CustomFieldTemplate;
import cn.master.backend.entity.CustomFieldTemplateDao;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字段模板关联表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
public interface CustomFieldTemplateService extends IService<CustomFieldTemplate> {

    /**
     * desc: add
     *
     * @param customFields customFields
     * @param templateId   templateId
     * @param scene        scene
     */
    void create(List<CustomFieldTemplate> customFields, String templateId, String scene);

    /**
     * desc: delete by template id
     *
     * @param id template id
     */
    void deleteByTemplateId(String id);

    /**
     * desc: delete by field id
     *
     * @param fieldId field id
     */
    void deleteByFieldId(String fieldId);

    /**
     * desc: search templates
     *
     * @param request params
     * @return java.util.List<cn.master.backend.entity.CustomFieldTemplateDao>
     */

    List<CustomFieldTemplateDao> listTemplate(CustomFieldTemplate request);

    /**
     * desc:  search custom fields
     *
     * @param id id
     * @return cn.master.backend.entity.CustomField
     */
    CustomField getCustomField(String id);

    /**
     * desc: get template by id
     *
     * @param templateId template id
     * @return java.util.List<cn.master.backend.entity.CustomFieldDao>
     */
    List<CustomFieldDao> getCustomFieldByTemplateId(String templateId);
}
