package cn.master.backend.service.impl;

import cn.master.backend.constants.CommonConstants;
import cn.master.backend.constants.TestCaseReviewStatus;
import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseDTO;
import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.mapper.TestCaseMapper;
import cn.master.backend.mapper.TestCaseNodeMapper;
import cn.master.backend.mapper.TestCaseReviewTestCaseMapper;
import cn.master.backend.request.EditTestCaseRequest;
import cn.master.backend.request.OrderRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.service.TestCaseService;
import cn.master.backend.service.TestPlanService;
import cn.master.backend.util.DateUtils;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-17
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements TestCaseService {
    final TestCaseNodeMapper testCaseNodeMapper;
    final SysUserMapper sysUserMapper;
    final TestPlanService testPlanService;
    final TestCaseReviewTestCaseMapper testCaseReviewTestCaseMapper;

    @Override
    public TestCase addTestCase(EditTestCaseRequest request) {
        checkTestCaseExist(request);
//        checkTestCustomNum(request);
        request.setNum(getNextNum(request.getProjectId()));
        if (StringUtils.isBlank(request.getCustomNum())) {
            request.setCustomNum(request.getNum().toString());
        }
        request.setReviewStatus(TestCaseReviewStatus.Prepare.name());
        request.setCreateUser(SessionUtils.getUserId());
        setNode(request);
        request.setOrder(ServiceUtils.getNextOrder(request.getProjectId(), baseMapper::getLastOrder));
        //完全新增一条记录直接就是最新
        request.setLatest(true);
        baseMapper.insert(request);
        request.setRefId(request.getId());
        baseMapper.updateById(request);
        return request;
    }

    @Override
    public IPage<TestCase> listTestCase(QueryTestCaseRequest request, Page<TestCase> page) {
        return listTestCase(request, page, false);
    }

    @Override
    public IPage<TestCase> listTestCase(QueryTestCaseRequest request, Page<TestCase> page, boolean isSampleInfo) {
        initRequest(request, true);
        setDefaultOrder(request);
        if (request.getFilters() != null && !request.getFilters().containsKey("status")) {
            request.getFilters().put("status", new ArrayList<>(0));
        }
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(request.getName())) {
            wrapper.and(i -> i.like(TestCase::getName, request.getName())).or(i -> i.like(TestCase::getTags, request.getName()));
        }
        wrapper.eq(StringUtils.isNoneBlank(request.getNodeId()), TestCase::getNodeId, request.getNodeId());
        wrapper.ne(StringUtils.isNotBlank(request.getStatusIsNot()), TestCase::getStatus, request.getStatusIsNot());
        wrapper.ne(StringUtils.isNotBlank(request.getNotEqStatus()), TestCase::getStatus, request.getStatusIsNot());
        wrapper.in((CollectionUtils.isNotEmpty(request.getIds())), TestCase::getId, request.getIds());
        wrapper.notIn((CollectionUtils.isNotEmpty(request.getNotInIds())), TestCase::getId, request.getNotInIds());
        wrapper.in((CollectionUtils.isNotEmpty(request.getNodeIds())), TestCase::getNodeId, request.getNodeIds());
        wrapper.eq(StringUtils.isNoneBlank(request.getProjectId()), TestCase::getProjectId, request.getProjectId());
        wrapper.ne(TestCase::getStatus, "Trash");
        wrapper.orderByDesc(TestCase::getOrder);
        Page<TestCase> testCasePage = baseMapper.selectPage(page, wrapper);
        for (TestCase record : testCasePage.getRecords()) {
            record.setMaintainerName(sysUserMapper.selectById(record.getMaintainer()).getName());
        }
        return testCasePage;
    }

    @Override
    public TestCase getTestCase(String testCaseId) {
        return baseMapper.selectById(testCaseId);
    }

    @Override
    public TestCase editTestCase(EditTestCaseRequest request) {
        TestCase testCase = baseMapper.selectById(request.getId());
        request.setNum(testCase.getNum());
        setNode(request);
        if (StringUtils.isEmpty(testCase.getDemandId())) {
            testCase.setDemandId(StringUtils.EMPTY);
        }
        baseMapper.updateById(request);
        return baseMapper.selectById(request.getId());
    }

    @Override
    public List<TestCase> getTestCaseVersions(String caseId) {
        TestCase testCase = baseMapper.selectById(caseId);
        if (Objects.isNull(testCase)) {
            return new ArrayList<>();
        }
        QueryTestCaseRequest request = new QueryTestCaseRequest();
        request.setRefId(testCase.getRefId());
        if (CommonConstants.TRASH_STATUS.equalsIgnoreCase(testCase.getStatus())) {
            request.setFilters(new HashMap<>(1) {{
                put("status", new ArrayList() {{
                    add(CommonConstants.TRASH_STATUS);
                }});
            }});
        }
        return listTestCase(request, null).getRecords();
    }

    @Override
    public int deleteTestCaseToGc(String testCaseId) {
        TestCase testCase = baseMapper.selectById(testCaseId);
        testCase.setDeleteTime(LocalDateTime.now());
        testCase.setDeleteUserId(SessionUtils.getUserId());
        testCase.setOriginalStatus(testCase.getStatus());
        testCase.setStatus("Trash");
        // TODO 删除其他信息
        return baseMapper.updateById(testCase);
    }

    @Override
    public IPage<TestCaseDTO> getTestCaseRelateList(QueryTestCaseRequest request, long page, long limit) {
        setDefaultOrder(request);
        request.getOrders().forEach(order -> {
            order.setPrefix("test_case");
        });
        if (testPlanService.isAllowedRepeatCase(request.getPlanId())) {
            request.setRepeatCase(true);
        }
        return baseMapper.getTestCaseByNotInPlan(new Page<>(page, limit), request);
    }

    @Override
    public IPage<TestCaseDTO> getTestReviewCase(QueryTestCaseRequest request, Page<TestCase> page) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        List<String> caseIds = testCaseReviewTestCaseMapper.getCaseIdByReviewId(request.getReviewId());
        wrapper.notIn(CollectionUtils.isNotEmpty(caseIds), TestCase::getId, caseIds);
        wrapper.eq(TestCase::getProjectId, request.getProjectId());
        wrapper.in(CollectionUtils.isNotEmpty(request.getNodeIds()), TestCase::getNodeId, request.getNodeIds());
        wrapper.orderByDesc(TestCase::getUpdateTime);
        return baseMapper.getTestCaseByNotInReview(page, wrapper);
    }

    private void setDefaultOrder(QueryTestCaseRequest request) {
        List<OrderRequest> orders = ServiceUtils.getDefaultSortOrder(request.getOrders());
        orders.forEach(i -> i.setPrefix("test_case"));
        request.setOrders(orders);
    }

    private void initRequest(QueryTestCaseRequest request, boolean checkThisWeekData) {
        if (checkThisWeekData) {
            Map<String, Date> weekFirstTimeAndLastTime = DateUtils.getWeedFirstTimeAndLastTime(new Date());
            Date weekFirstTime = weekFirstTimeAndLastTime.get("firstTime");
            if (request.isSelectThisWeedData()) {
                if (weekFirstTime != null) {
                    request.setCreateTime(weekFirstTime.getTime());
                }
            }
            if (request.isSelectThisWeedRelevanceData()) {
                if (weekFirstTime != null) {
                    request.setRelevanceCreateTime(weekFirstTime.getTime());
                }
            }
        }
    }

    private void setNode(TestCase testCase) {
        if (StringUtils.isEmpty(testCase.getNodeId()) || "default-module".equals(testCase.getNodeId())) {
            LambdaQueryWrapper<TestCaseNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseNode::getProjectId, testCase.getProjectId()).eq(TestCaseNode::getName, "未规划用例");
            List<TestCaseNode> nodes = testCaseNodeMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(nodes)) {
                testCase.setNodeId(nodes.get(0).getId());
                testCase.setNodePath("/" + nodes.get(0).getName());
            }
        }
    }

    private Integer getNextNum(String projectId) {
        TestCase testCase = baseMapper.getMaxNumByProjectId(projectId);
        if (Objects.isNull(testCase) || Objects.isNull(testCase.getNum())) {
            return 100001;
        } else {
            return Optional.of(testCase.getNum() + 1).orElse(100001);
        }
    }

    private void checkTestCustomNum(TestCase testCase) {
        if (StringUtils.isNotBlank(testCase.getCustomNum())) {
            String projectId = testCase.getProjectId();
        }
    }

    private TestCase checkTestCaseExist(TestCase testCase) {
        if (Objects.nonNull(testCase)) {
        /*
            例如对于“/模块5”，用户的输入可能为“模块5”或者“/模块5/”或者“模块5/”。
            不这样处理的话，下面进行判断时就会用用户输入的错误格式进行判断，而模块名为“/模块5”、
            “模块5”、“/模块5/”、“模块5/”时，它们应该被认为是同一个模块。
            数据库存储的node_path都是“/模块5”这种格式的
             */
            String nodePath = testCase.getNodePath();
            if (!nodePath.startsWith("/")) {
                nodePath = "/" + nodePath;
            }
            if (nodePath.endsWith("/")) {
                nodePath = nodePath.substring(0, nodePath.length() - 1);
            }
            LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCase::getName, testCase.getName())
                    .eq(TestCase::getProjectId, testCase.getProjectId())
                    .eq(TestCase::getNodePath, nodePath)
                    .eq(StringUtils.isNotBlank(testCase.getType()), TestCase::getType, testCase.getType())
                    .ne(TestCase::getStatus, "Trash");
            wrapper.eq(StringUtils.isNotBlank(testCase.getPriority()), TestCase::getPriority, testCase.getPriority());
            wrapper.eq(StringUtils.isNotBlank(testCase.getTestId()), TestCase::getTestId, testCase.getTestId());
            wrapper.ne(StringUtils.isNotBlank(testCase.getId()), TestCase::getId, testCase.getId());
            List<TestCase> caseList = baseMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(caseList)) {
                String caseRemark = testCase.getRemark() == null ? StringUtils.EMPTY : testCase.getRemark();
                String caseSteps = testCase.getSteps() == null ? StringUtils.EMPTY : testCase.getSteps();
                String casePrerequisite = testCase.getPrerequisite() == null ? StringUtils.EMPTY : testCase.getPrerequisite();
                for (TestCase tc : caseList) {
                    String steps = tc.getSteps() == null ? StringUtils.EMPTY : tc.getSteps();
                    String remark = tc.getRemark() == null ? StringUtils.EMPTY : tc.getRemark();
                    String prerequisite = tc.getPrerequisite() == null ? StringUtils.EMPTY : tc.getPrerequisite();
                    if (StringUtils.equals(steps, caseSteps) && StringUtils.equals(remark, caseRemark) && StringUtils.equals(prerequisite, casePrerequisite)) {
                        return tc;
                    }
                }
            }
        }
        return null;
    }
}
