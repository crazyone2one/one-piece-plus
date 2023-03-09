package cn.master.backend.service.impl;

import cn.master.backend.constants.UserGroupType;
import cn.master.backend.entity.*;
import cn.master.backend.mapper.SysGroupMapper;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.GroupRequest;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.service.SysGroupService;
import cn.master.backend.service.SysWorkspaceService;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-01
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {
    final JwtUtils jwtUtils;
    final SysUserGroupMapper sysUserGroupMapper;
    final SysWorkspaceService sysWorkspaceService;

    private static final String GLOBAL = "global";
    private static final Map<String, String> typeMap = new HashMap<>(4) {{
        put(UserGroupType.SYSTEM, "系统");
        put(UserGroupType.WORKSPACE, "工作空间");
        put(UserGroupType.PROJECT, "项目");
    }};
    private static final Map<String, List<String>> map = new HashMap<>(4) {{
        put(UserGroupType.SYSTEM, Arrays.asList(UserGroupType.SYSTEM, UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.WORKSPACE, Arrays.asList(UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.PROJECT, Collections.singletonList(UserGroupType.PROJECT));
    }};
    @Override
    public SysGroup addGroup(HttpServletRequest httpServletRequest, EditGroupRequest request) {
        checkGroupExist(request);
        String id = jwtUtils.getUserIdFromToken(httpServletRequest);
        request.setCreator(id);
        request.setSystem(false);
        if (BooleanUtils.isTrue(request.getGlobal())) {
            request.setScopeId(GLOBAL);
        }
        baseMapper.insert(request);
        return request;
    }

    @Override
    public List<SysGroup> getGroupByType(EditGroupRequest request) {
        List<SysGroup> list = new ArrayList<>();
        LambdaQueryWrapper<SysGroup> wrapper = new LambdaQueryWrapper<>();
        String type = request.getType();
        if (StringUtils.isBlank(type)) {
            return list;
        }
        wrapper.eq(!StringUtils.equals(type, UserGroupType.SYSTEM), SysGroup::getType, type);
        wrapper.eq(BooleanUtils.isTrue(request.isOnlyQueryGlobal()), SysGroup::getScopeId, GLOBAL);
        wrapper.orderByDesc(SysGroup::getUpdateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> getAllUserGroup(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        LambdaQueryWrapper<SysUserGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserGroup::getUserId, userId);
        List<SysUserGroup> sysUserGroups = sysUserGroupMapper.selectList(lambdaQueryWrapper);
        List<String> groupsIds = sysUserGroups.stream().map(SysUserGroup::getGroupId).distinct().collect(Collectors.toList());
        for (String id : groupsIds) {
            SysGroup sysGroup = baseMapper.selectById(id);
            String type = sysGroup.getType();
            Map<String, Object> map = new HashMap<>(2);
            map.put("type", id + "+" + type);
            WorkspaceResource workspaceResource = sysWorkspaceService.listResource(id, sysGroup.getType());
            List<String> collect = sysUserGroups.stream().filter(ug -> ug.getGroupId().equals(id)).map(SysUserGroup::getSourceId).collect(Collectors.toList());
            map.put("ids", collect);
            if (StringUtils.equals(type, UserGroupType.WORKSPACE)) {
                map.put("workspaces", workspaceResource.getWorkspaces());
            }
            if (StringUtils.equals(type, UserGroupType.PROJECT)) {
                map.put("projects", workspaceResource.getProjects());
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public List<SysGroup> getWorkspaceMemberGroups(String workspaceId, String userId) {
        return sysUserGroupMapper.getWorkspaceMemberGroups(workspaceId, userId);
    }

    @Override
    public IPage<SysGroup> getGroupList(EditGroupRequest request, Page<SysGroup> producePage, HttpServletRequest httpServletRequest) {
        String id = jwtUtils.getUserIdFromToken(httpServletRequest);
        List<UserGroupDTO> userGroup = sysUserGroupMapper.getUserGroup(id, request.getProjectId());
        List<String> groupTypeList = userGroup.stream().map(UserGroupDTO::getType).distinct().collect(Collectors.toList());
        return getGroups(groupTypeList, request,producePage);
    }

    @Override
    public List<SysGroup> getGroupsByType(GroupRequest request) {
        String resourceId = request.getResourceId();
        String type = request.getType();
        List<String> scopeList = Arrays.asList(GLOBAL, resourceId);
        if (StringUtils.equals(type, UserGroupType.PROJECT) && StringUtils.isNotBlank(request.getProjectId())) {
            scopeList = Arrays.asList(GLOBAL, resourceId, request.getProjectId());
        }
        LambdaQueryWrapper<SysGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysGroup::getScopeId, scopeList).eq(SysGroup::getType, type);
        return baseMapper.selectList(wrapper);
    }

    private IPage<SysGroup> getGroups(List<String> groupTypeList, EditGroupRequest request, Page<SysGroup> producePage) {
        if (groupTypeList.contains(UserGroupType.SYSTEM)) {
            return getUserGroup(UserGroupType.SYSTEM, request,producePage);
        }
        if (groupTypeList.contains(UserGroupType.WORKSPACE)) {
            return getUserGroup(UserGroupType.WORKSPACE, request,producePage);
        }

        if (groupTypeList.contains(UserGroupType.PROJECT)) {
            return getUserGroup(UserGroupType.PROJECT, request,producePage);
        }
        return null;
    }

    private IPage<SysGroup> getUserGroup(String groupType, EditGroupRequest request, Page<SysGroup> producePage) {
        List<String> types;
        String workspaceId = SessionUtils.getCurrentWorkspaceId();
        String projectId = SessionUtils.getCurrentProjectId();
        List<String> scopes = Arrays.asList(GLOBAL, workspaceId, projectId);
        if (StringUtils.equals(groupType, UserGroupType.SYSTEM)) {
            scopes = new ArrayList<>();
        }
        types = map.get(groupType);
        request.setTypes(types);
        request.setScopes(scopes);
        IPage<SysGroup> groups = baseMapper.getGroupList(producePage, request);
        buildUserInfo(groups.getRecords());
        return groups;
    }

    private void buildUserInfo(List<SysGroup> groups) {
        List<String> userIds = groups.stream().map(SysGroup::getCreator).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            Map<String, String> userMap = ServiceUtils.getUserNameMap(userIds);
            groups.forEach(caseResult -> caseResult.setCreator(userMap.getOrDefault(caseResult.getCreator(), caseResult.getCreator())));
        }
    }


    private void checkGroupExist(EditGroupRequest request) {
        String name = request.getName();
        LambdaQueryWrapper<SysGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroup::getName, name);
        if (baseMapper.exists(wrapper)) {
            throw new IllegalArgumentException("用户组名称已存在");
        }
    }
}
