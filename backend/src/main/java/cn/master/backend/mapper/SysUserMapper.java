package cn.master.backend.mapper;

import cn.master.backend.entity.SysUser;
import cn.master.backend.request.QueryMemberRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * desc: search for user by username
     *
     * @param name name of the user
     * @return cn.master.backend.entity.SysUser
     */
    @Select("select * from sys_user where name=#{name}")
    SysUser findByName(String name);

    /**
     * desc: 是否是超级用户
     *
     * @param userId userid
     * @return boolean
     */
    @Select("select count(*) from sys_user_group where user_id = #{userId} and group_id = 'super_group'")
    boolean isSuperUser(String userId);

    /**
     * desc: search name by user id
     *
     * @param userId id of the user
     * @return java.lang.String
     */
    @Select("select name from sys_user where id = #{userId}")
    String getNameById(String userId);

    /**
     * desc: 查询用户
     *
     * @param page
     * @param projectId project id
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    @Select("SELECT DISTINCT * from (SELECT u.*  FROM sys_user_group ug JOIN `sys_user` u  ON ug.user_id = u.id WHERE ug.source_id = #{projectId} ORDER BY u.update_time DESC) temp")
    List<SysUser> getProjectMemberList(Page<SysUser> page, String projectId);

    /**
     * desc: 更新用户数据
     *
     * @param projectId project id
     * @param userId    user id
     */
    @Update("UPDATE sys_user SET last_project_id = #{projectId} WHERE id = #{userId}")
    void updateLastProjectIdIfNull(@Param("projectId") String projectId, @Param("userId") String userId);

    /**
     * desc: 分页查询
     *
     * @param page    分页条件
     * @param request 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysUser>
     */
    IPage<SysUser> selectPageVo(IPage<?> page, @Param("member") QueryMemberRequest request);

    /**
     * desc: 查询用户名称
     *
     * @param userIds user IDs
     * @return java.util.Map<java.lang.String, cn.master.backend.entity.SysUser>
     */
    @MapKey("id")
    Map<String, SysUser> queryNameByIds(List<String> userIds);

    /**
     * desc: 查询用户
     *
     * @param projectId project id
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    @Select("SELECT DISTINCT * from (SELECT u.id, u.name, u.email  FROM sys_user_group ug JOIN `sys_user` u  ON ug.user_id = u.id WHERE ug.source_id = #{projectId} ORDER BY u.update_time DESC) temp")
    List<SysUser> getProjectMemberOption(String projectId);

    /**
     * desc: 查询用户
     *
     * @param request 查询条件
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    List<SysUser> getMemberList(@Param("member") QueryMemberRequest request);

    /**
     * desc: 查询用户
     *
     * @param wrapper 查询条件
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    @Select("select * from sys_user ${ew.customSqlSegment}")
    List<SysUser> getAll(@Param(Constants.WRAPPER) LambdaQueryWrapper<SysUser> wrapper);
}
