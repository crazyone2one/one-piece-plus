package cn.master.backend.service.impl;

import cn.master.backend.constants.UserGroupType;
import cn.master.backend.constants.UserStatus;
import cn.master.backend.entity.*;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.SysGroupMapper;
import cn.master.backend.mapper.SysProjectMapper;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.request.AddMemberRequest;
import cn.master.backend.request.EditPassWordRequest;
import cn.master.backend.request.QueryMemberRequest;
import cn.master.backend.request.UserRequest;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.service.SysUserService;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final SysUserGroupMapper sysUserGroupMapper;
    final SysGroupMapper sysGroupMapper;
    final SysProjectMapper sysProjectMapper;
    final SysUserMapper sysUserMapper;
    private final static String DEFAULT_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

    @Override
    public UserDTO addUser(HttpServletRequest httpServletRequest, UserRequest sysUser) {
//        checkUserParam(sysUser);
        String password = passwordEncoder.encode(StringUtils.isBlank(sysUser.getPassword()) ? DEFAULT_PASSWORD : sysUser.getPassword());
        sysUser.setPassword(password);
        sysUser.setStatus(UserStatus.NORMAL);
        sysUser.setSource("LOCAL");
        baseMapper.insert(sysUser);
        List<Map<String, Object>> groups = sysUser.getGroups();
        if (!groups.isEmpty()) {
            insertUserGroup(groups, sysUser.getId());
        }
        return getUserDTO(sysUser.getId());
    }

    private void insertUserGroup(List<Map<String, Object>> groups, String userId) {
        for (Map<String, Object> map : groups) {
            String idType = (String) map.get("type");
            if (StringUtils.isNotBlank(idType)) {
                String[] arr = idType.split("\\+");
                String groupId = arr[0];
                String type = arr[1];
                if (StringUtils.equals(type, UserGroupType.SYSTEM)) {
                    SysUserGroup sysUserGroup = SysUserGroup.builder().userId(userId).groupId(groupId).sourceId("system").build();
                    sysUserGroupMapper.insert(sysUserGroup);
                } else {
                    List<String> ids = (List<String>) map.get("ids");
                    for (String id : ids) {
                        SysUserGroup sysUserGroup = SysUserGroup.builder().userId(userId).groupId(groupId).sourceId(id).build();
                        sysUserGroupMapper.insert(sysUserGroup);
                    }
                }
            }
        }
    }


    @Override
    public void refreshSessionUser(HttpServletRequest httpServletRequest, String sign, String sourceId) {
        String userId = returnUserId(httpServletRequest);
        SysUser sysUser = baseMapper.selectById(userId);
        SysUser newUser = new SysUser();
        if (StringUtils.equals("organization", sign)) {
            sysUser.setLastWorkspaceId(StringUtils.EMPTY);
        }
        if (StringUtils.equals("workspace", sign) && StringUtils.equals(sourceId, sysUser.getLastWorkspaceId())) {
            sysUser.setLastWorkspaceId(StringUtils.EMPTY);
        }
        BeanUtils.copyProperties(sysUser, newUser);
        baseMapper.updateById(newUser);
    }

    private String returnUserId(HttpServletRequest httpServletRequest) {
        String token = jwtUtils.getJwtTokenFromRequest(httpServletRequest);
        Claims claims = jwtUtils.extractAllClaims(token);
        return (String) claims.get("id");
    }

    @Override
    public List<SysUser> getMemberList(QueryMemberRequest request) {
        return baseMapper.getMemberList(request);
    }

    @Override
    public SysUser switchUserResource(String sign, String sourceId, HttpServletRequest httpServletRequest) {
        String userId = returnUserId(httpServletRequest);
        SysUser user = baseMapper.selectById(userId);
        boolean isSuper = baseMapper.isSuperUser(userId);
        if (StringUtils.equals("workspace", sign)) {
            user.setLastWorkspaceId(sourceId);
            List<SysProject> projects = getProjectListByWsAndUserId(userId, sourceId);
            if (CollectionUtils.isNotEmpty(projects)) {
                user.setLastProjectId(projects.get(0).getId());
            } else {
                if (isSuper) {
                    LambdaQueryWrapper<SysProject> projectWrapper = new LambdaQueryWrapper<SysProject>().eq(SysProject::getWorkspaceId, sourceId);
                    List<SysProject> allWsProject = sysProjectMapper.selectList(projectWrapper);
                    if (CollectionUtils.isNotEmpty(allWsProject)) {
                        user.setLastProjectId(allWsProject.get(0).getId());
                    }
                } else {
                    user.setLastProjectId(StringUtils.EMPTY);
                }
            }
        }
        baseMapper.updateById(user);
        return user;
    }

    @Override
    public List<SysUser> getProjectMemberList(QueryMemberRequest request, Page<SysUser> producePage) {
        return baseMapper.getProjectMemberList(producePage, request.getProjectId());
    }

    @Override
    public UserDTO updateCurrentUser(SysUser user) {
        updateUser(user);
        return getUserDTO(user.getId());
    }

    @Override
    public void updateUser(SysUser user) {
        SysUser userFromDB = baseMapper.selectById(user.getId());
        if (user.getLastWorkspaceId() != null && !StringUtils.equals(user.getLastWorkspaceId(), userFromDB.getLastWorkspaceId())) {
            List<SysProject> projects = getProjectListByWsAndUserId(user.getId(), user.getLastWorkspaceId());
            if (CollectionUtils.isNotEmpty(projects)) {
                // 如果传入的 last_project_id 是 last_workspace_id 下面的
                boolean present = projects.stream().anyMatch(p -> StringUtils.equals(p.getId(), user.getLastProjectId()));
                if (!present) {
                    user.setLastProjectId(projects.get(0).getId());
                }
            } else {
                user.setLastProjectId(StringUtils.EMPTY);
            }
        }
        baseMapper.updateById(user);
    }

    @Override
    public UserDTO getCurrentUser(String userId) {
        return getUserDTO(userId);
    }

    @Override
    public IPage<SysUser> getMemberList(QueryMemberRequest request, Page<SysUser> producePage) {
        return baseMapper.selectPageVo(producePage, request);
    }

    @Override
    public IPage<SysUser> getUserListWithRequest(UserRequest request, Page<SysUser> producePage) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNoneBlank(request.getName()), SysUser::getName, request.getName());
        wrapper.like(StringUtils.isNoneBlank(request.getEmail()), SysUser::getEmail, request.getEmail());
        wrapper.orderByDesc(SysUser::getCreateTime);
        return baseMapper.selectPage(producePage, wrapper);
    }

    @Override
    public UserGroupPermissionDTO getUserGroup(String userId) {
        UserGroupPermissionDTO userGroupPermissionDTO = new UserGroupPermissionDTO();
        LambdaQueryWrapper<SysUserGroup> sysUserGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserGroupLambdaQueryWrapper.eq(SysUserGroup::getUserId, userId);
        List<SysUserGroup> userGroups = sysUserGroupMapper.selectList(sysUserGroupLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(userGroups)) {
            return userGroupPermissionDTO;
        }
        userGroupPermissionDTO.setUserGroups(userGroups);
        List<String> groupId = userGroups.stream().map(SysUserGroup::getGroupId).collect(Collectors.toList());
        LambdaQueryWrapper<SysGroup> groupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        groupLambdaQueryWrapper.in(SysGroup::getId, groupId);
        List<SysGroup> groups = sysGroupMapper.selectList(groupLambdaQueryWrapper);
        userGroupPermissionDTO.setGroups(groups);
        return userGroupPermissionDTO;
    }

    @Override
    public String updateUserRole(UserRequest request) {
        String userId = request.getId();
        LambdaQueryWrapper<SysUserGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserGroup::getUserId, userId);
        List<SysUserGroup> userGroups = sysUserGroupMapper.selectList(queryWrapper);
        List<String> list = userGroups.stream().map(SysUserGroup::getSourceId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.contains(request.getLastWorkspaceId())) {
                request.setLastWorkspaceId(null);
                baseMapper.updateById(request);
            }
        }
        sysUserGroupMapper.delete(queryWrapper);
        List<Map<String, Object>> groups = request.getGroups();
        if (!groups.isEmpty()) {
            insertUserGroup(groups, request.getId());
        }
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNoneBlank(request.getEmail()), SysUser::getEmail, request.getEmail());
        wrapper.ne(StringUtils.isNoneBlank(request.getId()), SysUser::getId, request.getId());
        if (baseMapper.exists(wrapper)) {
            throw new CustomException("user_email_already_exists");
        }
        baseMapper.updateById(request);
        return "updated successfully";
    }

    @Override
    public Integer updateUserPassword(EditPassWordRequest request) {
        SysUser sysUser = baseMapper.selectById(request.getId());
        String password = passwordEncoder.encode(request.getNewPassword());
        sysUser.setPassword(password);
        return baseMapper.updateById(sysUser);
    }

    @Override
    public List<SysUser> getProjectMemberOption(String projectId) {
        return baseMapper.getProjectMemberOption(projectId);
    }

    @Override
    public List<SysUser> getUserList() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getUpdateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public String addWorkspaceMember(AddMemberRequest request) {
        addGroupMember("WORKSPACE", request.getWorkspaceId(), request.getUserIds(), request.getGroupIds());
        return "添加完成";
    }

    @Override
    public String addProjectMember(AddMemberRequest request) {
        addGroupMember("PROJECT", request.getProjectId(), request.getUserIds(), request.getGroupIds());
        return "添加完成";
    }

    @Override
    public int deleteProjectMember(String projectId, String userId) {
        LambdaQueryWrapper<SysGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroup::getType, UserGroupType.PROJECT);
        List<SysGroup> groups = sysGroupMapper.selectList(wrapper);
        List<String> groupIds = groups.stream().map(SysGroup::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIds)) {
            return 0;
        }
        LambdaQueryWrapper<SysUserGroup> userGroupWrapper = new LambdaQueryWrapper<>();
        userGroupWrapper.eq(SysUserGroup::getUserId, userId)
                .eq(SysUserGroup::getSourceId, projectId)
                .in(SysUserGroup::getGroupId, groupIds);
        SysUser sysUser = baseMapper.selectById(userId);
        if (StringUtils.equals(projectId, sysUser.getLastProjectId())) {
            sysUser.setLastProjectId(StringUtils.EMPTY);
            baseMapper.updateById(sysUser);
        }
        return sysUserGroupMapper.delete(userGroupWrapper);
    }

    @Override
    public int deleteUser(String userId) {
        String id = SessionUtils.getUserId();
        if (StringUtils.equals(id, userId)) {
            throw new CustomException("无法删除当前登录用户");
        }
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserGroup::getUserId, userId);
        sysUserGroupMapper.delete(wrapper);
        return baseMapper.deleteById(userId);
    }

    @Override
    public long getUserSize() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectCount(wrapper);
    }

    private void addGroupMember(String type, String sourceId, List<String> userIds, List<String> groupIds) {
        if (!StringUtils.equalsAny(type, "PROJECT", "WORKSPACE") || StringUtils.isBlank(sourceId)
                || CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(groupIds)) {
            log.warn("add member warning, please check param!");
            return;
        }
        List<String> dbOptionalGroupIds = getGroupIdsByType(type, sourceId);
        for (String userId : userIds) {
            SysUser sysUser = sysUserMapper.selectById(userId);
            if (Objects.isNull(sysUser)) {
                log.warn("add member warning, invalid user id:" + userId);
                continue;
            }
            List<String> toAddGroupIds = new ArrayList<>(groupIds);
            List<String> existGroupIds = getUserExistSourceGroup(userId, sourceId);
            toAddGroupIds.removeAll(existGroupIds);
            toAddGroupIds.retainAll(dbOptionalGroupIds);
            if (CollectionUtils.isEmpty(toAddGroupIds)) {
                log.warn("group ids not in db or not has permission, please check!");
                continue;
            }
            for (String groupId : toAddGroupIds) {
                SysUserGroup sysUserGroup = SysUserGroup.builder().userId(userId).groupId(groupId).sourceId(sourceId).build();
                sysUserGroupMapper.insert(sysUserGroup);
            }
        }
    }

    private List<String> getUserExistSourceGroup(String userId, String sourceId) {
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserGroup::getUserId, userId).eq(SysUserGroup::getSourceId, sourceId);
        List<SysUserGroup> userGroups = sysUserGroupMapper.selectList(wrapper);
        return userGroups.stream().map(SysUserGroup::getGroupId).collect(Collectors.toList());
    }

    private List<String> getGroupIdsByType(String type, String sourceId) {
        List<String> scopeList = Arrays.asList("global", sourceId);
        if (StringUtils.equals(type, "PROJECT")) {
            SysProject sysProject = sysProjectMapper.selectById(sourceId);
            if (Objects.nonNull(sysProject)) {
                scopeList = Arrays.asList("global", sourceId, sysProject.getWorkspaceId());
            }
        }
        LambdaQueryWrapper<SysGroup> groupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        groupLambdaQueryWrapper.in(SysGroup::getScopeId, scopeList).eq(SysGroup::getType, type);
        List<SysGroup> groups = sysGroupMapper.selectList(groupLambdaQueryWrapper);
        return groups.stream().map(SysGroup::getId).collect(Collectors.toList());
    }

    private List<SysProject> getProjectListByWsAndUserId(String userId, String sourceId) {
        LambdaQueryWrapper<SysProject> projectWrapper = new LambdaQueryWrapper<SysProject>().eq(SysProject::getWorkspaceId, sourceId);
        List<SysProject> projects = sysProjectMapper.selectList(projectWrapper);
        LambdaQueryWrapper<SysUserGroup> userGroupLambdaQueryWrapper = new LambdaQueryWrapper<SysUserGroup>().eq(SysUserGroup::getUserId, userId);
        List<SysUserGroup> userGroups = sysUserGroupMapper.selectList(userGroupLambdaQueryWrapper);
        List<SysProject> projectList = new ArrayList<>();
        userGroups.forEach(userGroup -> projects.forEach(project -> {
            if (StringUtils.equals(userGroup.getSourceId(), project.getId())) {
                if (!projectList.contains(project)) {
                    projectList.add(project);
                }
            }
        }));
        return projectList;
    }

    private UserDTO getUserDTO(String userId) {
        SysUser sysUser = baseMapper.selectById(userId);
        if (Objects.isNull(sysUser)) {
            return null;
        }
        if (StringUtils.equals(sysUser.getStatus(), UserStatus.DISABLED)) {
            throw new IllegalStateException("User is disabled");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(sysUser, userDTO);
        return userDTO;
    }
}
