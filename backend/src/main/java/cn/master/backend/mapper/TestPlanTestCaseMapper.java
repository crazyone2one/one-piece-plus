package cn.master.backend.mapper;

import cn.master.backend.entity.CountMapDTO;
import cn.master.backend.entity.TestPlanCaseDTO;
import cn.master.backend.entity.TestPlanTestCase;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-29
 */
public interface TestPlanTestCaseMapper extends BaseMapper<TestPlanTestCase> {

    List<CountMapDTO> getExecResultMapByPlanId(@Param("planId") String planId);

    /**
     * desc: delete records by plan id
     *
     * @param wrapper parameter
     */
    @Delete("delete from test_plan_test_case ${ew.customSqlSegment}")
    void delByPlanId(@Param(Constants.WRAPPER) LambdaQueryWrapper<TestPlanTestCase> wrapper);

    List<TestPlanCaseDTO> listTestPlanTestCase(IPage<?> page, @Param("request") QueryTestPlanCaseRequest request);


    Long getLastOrder(@Param("planId") String planId, @Param("baseOrder") Long baseOrder);
}
