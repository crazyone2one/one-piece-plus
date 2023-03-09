package cn.master.backend.mapper;

import cn.master.backend.entity.ParamsDTO;
import cn.master.backend.entity.TestPlan;
import cn.master.backend.entity.TestPlanDtoWithMetric;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
public interface TestPlanMapper extends BaseMapper<TestPlan> {

    /**
     * desc: 查询列表
     *
     * @param page    分页条件
     * @param wrapper 查询条件
     * @return java.util.List<cn.master.backend.entity.TestPlanDtoWithMetric>
     */
    @Select("select * from test_plan ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "project_id", property = "projectId"),
            @Result(column = "project_id", property = "projectName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysProjectMapper.getNameById")),
            @Result(column = "creator", property = "creator"),
            @Result(column = "creator", property = "createUser", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysUserMapper.getNameById", fetchType = FetchType.EAGER)),
    })
    List<TestPlanDtoWithMetric> selectPageVo(IPage<TestPlan> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestPlan> wrapper);

    /**
     * desc: 测试计划关联测试用例的数量
     *
     * @param planIds plan is
     * @return java.util.Map<java.lang.String, cn.master.backend.entity.ParamsDTO>
     */
    @MapKey("id")
    Map<String, ParamsDTO> testPlanTestCaseCount(@Param("planIds") Set<String> planIds);
}
