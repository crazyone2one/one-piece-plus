package cn.master.backend.service;

import cn.master.backend.entity.TestPlanFollow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
public interface TestPlanFollowService extends IService<TestPlanFollow> {

    /**
     * delete by plan
     *
     * @param planIds a list of plan id
     */
    void deletePlanFollowByPlanIds(List<String> planIds);

    /**
     * desc: add item
     *
     * @param testPlanFollow item
     */
    void addPlanFollow(TestPlanFollow testPlanFollow);

}
