package cn.master.backend.service;

import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TestCaseTemplate;
import cn.master.backend.request.BaseQueryRequest;
import cn.master.backend.request.UpdateCaseFieldTemplateRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
public interface TestCaseTemplateService extends IService<TestCaseTemplate> {

    /**
     * desc: add test case template
     *
     * @param request parameters
     * @return java.lang.String
     */
    String addTemplate(UpdateCaseFieldTemplateRequest request);

    /**
     * desc: update test case template
     * <p>
     * desc: 列表查询
     *
     * @param request     查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.TestCaseTemplate>
     */
    IPage<TestCaseTemplate> getPageList(BaseQueryRequest request, Page<TestCaseTemplate> producePage);

    /**
     * desc: delete test case template
     *
     * @param id template id
     * @return java.lang.String
     */
    String deleteTemplate(String id);

    /**
     * desc: 验证模板是否已使用
     *
     * @param id            模板id
     * @param getProjectFuc function
     */
    void checkTemplateUsed(String id, Function<String, List<SysProject>> getProjectFuc);

    TestCaseTemplate getTemplate(String projectId);

    /**
     * desc: 获取项目相关的测试用例模板
     *
     * @param projectId 项目id
     * @return java.util.List<cn.master.backend.entity.TestCaseTemplate>
     */
    List<TestCaseTemplate> getTemplateList(String projectId);
}
