package cn.master.backend.service;

import cn.master.backend.entity.TestCaseReview;
import cn.master.backend.request.QueryCaseReviewRequest;
import cn.master.backend.request.ReviewRelevanceRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-01
 */
public interface TestCaseReviewService extends IService<TestCaseReview> {

    /**
     * desc: 查询列表
     *
     * @param page    分页条件
     * @param request 查询条件
     * @return java.util.List<cn.master.backend.entity.TestCaseReview>
     */
    IPage<TestCaseReview> getTestCaseReviewsList(IPage<TestCaseReview> page, QueryCaseReviewRequest request);

    /**
     * desc: 保存评审记录
     *
     * @param testCaseReview 参数
     * @return cn.master.backend.entity.TestCaseReview
     */
    TestCaseReview saveTestCaseReview(TestCaseReview testCaseReview);

    /**
     * desc: 修改评审记录
     *
     * @param testCaseReview 参数
     * @return cn.master.backend.entity.TestCaseReview
     */
    TestCaseReview editCaseReview(TestCaseReview testCaseReview);

    /**
     * desc: 删除评审记录
     *
     * @param reviewId review id
     * @return s
     */
    int deleteCaseReview(String reviewId);

    /**
     * desc: 查询项目下所有有效的任务数据
     *
     * @return java.util.List<cn.master.backend.entity.TestCaseReview>
     */
    List<TestCaseReview> listCaseReviewAll();

    /**
     * desc: 测试评审数据关联测试用例
     *
     * @param request 参数
     * @return java.lang.Integer
     */
    String testReviewRelevance(ReviewRelevanceRequest request);

    Long getLastOrder(String reviewId, Long baseOrder);
}
