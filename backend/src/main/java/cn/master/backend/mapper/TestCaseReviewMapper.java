package cn.master.backend.mapper;

import cn.master.backend.entity.TestCaseReview;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-01
 */
public interface TestCaseReviewMapper extends BaseMapper<TestCaseReview> {

    /**
     * desc: 查询列表。page参数不为null时则是分页查询
     *
     * @param page    分页条件
     * @param wrapper 查询条件
     * @return java.util.List<cn.master.backend.entity.TestCaseReview>
     */
    @Select("select * from test_case_review ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "creator", property = "creator"),
            @Result(column = "creator", property = "creatorName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysUserMapper.getNameById", fetchType = FetchType.EAGER)),
            @Result(column = "project_id", property = "projectId"),
            @Result(column = "project_id", property = "projectName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysProjectMapper.getNameById", fetchType = FetchType.EAGER)),
    })
    IPage<TestCaseReview> selectPageVo(IPage<TestCaseReview> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestCaseReview> wrapper);
}
