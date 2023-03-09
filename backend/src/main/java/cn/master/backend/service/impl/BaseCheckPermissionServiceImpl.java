package cn.master.backend.service.impl;

import cn.master.backend.entity.SysProject;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.SysProjectMapper;
import cn.master.backend.service.BaseCheckPermissionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author create by 11's papa on 2023/1/4-12:26
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseCheckPermissionServiceImpl implements BaseCheckPermissionService {
    final SysProjectMapper sysProjectMapper;

    @Override
    public void checkProjectBelongToWorkspace(String projectId, String workspaceId) {
        SysProject project = sysProjectMapper.selectById(projectId);
        if (Objects.isNull(project) || !StringUtils.equals(project.getWorkspaceId(), workspaceId)) {
            throw new CustomException("check_owner_project");
        }
    }
}
