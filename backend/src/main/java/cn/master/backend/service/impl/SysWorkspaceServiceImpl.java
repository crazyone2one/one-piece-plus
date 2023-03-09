package cn.master.backend.service.impl;

import cn.master.backend.constants.UserGroupConstants;
import cn.master.backend.constants.UserGroupType;
import cn.master.backend.entity.*;
import cn.master.backend.mapper.SysGroupMapper;
import cn.master.backend.mapper.SysProjectMapper;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.mapper.SysWorkspaceMapper;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.service.SysWorkspaceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysWorkspaceServiceImpl extends ServiceImpl<SysWorkspaceMapper, SysWorkspace> implements SysWorkspaceService {
    final JwtUtils jwtUtils;
    final SysUserGroupMapper userGroupMapper;
    final SysGroupMapper sysGroupMapper;
    final SysProjectMapper sysProjectMapper;
    private static final String GLOBAL = "global";

    @Override
    public IPage<SysWorkspace> getAllWorkspaceList(SysWorkspace workspace, Page<SysWorkspace> producePage) {
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(workspace.getName()), SysWorkspace::getName, workspace.getName());
        wrapper.orderByDesc(SysWorkspace::getUpdateTime);
        return baseMapper.selectPage(producePage, wrapper);
    }

    @Override
    public SysWorkspace addWorkspaceByAdmin(SysWorkspace workspace, HttpServletRequest httpServletRequest) {
        checkWorkspace(workspace);
        String token = jwtUtils.getJwtTokenFromRequest(httpServletRequest);
        Claims claims = jwtUtils.extractAllClaims(token);
        workspace.setCreateUser((String) claims.get("id"));
        baseMapper.insert(workspace);

        // 创建工作空间为当前用户添加用户组
        SysUserGroup userGroup = SysUserGroup.builder().userId((String) claims.get("id")).sourceId(workspace.getId()).groupId(UserGroupConstants.WS_ADMIN).build();
        userGroupMapper.insert(userGroup);
        return workspace;
    }

    @Override
    public SysWorkspace updateWorkspaceByAdmin(SysWorkspace workspace) {
        checkWorkspace(workspace);
        baseMapper.updateById(workspace);
        return workspace;
    }

    @Override
    public String deleteWorkspaceById(String workspaceId) {
        // delete project
        LambdaQueryWrapper<SysProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<SysProject>().eq(SysProject::getWorkspaceId, workspaceId);
        List<SysProject> projectList = sysProjectMapper.selectList(projectLambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(projectList)) {
            List<String> projectIdList = projectList.stream().map(SysProject::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(projectIdList)) {
                for (String id : projectIdList) {
                    LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SysUserGroup::getSourceId, id);
                    userGroupMapper.delete(wrapper);
                }
            }
            sysProjectMapper.delete(projectLambdaQueryWrapper);
        }

        // delete user group
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserGroup::getSourceId, workspaceId);
        userGroupMapper.delete(wrapper);
        // delete workspace
        baseMapper.deleteById(workspaceId);
        return "删除成功";
    }

    @Override
    public List<SysWorkspace> getWorkspaceList(SysWorkspace workspace) {
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(workspace.getName()), SysWorkspace::getName, workspace.getName());
        wrapper.orderByDesc(SysWorkspace::getUpdateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public WorkspaceResource listResource(String groupId, String type) {
        SysGroup sysGroup = sysGroupMapper.selectById(groupId);
        WorkspaceResource resource = new WorkspaceResource();
        if (Objects.isNull(sysGroup)) {
            return resource;
        }
        if (StringUtils.equals(UserGroupType.WORKSPACE, type)) {
            resource.setWorkspaces(getWorkspaceGroupResource(sysGroup.getScopeId()));
        }
        if (StringUtils.equals(UserGroupType.PROJECT, type)) {
            resource.setProjects(getProjectGroupResource(sysGroup.getScopeId()));
        }
        return resource;
    }

    private List<SysProject> getProjectGroupResource(String scopeId) {
        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.equals(scopeId, GLOBAL)) {
            return sysProjectMapper.selectList(wrapper);
        }
        SysWorkspace workspace = baseMapper.selectById(scopeId);
        if (Objects.nonNull(workspace)) {
            wrapper.eq(SysProject::getWorkspaceId, workspace.getId());
            return sysProjectMapper.selectList(wrapper);
        }
        SysProject project = sysProjectMapper.selectById(scopeId);
        List<SysProject> list = new ArrayList<>();
        if (Objects.nonNull(project)) {
            list.add(project);
        }
        return list;
    }

    @Override
    public List<SysWorkspace> getWorkspaceListByUserId(HttpServletRequest httpServletRequest) {
        String token = jwtUtils.getJwtTokenFromRequest(httpServletRequest);
        Claims claims = jwtUtils.extractAllClaims(token);
        String userId = (String) claims.get("id");
        List<RelatedSource> relatedSource = userGroupMapper.getRelatedSource(userId);
        List<String> wsIds = relatedSource
                .stream()
                .map(RelatedSource::getWorkspaceId)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(wsIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysWorkspace::getId, wsIds);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public long getWorkspaceSize() {
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public void batchDeleteWorkspace(HttpServletRequest httpServletRequest, List<String> workspaceIds) {
        workspaceIds.forEach(this::deleteWorkspaceById);
    }

    private List<SysWorkspace> getWorkspaceGroupResource(String scopeId) {
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtils.equals(scopeId, GLOBAL), SysWorkspace::getId, scopeId);
        return baseMapper.selectList(wrapper);
    }

    private void checkWorkspace(SysWorkspace workspace) {
        if (StringUtils.isBlank(workspace.getName())) {
            throw new IllegalArgumentException("名称不能为空！");
        }
        if (workspace.getName().length() > 100) {
            throw new IllegalArgumentException("名称长度不能超过 100 ！");
        }
        LambdaQueryWrapper<SysWorkspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysWorkspace::getName, workspace.getName());
        wrapper.ne(StringUtils.isNotBlank(workspace.getId()), SysWorkspace::getId, workspace.getId());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new IllegalArgumentException("名称已存在！");
        }
    }
}
