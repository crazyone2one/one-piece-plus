package cn.master.backend.service.impl;

import cn.master.backend.constants.TestCaseReviewStatus;
import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.TestCaseReview;
import cn.master.backend.entity.TestCaseReviewTestCase;
import cn.master.backend.entity.TestCaseReviewUsers;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.mapper.TestCaseReviewMapper;
import cn.master.backend.mapper.TestCaseReviewTestCaseMapper;
import cn.master.backend.mapper.TestCaseReviewUsersMapper;
import cn.master.backend.request.QueryCaseReviewRequest;
import cn.master.backend.request.ReviewRelevanceRequest;
import cn.master.backend.service.TestCaseReviewService;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-01
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TestCaseReviewServiceImpl extends ServiceImpl<TestCaseReviewMapper, TestCaseReview> implements TestCaseReviewService {
    final TestCaseReviewUsersMapper testCaseReviewUsersMapper;
    final SysUserMapper sysUserMapper;
    final TestCaseReviewTestCaseMapper testCaseReviewTestCaseMapper;

    @Override
    public IPage<TestCaseReview> getTestCaseReviewsList(IPage<TestCaseReview> page, QueryCaseReviewRequest request) {
        LambdaQueryWrapper<TestCaseReview> wrapper = new LambdaQueryWrapper<>();
        // 构建查询参数
        wrapper.like(StringUtils.isNoneBlank(request.getName()), TestCaseReview::getName, request.getName());
        if (StringUtils.equalsIgnoreCase(request.getReviewerId(), "currentUserId")) {
            request.setReviewerId(SessionUtils.getUserId());
        }
        if (StringUtils.isNoneBlank(request.getReviewId())) {
            wrapper.inSql(TestCaseReview::getId, "select test_case_review_users.review_id from test_case_review_users where test_case_review_users.user_id ='" + request.getReviewId() + "'");
        }
        wrapper.eq(StringUtils.isNoneBlank(request.getStatus()), TestCaseReview::getStatus, request.getStatus());
        wrapper.eq(StringUtils.isNoneBlank(request.getProjectId()), TestCaseReview::getProjectId, request.getProjectId());
        wrapper.orderByDesc(TestCaseReview::getUpdateTime);
        IPage<TestCaseReview> iPage = baseMapper.selectPageVo(page, wrapper);
        buildReviewer(iPage.getRecords());
        return iPage;
    }

    private void buildReviewer(List<TestCaseReview> reviewList) {
        if (CollectionUtils.isNotEmpty(reviewList)) {
            reviewList.forEach(item -> {
                StringBuilder reviewer = new StringBuilder();
                List<String> userIds = new ArrayList<>();
                LambdaQueryWrapper<TestCaseReviewUsers> wrapper = new LambdaQueryWrapper<TestCaseReviewUsers>().eq(TestCaseReviewUsers::getReviewId, item.getId());
                List<TestCaseReviewUsers> testCaseReviewUsers = testCaseReviewUsersMapper.selectList(wrapper);
                List<String> userIdList = testCaseReviewUsers
                        .stream()
                        .map(TestCaseReviewUsers::getUserId)
                        .collect(Collectors.toList());
                LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.in(CollectionUtils.isNotEmpty(userIdList), SysUser::getId, userIdList);
                List<SysUser> sysUsers = sysUserMapper.selectList(userWrapper);
                if (CollectionUtils.isNotEmpty(sysUsers)) {
                    for (SysUser sysUser : sysUsers) {
                        if (StringUtils.isNotBlank(reviewer)) {
                            reviewer.append("、").append(sysUser.getName());
                        } else {
                            reviewer.append(sysUser.getName());
                        }
                        userIds.add(sysUser.getId());
                    }
                }
                item.setReviewer(reviewer.toString());
                item.setUserIds(userIds);
            });
        }
    }

    @Override
    public TestCaseReview saveTestCaseReview(TestCaseReview testCaseReview) {
        checkCaseReviewExist(testCaseReview);
        testCaseReview.setCreator(SessionUtils.getUserId());
        testCaseReview.setCreateUser(SessionUtils.getUserId());
        testCaseReview.setStatus(TestCaseReviewStatus.Prepare.name());
        if (StringUtils.isNoneBlank(testCaseReview.getProjectId())) {
            testCaseReview.setProjectId(SessionUtils.getCurrentProjectId());
        }
        baseMapper.insert(testCaseReview);
        // 执行人
        List<String> userIds = testCaseReview.getUserIds();
        userIds.forEach(userId -> {
            TestCaseReviewUsers build = TestCaseReviewUsers.builder().reviewId(testCaseReview.getId()).userId(userId).build();
            testCaseReviewUsersMapper.insert(build);
        });
        // 关注人
        List<String> followIds = testCaseReview.getFollowIds();
        return testCaseReview;
    }

    @Override
    public TestCaseReview editCaseReview(TestCaseReview testCaseReview) {
        editCaseReviewer(testCaseReview);
        checkCaseReviewExist(testCaseReview);
        baseMapper.updateById(testCaseReview);
        return testCaseReview;
    }

    @Override
    public int deleteCaseReview(String reviewId) {
        deleteCaseReviewUsers(reviewId);
        deleteCaseReviewTestCase(reviewId);
        return baseMapper.deleteById(reviewId);
    }

    @Override
    public List<TestCaseReview> listCaseReviewAll() {
        if (StringUtils.isNotBlank(SessionUtils.getCurrentProjectId())) {
            LambdaQueryWrapper<TestCaseReview> wrapper = new LambdaQueryWrapper<TestCaseReview>()
                    .eq(TestCaseReview::getProjectId, SessionUtils.getCurrentProjectId());
            return baseMapper.selectList(wrapper);
        }
        return new ArrayList<>();
    }

    @Override
    public String testReviewRelevance(ReviewRelevanceRequest request) {
        List<String> testCaseIds = request.getTestCaseIds();
        if (CollectionUtils.isNotEmpty(testCaseIds)) {
            Collections.reverse(testCaseIds);
            long nextOrder = ServiceUtils.getNextOrder(request.getReviewId(), getLastOrder(request.getReviewId(), null));
            for (String caseId : testCaseIds) {
                TestCaseReviewTestCase build = TestCaseReviewTestCase.builder().reviewer(SessionUtils.getUserId()).createUser(SessionUtils.getUserId())
                        .caseId(caseId).reviewId(request.getReviewId()).status(TestCaseReviewStatus.Prepare.name()).isDel(false)
                        .order(nextOrder).build();
                testCaseReviewTestCaseMapper.insert(build);
                nextOrder += ServiceUtils.ORDER_STEP;
            }
            TestCaseReview testCaseReview = baseMapper.selectById(request.getReviewId());
            if (StringUtils.equals(testCaseReview.getStatus(), TestCaseReviewStatus.Prepare.name())
                    || StringUtils.equals(testCaseReview.getStatus(), TestCaseReviewStatus.Completed.name())) {
                testCaseReview.setStatus(TestCaseReviewStatus.Underway.name());
                baseMapper.updateById(testCaseReview);
            }
        }
        return "completed";
    }

    @Override
    public Long getLastOrder(String reviewId, Long baseOrder) {
        LambdaQueryWrapper<TestCaseReviewTestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TestCaseReviewTestCase::getOrder);
        wrapper.eq(TestCaseReviewTestCase::getReviewId, reviewId);
        wrapper.gt(Objects.nonNull(baseOrder), TestCaseReviewTestCase::getOrder, baseOrder);
        wrapper.orderByDesc(TestCaseReviewTestCase::getOrder).last("limit 1");
        List<TestCaseReviewTestCase> cases = testCaseReviewTestCaseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(cases)) {
            return null;
        } else {
            return cases.get(0).getOrder();
        }
    }

    private void deleteCaseReviewTestCase(String reviewId) {
        LambdaQueryWrapper<TestCaseReviewTestCase> wrapper = new LambdaQueryWrapper<TestCaseReviewTestCase>()
                .eq(TestCaseReviewTestCase::getReviewId, reviewId);
        testCaseReviewTestCaseMapper.delete(wrapper);
    }

    private void deleteCaseReviewUsers(String reviewId) {
        LambdaQueryWrapper<TestCaseReviewUsers> wrapper = new LambdaQueryWrapper<TestCaseReviewUsers>().eq(TestCaseReviewUsers::getReviewId, reviewId);
        testCaseReviewUsersMapper.delete(wrapper);
    }

    private void editCaseReviewer(TestCaseReview testCaseReview) {
        List<String> reviewerIds = testCaseReview.getUserIds();
        String id = testCaseReview.getId();
        LambdaQueryWrapper<TestCaseReviewUsers> wrapper = new LambdaQueryWrapper<TestCaseReviewUsers>().eq(TestCaseReviewUsers::getReviewId, id);
        List<TestCaseReviewUsers> testCaseReviewUsers = testCaseReviewUsersMapper.selectList(wrapper);
        List<String> dbReviewIds = testCaseReviewUsers.stream().map(TestCaseReviewUsers::getUserId).collect(Collectors.toList());
        reviewerIds.forEach(reviewerId -> {
            if (!dbReviewIds.contains(reviewerId)) {
                TestCaseReviewUsers build = TestCaseReviewUsers.builder().userId(reviewerId).reviewId(id).build();
                testCaseReviewUsersMapper.insert(build);
            }
        });
        wrapper.clear();
        wrapper.eq(TestCaseReviewUsers::getReviewId, id).notIn(TestCaseReviewUsers::getUserId, reviewerIds);
        testCaseReviewUsersMapper.delete(wrapper);
    }

    private void checkCaseReviewExist(TestCaseReview testCaseReview) {
        if (Objects.nonNull(testCaseReview.getName())) {
            LambdaQueryWrapper<TestCaseReview> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCaseReview::getName, testCaseReview.getName());
            wrapper.eq(TestCaseReview::getProjectId, testCaseReview.getProjectId());
            wrapper.ne(StringUtils.isNoneBlank(testCaseReview.getId()), TestCaseReview::getId, testCaseReview.getId());
            if (baseMapper.exists(wrapper)) {
                throw new CustomException("评审名称已存在");
            }
        }
    }
}
