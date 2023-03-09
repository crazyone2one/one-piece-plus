package cn.master.backend.service.impl;

import cn.master.backend.entity.CustomField;
import cn.master.backend.entity.CustomFieldDao;
import cn.master.backend.entity.CustomFieldTemplate;
import cn.master.backend.entity.CustomFieldTemplateDao;
import cn.master.backend.mapper.CustomFieldMapper;
import cn.master.backend.mapper.CustomFieldTemplateMapper;
import cn.master.backend.service.CustomFieldTemplateService;
import cn.master.backend.util.ServiceUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 字段模板关联表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CustomFieldTemplateServiceImpl extends ServiceImpl<CustomFieldTemplateMapper, CustomFieldTemplate> implements CustomFieldTemplateService {
    final CustomFieldTemplateMapper customFieldTemplateMapper;
    final CustomFieldMapper customFieldMapper;

    @Override
    public void create(List<CustomFieldTemplate> customFields, String templateId, String scene) {
        if (CollectionUtils.isNotEmpty(customFields)) {
            AtomicLong nextOrder = new AtomicLong(ServiceUtils.getNextOrder(templateId, baseMapper::getLastOrder));
            customFields.forEach(customField -> {
                CustomFieldTemplate customFieldTemplate = CustomFieldTemplate.builder().templateId(templateId).scene(scene).build();
                if (Objects.isNull(customField.getRequired())) {
                    customFieldTemplate.setRequired(false);
                }
                nextOrder.addAndGet(ServiceUtils.ORDER_STEP);
                customFieldTemplate.setOrder((int) nextOrder.longValue());
                baseMapper.insert(customFieldTemplate);
            });
        }
    }

    @Override
    public void deleteByTemplateId(String id) {
        if (StringUtils.isNotBlank(id)) {
            LambdaQueryWrapper<CustomFieldTemplate> wrapper = new LambdaQueryWrapper<CustomFieldTemplate>().eq(CustomFieldTemplate::getTemplateId, id);
            baseMapper.delete(wrapper);
        }
    }

    @Override
    public void deleteByFieldId(String fieldId) {
        if (StringUtils.isNotBlank(fieldId)) {
            LambdaQueryWrapper<CustomFieldTemplate> wrapper = new LambdaQueryWrapper<CustomFieldTemplate>().eq(CustomFieldTemplate::getFieldId, fieldId);
            baseMapper.delete(wrapper);
        }
    }

    @Override
    public List<CustomFieldTemplateDao> listTemplate(CustomFieldTemplate request) {
        return baseMapper.queryCustomFieldTemplateDaoByCustom(request);
    }

    @Override
    public CustomField getCustomField(String id) {
        CustomFieldTemplate customFieldTemplate = customFieldTemplateMapper.selectById(id);
        return customFieldMapper.selectById(customFieldTemplate.getFieldId());
    }

    @Override
    public List<CustomFieldDao> getCustomFieldByTemplateId(String templateId) {
        return baseMapper.getCustomFieldByTemplateId(templateId);
    }
}
