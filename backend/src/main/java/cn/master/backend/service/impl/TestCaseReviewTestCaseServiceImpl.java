package cn.master.backend.service.impl;

import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseReviewTestCase;
import cn.master.backend.mapper.TestCaseMapper;
import cn.master.backend.mapper.TestCaseReviewTestCaseMapper;
import cn.master.backend.request.QueryCaseReviewRequest;
import cn.master.backend.service.TestCaseReviewTestCaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-02
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestCaseReviewTestCaseServiceImpl extends ServiceImpl<TestCaseReviewTestCaseMapper, TestCaseReviewTestCase> implements TestCaseReviewTestCaseService {
    final TestCaseMapper testCaseMapper;

    @Override
    public IPage<TestCaseReviewTestCase> getTestCaseReviewCase(QueryCaseReviewRequest request, IPage<TestCaseReviewTestCase> page) {
        // 测试用例数据查询
        LambdaQueryWrapper<TestCase> testCaseWrapper = new LambdaQueryWrapper<>();
        testCaseWrapper.in(CollectionUtils.isNotEmpty(request.getNodeIds()), TestCase::getNodeId, request.getNodeIds());
        testCaseWrapper.eq(StringUtils.isNoneBlank(request.getId()), TestCase::getId, request.getId());
        testCaseWrapper.like(StringUtils.isNoneBlank(request.getName()), TestCase::getName, request.getName());
        testCaseWrapper.eq(StringUtils.isNoneBlank(request.getPriority()), TestCase::getPriority, request.getPriority());
        testCaseWrapper.ne(TestCase::getStatus, "Trash");
        List<TestCase> testCases = testCaseMapper.selectPageVO(null, testCaseWrapper);
        List<String> caseIds = testCases.stream().map(TestCase::getId).collect(Collectors.toList());
        IPage<TestCaseReviewTestCase> testCaseReviewTestCases = null;
        if (CollectionUtils.isNotEmpty(caseIds)) {
            // 查询关联数据
            LambdaQueryWrapper<TestCaseReviewTestCase> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNoneBlank(request.getReviewId()), TestCaseReviewTestCase::getReviewId, request.getReviewId());
            wrapper.eq(StringUtils.isNoneBlank(request.getReviewer()), TestCaseReviewTestCase::getReviewer, request.getReviewer());
            wrapper.eq(StringUtils.isNoneBlank(request.getStatus()), TestCaseReviewTestCase::getStatus, request.getStatus());
            wrapper.in(TestCaseReviewTestCase::getCaseId, caseIds);
            wrapper.eq(TestCaseReviewTestCase::getIsDel, false).orderByDesc(TestCaseReviewTestCase::getUpdateTime);
            testCaseReviewTestCases = baseMapper.selectPageVo(page, wrapper);
            testCaseReviewTestCases.getRecords().forEach(item -> {
                List<TestCase> collect = testCases.stream().filter(t -> Objects.equals(t.getId(), item.getCaseId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    TestCase testCase = collect.get(0);
                    item.setTestCase(testCase);
                } else {
                    item.setTestCase(new TestCase());
                }
            });
        }
        return testCaseReviewTestCases;
    }
}
