package cn.master.backend.service;

import cn.master.backend.entity.TestCaseReviewTestCase;
import cn.master.backend.request.QueryCaseReviewRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-02
 */
public interface TestCaseReviewTestCaseService extends IService<TestCaseReviewTestCase> {
    /**
     * desc: 分页查询
     *
     * @param request 查询参数
     * @param page    分页参数
     * @return java.util.List<cn.master.backend.entity.TestCaseReviewTestCase>
     */
    IPage<TestCaseReviewTestCase> getTestCaseReviewCase(QueryCaseReviewRequest request, IPage<TestCaseReviewTestCase> page);
}
