package cn.master.backend.service;

import cn.master.backend.entity.TestPlanPrincipal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-28
 */

public interface TestPlanPrincipalService extends IService<TestPlanPrincipal> {

    /**
     * desc: add plan principal record
     *
     * @param testPlanPrincipal parameter
     */
    void addPlanPrincipal(TestPlanPrincipal testPlanPrincipal);

    /**
     * desc: delete plan principal record
     *
     * @param planIds plan id
     */
    void deleteTestPlanPrincipalByPlanId(List<String> planIds);
}
