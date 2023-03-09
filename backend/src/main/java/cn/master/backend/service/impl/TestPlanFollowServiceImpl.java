package cn.master.backend.service.impl;

import cn.master.backend.entity.TestPlanFollow;
import cn.master.backend.mapper.TestPlanFollowMapper;
import cn.master.backend.service.TestPlanFollowService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestPlanFollowServiceImpl extends ServiceImpl<TestPlanFollowMapper, TestPlanFollow> implements TestPlanFollowService {

    @Override
    public void deletePlanFollowByPlanIds(List<String> planIds) {
        if (CollectionUtils.isNotEmpty(planIds)) {
            LambdaQueryWrapper<TestPlanFollow> wrapper = new LambdaQueryWrapper<TestPlanFollow>().in(TestPlanFollow::getTestPlanId, planIds);
            baseMapper.delete(wrapper);
        }
    }

    @Override
    public void addPlanFollow(TestPlanFollow testPlanFollow) {
        baseMapper.insert(testPlanFollow);
    }
}
