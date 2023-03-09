package cn.master.backend.mapper;

import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseDTO;
import cn.master.backend.request.QueryTestCaseRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-17
 */
public interface TestCaseMapper extends BaseMapper<TestCase> {
    /**
     * desc: get max number by project id
     *
     * @param projectId project id
     * @return cn.master.backend.entity.TestCase
     */
    @Select("SELECT * FROM test_case WHERE test_case.project_id = #{projectId} ORDER BY num DESC LIMIT 1;")
    TestCase getMaxNumByProjectId(@Param("projectId") String projectId);

    /**
     * desc: get latest order
     *
     * @param projectId project id
     * @param baseOrder base order
     * @return java.lang.Long
     */
    Long getLastOrder(@Param("projectId") String projectId, @Param("baseOrder") Long baseOrder);

    IPage<TestCaseDTO> getTestCaseByNotInPlan(IPage<?> page, @Param("request") QueryTestCaseRequest request);

    @Select("select  test_case.id," +
            "        test_case.name," +
            "        test_case.priority," +
            "        test_case.type," +
            "        test_case.review_status from test_case ${ew.customSqlSegment}")
    IPage<TestCaseDTO> getTestCaseByNotInReview(IPage<?> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    /**
     * desc: 查询列表
     *
     * @param page    分页条件 为null时不分页
     * @param wrapper 查询条件
     * @return java.util.List<cn.master.backend.entity.TestCase>
     */
    @Select("select * from test_case ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "maintainer", property = "maintainer"),
            @Result(column = "maintainer", property = "maintainerName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysUserMapper.getNameById", fetchType = FetchType.EAGER)),
            @Result(column = "project_id", property = "projectId"),
            @Result(column = "project_id", property = "projectName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysProjectMapper.getNameById", fetchType = FetchType.EAGER)),
    })
    List<TestCase> selectPageVO(IPage<?> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);
}
