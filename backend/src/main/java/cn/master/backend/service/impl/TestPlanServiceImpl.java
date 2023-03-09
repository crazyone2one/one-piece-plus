package cn.master.backend.service.impl;

import cn.master.backend.constants.TestPlanStatus;
import cn.master.backend.constants.TestPlanTestCaseStatus;
import cn.master.backend.entity.*;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.mapper.TestCaseMapper;
import cn.master.backend.mapper.TestPlanMapper;
import cn.master.backend.mapper.TestPlanTestCaseMapper;
import cn.master.backend.request.AddTestPlanRequest;
import cn.master.backend.request.PlanCaseRelevanceRequest;
import cn.master.backend.request.QueryTestPlanRequest;
import cn.master.backend.service.CustomFieldService;
import cn.master.backend.service.TestPlanFollowService;
import cn.master.backend.service.TestPlanPrincipalService;
import cn.master.backend.service.TestPlanService;
import cn.master.backend.util.JsonUtils;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import cn.master.backend.util.CommonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan> implements TestPlanService {
    final TestPlanFollowService testPlanFollowService;
    final TestPlanPrincipalService testPlanPrincipalService;
    final CustomFieldService customFieldService;
    final SysUserMapper sysUserMapper;
    final TestPlanTestCaseMapper testPlanTestCaseMapper;
    final TestCaseMapper testCaseMapper;

    @Override
    public List<TestPlanDtoWithMetric> findList(QueryTestPlanRequest request, IPage<TestPlan> page) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(request.getName()), TestPlan::getName, request.getName());
        wrapper.eq(StringUtils.isNoneBlank(request.getWorkspaceId()), TestPlan::getWorkspaceId, request.getWorkspaceId());
        wrapper.eq(StringUtils.isNoneBlank(request.getProjectId()), TestPlan::getProjectId, request.getProjectId());
        wrapper.eq(StringUtils.isNoneBlank(request.getId()), TestPlan::getId, request.getId());
        List<TestPlanDtoWithMetric> testPlans = baseMapper.selectPageVo(page, wrapper);
        Set<String> ids = testPlans.stream().map(TestPlan::getId).collect(Collectors.toSet());
        Map<String, ParamsDTO> planTestCaseCountMap = baseMapper.testPlanTestCaseCount(ids);

        testPlans.forEach(item -> {
            if (!Objects.isNull(planTestCaseCountMap.get(item.getId()))){
                String value = planTestCaseCountMap.get(item.getId()).getValue();
                int parseInt = Integer.parseInt(Objects.isNull(value) ? "0" : value);
                item.setTestPlanTestCaseCount(parseInt);
            }
            // 责任人
            List<SysUser> planPrincipal = getPlanPrincipal(item.getId());
            StringBuilder principal = new StringBuilder();
            List<String> principals = new ArrayList<>();
            planPrincipal.forEach(user -> {
                principals.add(user.getId());
                if (StringUtils.isNotBlank(principal)) {
                    principal.append("、").append(user.getName());
                } else {
                    principal.append(user.getName());
                }
            });
            item.setPrincipal(principal.toString());
            item.setPrincipals(principals);
        });
        calcTestPlanRate(testPlans);
        return testPlans;
    }

    private void calcTestPlanRate(List<TestPlanDtoWithMetric> testPlans) {
        testPlans.forEach(this::calcTestPlanRate);
    }

    private void calcTestPlanRate(TestPlanDtoWithMetric testPlan) {
        testPlan.setTested(0);
        testPlan.setPassed(0);
        testPlan.setTotal(0);
        List<CountMapDTO> statusCountMap = testPlanTestCaseMapper.getExecResultMapByPlanId(testPlan.getId());
        Integer functionalExecTotal = 0;

        for (CountMapDTO item : statusCountMap) {
            functionalExecTotal += item.getValue();
            if (!StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Prepare.name())
                    && !StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Underway.name())) {
                testPlan.setTested(testPlan.getTested() + item.getValue());
                if (StringUtils.equals(item.getKey(), TestPlanTestCaseStatus.Pass.name())) {
                    testPlan.setPassed(testPlan.getPassed() + item.getValue());
                }
            }
        }
        testPlan.setTotal(testPlan.getTotal() + functionalExecTotal);
        testPlan.setPassRate(CommonUtils.getPercentWithDecimal(testPlan.getTested() == 0 ? 0 : testPlan.getPassed() * 1.0 / testPlan.getTotal()));
        testPlan.setTestRate(CommonUtils.getPercentWithDecimal(testPlan.getTotal() == 0 ? 0 : testPlan.getTested() * 1.0 / testPlan.getTotal()));
    }

    @Override
    public TestPlan addTestPlan(AddTestPlanRequest testPlan) {
        if (CollectionUtils.isNotEmpty(getTestPlanByName(testPlan.getName()))) {
            throw new CustomException("测试计划名称已存在");
        }
        testPlan.setStatus(TestPlanStatus.Prepare.name());
        testPlan.setCreator(SessionUtils.getUserId());
        if (StringUtils.isBlank(testPlan.getProjectId())) {
            testPlan.setProjectId(SessionUtils.getCurrentProjectId());
        }
        baseMapper.insert(testPlan);
        List<String> follows = testPlan.getFollows();
        if (CollectionUtils.isNotEmpty(follows)) {
            for (String follow : follows) {
                TestPlanFollow testPlanFollow = TestPlanFollow.builder().testPlanId(testPlan.getId()).followId(follow).build();
                testPlanFollowService.addPlanFollow(testPlanFollow);
            }
        }
        List<String> principals = testPlan.getPrincipals();
        if (CollectionUtils.isNotEmpty(principals)) {
            for (String principal : principals) {
                TestPlanPrincipal testPlanPrincipal = TestPlanPrincipal.builder().testPlanId(testPlan.getId()).principalId(principal).build();
                testPlanPrincipalService.addPlanPrincipal(testPlanPrincipal);
            }
        }
        return testPlan;
    }

    @Override
    public TestPlan editTestPlan(AddTestPlanRequest request) {
        List<String> principals = request.getPrincipals();
        if (CollectionUtils.isNotEmpty(principals)) {
            if (StringUtils.isNotBlank(request.getId())) {
                List<String> planIds = new ArrayList<>();
                planIds.add(request.getId());
                // 先删除原有的关系，再重新添加
                testPlanPrincipalService.deleteTestPlanPrincipalByPlanId(planIds);
                for (String principal : principals) {
                    TestPlanPrincipal testPlanPrincipal = TestPlanPrincipal.builder().testPlanId(request.getId()).principalId(principal).build();
                    testPlanPrincipalService.addPlanPrincipal(testPlanPrincipal);
                }
            }
        }
        checkTestPlanExist(request);
        TestPlan res = baseMapper.selectById(request.getId());
        if (!res.getStatus().equals(request.getStatus())) {
            if (TestPlanStatus.Underway.name().equals(request.getStatus())) {
                // 未开始->进行中，写入实际开始时间
                // 已完成->进行中，结束时间置空
                if (res.getStatus().equals(TestPlanStatus.Prepare.name())) {
                    request.setActualStartTime(LocalDateTime.now());
                } else if (res.getStatus().equals(TestPlanStatus.Completed.name())) {
                    request.setActualEndTime(null);
                }
            } else if (!res.getStatus().equals(TestPlanStatus.Prepare.name()) &&
                    TestPlanStatus.Prepare.name().equals(request.getStatus())) {
                // 非未开始->未开始，时间都置空
                request.setActualStartTime(null);
                request.setActualEndTime(null);
            } else if (TestPlanStatus.Completed.name().equals(request.getStatus())) {
                // 已完成，写入实际完成时间
                request.setActualEndTime(LocalDateTime.now());
            } else if (!res.getStatus().equals(TestPlanStatus.Finished.name()) &&
                    TestPlanStatus.Finished.name().equals(request.getStatus())) {
                //   非已结束->已结束，更新结束时间
                request.setActualEndTime(LocalDateTime.now());
            }
        }
        // 如果状态是未开始，设置时间为null
        if (StringUtils.isNotBlank(request.getStatus()) && request.getStatus().equals(TestPlanStatus.Prepare.name())) {
            request.setActualStartTime(null);
            request.setActualEndTime(null);
        }
        // 如果当前状态已完成，没有结束时间，设置结束时间
        if (StringUtils.equalsAnyIgnoreCase(request.getStatus(), TestPlanStatus.Finished.name(), TestPlanStatus.Completed.name())
                && res.getActualEndTime() == null) {
            request.setActualEndTime(LocalDateTime.now());
        }

        // 如果当前状态不是已完成，设置结束时间为null
        if (!StringUtils.equalsAnyIgnoreCase(request.getStatus(), TestPlanStatus.Finished.name(), TestPlanStatus.Completed.name())
                && res.getActualEndTime() != null) {
            request.setActualEndTime(null);
        }

        // 如果当前状态不是未开始，并且没有开始时间，设置开始时间
        if (!StringUtils.equals(request.getStatus(), TestPlanStatus.Prepare.name())
                && res.getActualStartTime() == null) {
            request.setActualStartTime(LocalDateTime.now());
        }
        baseMapper.updateById(request);
        return baseMapper.selectById(request.getReportId());
    }

    @Override
    public int deleteTestPlan(List<String> planIds) {
        if (CollectionUtils.isEmpty(planIds)) {
            return 0;
        }
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TestPlan::getId, planIds);
        testPlanFollowService.deletePlanFollowByPlanIds(planIds);
        testPlanPrincipalService.deleteTestPlanPrincipalByPlanId(planIds);
        LambdaQueryWrapper<TestPlanTestCase> wrapper1 = new LambdaQueryWrapper<TestPlanTestCase>().in(TestPlanTestCase::getPlanId, planIds);
        testPlanTestCaseMapper.delByPlanId(wrapper1);
        return baseMapper.delete(wrapper);
    }

    @Override
    public List<Map<String, String>> getStageOption(String projectId) {
        CustomField customField = customFieldService.getCustomFieldByName(projectId, "测试阶段");
        return JsonUtils.string2Obj(customField.getOptions(), new TypeReference<>() {
        });
    }

    @Override
    public List<SysUser> getPlanPrincipal(String planId) {
        List<SysUser> result = new ArrayList<>();
        if (StringUtils.isBlank(planId)) {
            return result;
        }
        LambdaQueryWrapper<TestPlanPrincipal> wrapper = new LambdaQueryWrapper<TestPlanPrincipal>().eq(TestPlanPrincipal::getTestPlanId, planId);
        List<TestPlanPrincipal> testPlanPrincipals = testPlanPrincipalService.list(wrapper);
        List<String> userIds = testPlanPrincipals.stream().map(TestPlanPrincipal::getPrincipalId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIds);
        return sysUserMapper.getAll(userLambdaQueryWrapper);
    }

    @Override
    public List<SysUser> getPlanFollow(String planId) {
        List<SysUser> result = new ArrayList<>();
        if (StringUtils.isBlank(planId)) {
            return result;
        }
        LambdaQueryWrapper<TestPlanFollow> wrapper = new LambdaQueryWrapper<TestPlanFollow>().eq(TestPlanFollow::getTestPlanId, planId);
        List<TestPlanFollow> testPlanFollows = testPlanFollowService.list(wrapper);
        List<String> userIds = testPlanFollows.stream().map(TestPlanFollow::getFollowId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIds);
        return sysUserMapper.getAll(userLambdaQueryWrapper);
    }

    @Override
    public List<TestPlan> listTestAllPlan(QueryTestPlanRequest request) {
        String projectId = request.getProjectId();
        if (StringUtils.isBlank(projectId)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<TestPlan>().eq(TestPlan::getProjectId, projectId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean isAllowedRepeatCase(String planId) {
        return baseMapper.selectById(planId).getRepeatCase();
    }

    @Override
    public String testPlanRelevance(PlanCaseRelevanceRequest request) {
        LinkedHashMap<String, String> userMap;
        TestPlan testPlan = baseMapper.selectById(request.getPlanId());
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<TestCase>().in(TestCase::getId, request.getIds());
        List<TestCase> testCaseList = testCaseMapper.selectList(wrapper);
        userMap = testCaseList.stream()
                .collect(LinkedHashMap::new, (m, v) -> m.put(v.getId(), v.getMaintainer()), LinkedHashMap::putAll);
        Set<String> projectMemberSet = sysUserMapper.getProjectMemberOption(testPlan.getProjectId()).stream()
                .map(SysUser::getId)
                .collect(Collectors.toSet());
        List<String> testCaseIds = new ArrayList<>(userMap.keySet());
        if (CollectionUtils.isNotEmpty(testCaseIds)) {
            Collections.reverse(testCaseIds);
            long nextOrder = ServiceUtils.getNextOrder(request.getPlanId(), testPlanTestCaseMapper::getLastOrder);
            for (String caseId : testCaseIds) {
                String maintainer = userMap.get(caseId);
                if (StringUtils.isBlank(maintainer) || !projectMemberSet.contains(maintainer)) {
                    maintainer = SessionUtils.getUserId();
                }
                TestPlanTestCase planTestCase = TestPlanTestCase.builder().createUser(SessionUtils.getUserId()).executor(maintainer).caseId(caseId)
                        .planId(request.getPlanId()).status(TestPlanStatus.Prepare.name()).isDel(false).order(nextOrder)
                        .build();
                nextOrder += ServiceUtils.ORDER_STEP;
                testPlanTestCaseMapper.insert(planTestCase);
            }
//            caseTestRelevance(request, testCaseIds);
            resetStatus(testPlan.getId());
        }
        return "over";
    }

    @Override
    public void resetStatus(String planId) {
        TestPlan testPlan = baseMapper.selectById(planId);
        if (StringUtils.equals(testPlan.getStatus(), TestPlanStatus.Prepare.name())
                || StringUtils.equals(testPlan.getStatus(), TestPlanStatus.Completed.name())) {
            testPlan.setStatus(TestPlanStatus.Underway.name());
            // 将状态更新为进行中时，开始时间也要更新
            testPlan.setActualStartTime(LocalDateTime.now());
            testPlan.setActualEndTime(null);
            baseMapper.updateById(testPlan);
        }
    }

    private List<TestPlan> getTestPlanByName(String name) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getName, name).eq(TestPlan::getProjectId, SessionUtils.getCurrentProjectId());
        return baseMapper.selectList(wrapper);
    }

    private void checkTestPlanExist(TestPlan testPlan) {
        LambdaQueryWrapper<TestPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestPlan::getName, testPlan.getName()).eq(TestPlan::getProjectId, testPlan.getProjectId());
        wrapper.ne(TestPlan::getId, testPlan.getId());
        if (baseMapper.exists(wrapper)) {
            throw new CustomException("测试计划名称已存在");
        }
    }
}
