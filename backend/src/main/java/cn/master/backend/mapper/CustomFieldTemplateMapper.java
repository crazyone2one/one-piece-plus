package cn.master.backend.mapper;

import cn.master.backend.entity.CustomFieldDao;
import cn.master.backend.entity.CustomFieldTemplate;
import cn.master.backend.entity.CustomFieldTemplateDao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字段模板关联表 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@Mapper
public interface CustomFieldTemplateMapper extends BaseMapper<CustomFieldTemplate> {
    /**
     * desc: 查询最新的order
     *
     * @param templateId templateId
     * @param baseOrder  baseOrder
     * @return java.lang.Long
     */
    Long getLastOrder(@Param("templateId") String templateId, @Param("baseOrder") Long baseOrder);

    /**
     * desc: search custom field templates
     *
     * @param request parameters
     * @return java.util.List<cn.master.backend.entity.CustomFieldTemplateDao>
     */
    List<CustomFieldTemplateDao> queryCustomFieldTemplateDaoByCustom(@Param("request") CustomFieldTemplate request);

    /**
     * desc: get template
     *
     * @param templateId template id
     * @return java.util.List<cn.master.backend.entity.CustomFieldDao>
     */
    List<CustomFieldDao> getCustomFieldByTemplateId(@Param("templateId") String templateId);
}
