package cn.master.backend.service;

import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseDTO;
import cn.master.backend.request.EditTestCaseRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-17
 */
public interface TestCaseService extends IService<TestCase> {

    /**
     * desc: add test case
     *
     * @param request parameter
     * @return cn.master.backend.entity.TestCase
     */
    TestCase addTestCase(EditTestCaseRequest request);

    /**
     * desc: 分页查询数据
     *
     * @param request 查询参数
     * @param page    分页参数
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysProject>
     */
    IPage<TestCase> listTestCase(QueryTestCaseRequest request, Page<TestCase> page);

    IPage<TestCase> listTestCase(QueryTestCaseRequest request, Page<TestCase> page, boolean isSampleInfo);

    TestCase getTestCase(String testCaseId);

    TestCase editTestCase(EditTestCaseRequest request);

    List<TestCase> getTestCaseVersions(String caseId);

    /**
     * desc: delete test case
     *
     * @param testCaseId test case id
     * @return int
     */
    int deleteTestCaseToGc(String testCaseId);

    IPage<TestCaseDTO> getTestCaseRelateList(QueryTestCaseRequest request, long page, long limit);

    IPage<TestCaseDTO> getTestReviewCase(QueryTestCaseRequest request, Page<TestCase> page);
}
