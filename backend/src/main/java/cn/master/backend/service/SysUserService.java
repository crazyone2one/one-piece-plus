package cn.master.backend.service;

import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.UserDTO;
import cn.master.backend.entity.UserGroupPermissionDTO;
import cn.master.backend.request.AddMemberRequest;
import cn.master.backend.request.EditPassWordRequest;
import cn.master.backend.request.QueryMemberRequest;
import cn.master.backend.request.UserRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * desc: 添加用户信息
     *
     * @param httpServletRequest HttpServletRequest
     * @param sysUser            参数
     * @return cn.master.backend.entity.SysUser
     */
    UserDTO addUser(HttpServletRequest httpServletRequest, UserRequest sysUser);

    /**
     * desc: refresh user
     *
     * @param httpServletRequest HttpServletRequest
     * @param sign               sign
     * @param sourceId           sourceId
     */
    void refreshSessionUser(HttpServletRequest httpServletRequest, String sign, String sourceId);

    List<SysUser> getMemberList(QueryMemberRequest request);

    /**
     * desc: 更新user
     *
     * @param sign               workspace/project
     * @param sourceId           sourceId
     * @param httpServletRequest httpServletRequest
     * @return cn.master.backend.entity.UserDTO
     */
    SysUser switchUserResource(String sign, String sourceId, HttpServletRequest httpServletRequest);

    /**
     * desc: 查询项目下成员数量
     *
     * @param request     查询参数
     * @param producePage 分页参数
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    List<SysUser> getProjectMemberList(QueryMemberRequest request, Page<SysUser> producePage);

    /**
     * desc: 删除项目数据时更新用户信息
     *
     * @param user user parameter
     * @return cn.master.backend.entity.UserDTO
     */
    UserDTO updateCurrentUser(SysUser user);

    /**
     * desc: update user
     *
     * @param user user parameter
     */
    void updateUser(SysUser user);

    UserDTO getCurrentUser(String userId);

    IPage<SysUser> getMemberList(QueryMemberRequest request, Page<SysUser> producePage);

    /**
     * desc: 用户模块列表数据查询
     *
     * @param request     查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysUser>
     */
    IPage<SysUser> getUserListWithRequest(UserRequest request, Page<SysUser> producePage);

    /**
     * desc: return user's permissions
     *
     * @param userId user id
     * @return cn.master.backend.entity.UserGroupPermissionDTO
     */
    UserGroupPermissionDTO getUserGroup(String userId);

    /**
     * desc: 更新user
     *
     * @param request 参数
     * @return java.lang.String
     */
    String updateUserRole(UserRequest request);

    /**
     * desc: update user's password
     *
     * @param request parameter
     * @return java.lang.Integer
     */
    Integer updateUserPassword(EditPassWordRequest request);

    List<SysUser> getProjectMemberOption(String projectId);

    /**
     * desc: 用户列表
     *
     * @return java.util.List<cn.master.backend.entity.SysUser>
     */
    List<SysUser> getUserList();

    /**
     * desc: 添加工作空间成员
     *
     * @param request parameter
     * @return java.lang.String
     */
    String addWorkspaceMember(AddMemberRequest request);

    /**
     * desc: 添加项目用户关联关系
     *
     * @param request parameter
     * @return
     */
    String addProjectMember(AddMemberRequest request);

    int deleteProjectMember(String projectId, String userId);

    /**
     * desc: delete user
     *
     * @param userId user id
     * @return int
     */
    int deleteUser(String userId);

    long getUserSize();
}
