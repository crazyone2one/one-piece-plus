package cn.master.backend.service.impl;

import cn.master.backend.entity.ParamsDTO;
import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.TestPlanCaseDTO;
import cn.master.backend.entity.TestPlanTestCase;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.mapper.TestPlanTestCaseMapper;
import cn.master.backend.request.QueryMemberRequest;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import cn.master.backend.service.TestPlanTestCaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-29
 */
@Service
@RequiredArgsConstructor
public class TestPlanTestCaseServiceImpl extends ServiceImpl<TestPlanTestCaseMapper, TestPlanTestCase> implements TestPlanTestCaseService {
    final SysUserMapper sysUserMapper;

    @Override
    public Map<String, ParamsDTO> testPlanTestCaseCount(Set<String> planIds) {
        LambdaQueryWrapper<TestPlanTestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TestPlanTestCase::getPlanId, planIds);
        return null;
    }

    @Override
    public List<TestPlanCaseDTO> getTestPlanCases(QueryTestPlanCaseRequest request, long page, long limit) {
        List<TestPlanCaseDTO> list = baseMapper.listTestPlanTestCase(new Page<>(page, limit), request);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, String> userMap = sysUserMapper.getProjectMemberList(null, request.getProjectId()).stream().collect(Collectors.toMap(SysUser::getId, SysUser::getName));
            list.forEach(item -> {
                item.setExecutorName(userMap.get(item.getExecutor()));
                item.setMaintainerName(userMap.get(item.getMaintainer()));
            });
        }
        return list;
    }
}
