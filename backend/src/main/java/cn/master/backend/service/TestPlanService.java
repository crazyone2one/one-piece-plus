package cn.master.backend.service;

import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.TestPlan;
import cn.master.backend.entity.TestPlanDtoWithMetric;
import cn.master.backend.request.AddTestPlanRequest;
import cn.master.backend.request.PlanCaseRelevanceRequest;
import cn.master.backend.request.QueryTestPlanRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
public interface TestPlanService extends IService<TestPlan> {

    /**
     * desc: 查询列表数据
     *
     * @param request 查询条件
     * @param page    分页条件
     * @return java.util.List<cn.master.backend.entity.TestPlanDtoWithMetric>
     */
    List<TestPlanDtoWithMetric> findList(QueryTestPlanRequest request, IPage<TestPlan> page);

    /**
     * desc: 添加测试计划数据
     *
     * @param testPlan 参数
     * @return cn.master.backend.entity.TestPlan
     */
    TestPlan addTestPlan(AddTestPlanRequest testPlan);

    /**
     * desc: 编辑测试计划
     *
     * @param request 参数
     * @return cn.master.backend.entity.TestPlan
     */
    TestPlan editTestPlan(AddTestPlanRequest request);

    /**
     * desc: 删除测试计划
     *
     * @param planIds a list of test plan id
     * @return int
     */
    int deleteTestPlan(List<String> planIds);

    /**
     * desc: 获取测试阶段options
     *
     * @param projectId project id
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     */
    List<Map<String, String>> getStageOption(String projectId);

    /**
     * desc: 查询计划的责任人
     *
     * @param planId plan id
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    List<SysUser> getPlanPrincipal(String planId);

    List<SysUser> getPlanFollow(String planId);

    List<TestPlan> listTestAllPlan(QueryTestPlanRequest request);

    boolean isAllowedRepeatCase(String planId);

    /**
     * desc: 保存关联关系
     *
     * @param request 参数
     * @return java.lang.String
     */
    String testPlanRelevance(PlanCaseRelevanceRequest request);

    void resetStatus(String planId);
}
