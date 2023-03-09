package cn.master.backend.service;

import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.SysUserGroup;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.EditGroupUserRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
public interface SysUserGroupService extends IService<SysUserGroup> {
    /**
     * desc: 查询group下的用户
     *
     * @param request     查询参数
     * @param producePage 分页参数
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysUser>
     */
    IPage<SysUser> getGroupUser(EditGroupRequest request, Page<SysUser> producePage);

    /**
     * desc: 移除关联关系
     *
     * @param userId  user id
     * @param groupId group id
     * @return int
     */
    int removeGroupMember(String userId, String groupId);

    /**
     * desc: 添加关联数据
     *
     * @param request parameter
     * @return int
     */
    int addGroupUser(EditGroupUserRequest request);
}
