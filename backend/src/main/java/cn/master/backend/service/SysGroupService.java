package cn.master.backend.service;

import cn.master.backend.entity.SysGroup;
import cn.master.backend.entity.SysUser;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.GroupRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-01
 */
public interface SysGroupService extends IService<SysGroup> {

    /**
     * desc: add group
     *
     * @param httpServletRequest http request
     * @param request            parameters
     * @return cn.master.backend.entity.SysGroup
     */
    SysGroup addGroup(HttpServletRequest httpServletRequest, EditGroupRequest request);

    List<SysGroup> getGroupByType(EditGroupRequest request);

    List<Map<String, Object>> getAllUserGroup(String userId);

    List<SysGroup> getWorkspaceMemberGroups(String workspaceId, String userId);

    IPage<SysGroup> getGroupList(EditGroupRequest request, Page<SysGroup> producePage, HttpServletRequest httpServletRequest);

    /**
     * desc: search by type
     *
     * @param request search request parameters
     * @return java.util.List<cn.master.backend.entity.SysGroup>
     */
    List<SysGroup> getGroupsByType(GroupRequest request);
}
