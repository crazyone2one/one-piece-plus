package cn.master.backend.service.impl;

import cn.master.backend.constants.TemplateConstants;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TestCaseTemplate;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.TestCaseTemplateMapper;
import cn.master.backend.request.BaseQueryRequest;
import cn.master.backend.request.UpdateCaseFieldTemplateRequest;
import cn.master.backend.service.CustomFieldTemplateService;
import cn.master.backend.service.SysProjectService;
import cn.master.backend.service.TestCaseTemplateService;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestCaseTemplateServiceImpl extends ServiceImpl<TestCaseTemplateMapper, TestCaseTemplate> implements TestCaseTemplateService {
    final CustomFieldTemplateService customFieldTemplateService;
    final SysProjectService sysProjectService;

    @Override
    public String addTemplate(UpdateCaseFieldTemplateRequest request) {
        checkExist(request);
        request.setGlobal(false);
        request.setCreateUser(SessionUtils.getUserId());
        if (Objects.isNull(request.getSystem())) {
            request.setSystem(false);
        }
        baseMapper.insert(request);
        customFieldTemplateService.create(request.getCustomFields(), request.getId(), TemplateConstants.FieldTemplateScene.TEST_CASE.name());
        return request.getId();
    }

    @Override
    public IPage<TestCaseTemplate> getPageList(BaseQueryRequest request, Page<TestCaseTemplate> producePage) {
        request.setOrders(ServiceUtils.getDefaultOrder(request.getOrders()));
        return baseMapper.queryPageList(producePage, request);
    }

    @Override
    public String deleteTemplate(String id) {
        checkTemplateUsed(id, sysProjectService::getByCaseTemplateId);
        baseMapper.deleteById(id);
        customFieldTemplateService.deleteByTemplateId(id);
        return "delete successfully:" + id;
    }

    @Override
    public void checkTemplateUsed(String id, Function<String, List<SysProject>> getProjectFuc) {
        List<SysProject> projects = getProjectFuc.apply(id);
        if (!CollectionUtils.isEmpty(projects)) {
            StringBuilder tip = new StringBuilder();
            projects.forEach(item -> {
                tip.append(item.getName()).append(',');
            });
            tip.deleteCharAt(tip.length() - 1);
            throw new CustomException("该模板已关联项目:" + tip);
        }
    }

    @Override
    public TestCaseTemplate getTemplate(String projectId) {
        SysProject project = sysProjectService.getById(projectId);
        String caseTemplateId = project.getCaseTemplateId();
        TestCaseTemplate caseTemplate;
        if (StringUtils.isNotBlank(caseTemplateId)) {
            caseTemplate = baseMapper.selectById(caseTemplateId);
            if (Objects.isNull(caseTemplate)) {
                caseTemplate = getDefaultTemplate(projectId);
            }
        }else {
            caseTemplate = getDefaultTemplate(projectId);
        }
//        List<CustomFieldDao> result = customFieldTemplateService.getCustomFieldByTemplateId(caseTemplate.getId());
//        caseTemplate.setCustomFields(result);
        return caseTemplate;
    }

    @Override
    public List<TestCaseTemplate> getTemplateList(String projectId) {
        LambdaQueryWrapper<TestCaseTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNoneBlank(projectId),TestCaseTemplate::getProjectId, projectId).eq(TestCaseTemplate::getSystem, true);
        List<TestCaseTemplate> testCaseTemplates = baseMapper.selectList(wrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(testCaseTemplates)) {
            return testCaseTemplates;
        }else {
            wrapper.clear();
            wrapper.eq(TestCaseTemplate::getGlobal, true);
            return baseMapper.selectList(wrapper);
        }
    }

    private TestCaseTemplate getDefaultTemplate(String projectId) {
        LambdaQueryWrapper<TestCaseTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCaseTemplate::getProjectId, projectId).eq(TestCaseTemplate::getSystem, true);
        List<TestCaseTemplate> testCaseTemplates = baseMapper.selectList(wrapper);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(testCaseTemplates)) {
            return testCaseTemplates.get(0);
        }else {
            wrapper.clear();
            wrapper.eq(TestCaseTemplate::getGlobal, true);
            return baseMapper.selectList(wrapper).get(0);
        }
    }

    private void checkExist(UpdateCaseFieldTemplateRequest testCaseTemplate) {
        if (Objects.nonNull(testCaseTemplate.getName())) {
            LambdaQueryWrapper<TestCaseTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseTemplate::getName, testCaseTemplate.getName());
            wrapper.eq(StringUtils.isNoneBlank(testCaseTemplate.getProjectId()), TestCaseTemplate::getProjectId, testCaseTemplate.getProjectId());
            wrapper.eq(StringUtils.isNoneBlank(testCaseTemplate.getId()), TestCaseTemplate::getId, testCaseTemplate.getId());
            if (baseMapper.exists(wrapper)) {
                throw new CustomException("工作空间下已存在该模板:" + testCaseTemplate.getName());
            }
        }
    }
}
