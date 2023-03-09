package cn.master.backend.service;

import cn.master.backend.entity.ParamsDTO;
import cn.master.backend.entity.TestPlanCaseDTO;
import cn.master.backend.entity.TestPlanTestCase;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-29
 */
public interface TestPlanTestCaseService extends IService<TestPlanTestCase> {
    /**
     * desc: 测试计划关联测试用例的数量
     *
     * @param planIds plan IDs
     * @return java.util.Map<java.lang.String, cn.master.backend.entity.ParamsDTO>
     */
    Map<String, ParamsDTO> testPlanTestCaseCount(Set<String> planIds);

    List<TestPlanCaseDTO> getTestPlanCases(QueryTestPlanCaseRequest request, long page, long limit);
}
