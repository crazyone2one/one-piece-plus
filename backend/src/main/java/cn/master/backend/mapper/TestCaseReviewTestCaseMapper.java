package cn.master.backend.mapper;

import cn.master.backend.entity.TestCaseReviewTestCase;
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
 * @since 2023-02-02
 */
public interface TestCaseReviewTestCaseMapper extends BaseMapper<TestCaseReviewTestCase> {

    @Select("select case_id from test_case_review_test_case where review_id=#{reviewId}")
    List<String> getCaseIdByReviewId(String reviewId);

    /**
     * desc: 查询列表
     *
     * @param page    分页条件 为null时不分页
     * @param wrapper 查询条件
     * @return java.util.List<cn.master.backend.entity.TestCaseReviewTestCase>
     */
    @Select("select * from test_case_review_test_case ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "reviewer", property = "reviewer"),
            @Result(column = "reviewer", property = "reviewerName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysUserMapper.getNameById", fetchType = FetchType.EAGER)),
    })
    IPage<TestCaseReviewTestCase> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestCaseReviewTestCase> wrapper);
}
