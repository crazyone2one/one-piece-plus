package cn.master.backend.service.impl;

import cn.master.backend.entity.TestPlanPrincipal;
import cn.master.backend.mapper.TestPlanPrincipalMapper;
import cn.master.backend.service.TestPlanPrincipalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestPlanPrincipalServiceImpl extends ServiceImpl<TestPlanPrincipalMapper, TestPlanPrincipal> implements TestPlanPrincipalService {

    @Override
    public void addPlanPrincipal(TestPlanPrincipal testPlanPrincipal) {
        baseMapper.insert(testPlanPrincipal);
    }

    @Override
    public void deleteTestPlanPrincipalByPlanId(List<String> planIds) {
        LambdaQueryWrapper<TestPlanPrincipal> wrapper = new LambdaQueryWrapper<TestPlanPrincipal>().in(TestPlanPrincipal::getTestPlanId, planIds);
        baseMapper.delete(wrapper);
    }
}
