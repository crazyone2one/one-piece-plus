package cn.master.backend.service.impl;

import cn.master.backend.constants.UserGroupType;
import cn.master.backend.entity.SysGroup;
import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.SysUserGroup;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.SysGroupMapper;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.EditGroupUserRequest;
import cn.master.backend.service.SysUserGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SysUserGroupServiceImpl extends ServiceImpl<SysUserGroupMapper, SysUserGroup> implements SysUserGroupService {

    final SysUserMapper sysUserMapper;
    final SysGroupMapper sysGroupMapper;

    @Override
    public IPage<SysUser> getGroupUser(EditGroupRequest request, Page<SysUser> producePage) {
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<SysUserGroup>()
                .eq(SysUserGroup::getGroupId, request.getUserGroupId())
                .eq(request.isOnlyQueryCurrentProject(), SysUserGroup::getSourceId, request.getProjectId());
        List<SysUserGroup> sysUserGroups = baseMapper.selectList(wrapper);
        List<String> userIds = sysUserGroups.stream().map(SysUserGroup::getUserId).collect(Collectors.toList());
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.isNoneBlank(request.getName()), SysUser::getName, request.getName())
                .in(SysUser::getId, userIds);
        return sysUserMapper.selectPage(producePage, userLambdaQueryWrapper);
    }

    @Override
    public int removeGroupMember(String userId, String groupId) {
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<SysUserGroup>()
                .eq(SysUserGroup::getGroupId, groupId).eq(SysUserGroup::getUserId, userId);
        return baseMapper.delete(wrapper);
    }

    @Override
    public int addGroupUser(EditGroupUserRequest request) {
        SysGroup group = sysGroupMapper.selectById(request.getGroupId());
        if (Objects.isNull(group)) {
            throw new CustomException("add group user warning, group is null. group id: " + request.getGroupId());
        }
        addNotSystemGroupUser(group, request.getUserIds(), request.getSourceIds());
        return 0;
    }

    private void addNotSystemGroupUser(SysGroup group, List<String> userIds, List<String> sourceIds) {
        for (String userId : userIds) {
            SysUser sysUser = sysUserMapper.selectById(userId);
            if (Objects.isNull(sysUser)) {
                continue;
            }
            LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<SysUserGroup>()
                    .eq(SysUserGroup::getGroupId, group.getId()).eq(SysUserGroup::getUserId, userId);
            List<SysUserGroup> userGroups = baseMapper.selectList(wrapper);
            List<String> existSourceIds = userGroups.stream().map(SysUserGroup::getSourceId).collect(Collectors.toList());
            List<String> toAddSourceIds = new ArrayList<>(sourceIds);
            toAddSourceIds.removeAll(existSourceIds);
            for (String sourceId : toAddSourceIds) {
                SysUserGroup build = SysUserGroup.builder().userId(userId)
                        .sourceId(Objects.equals(group.getType(), UserGroupType.SYSTEM) ? "system" : sourceId)
                        .groupId(group.getId()).build();
                baseMapper.insert(build);
            }
        }
    }
}
