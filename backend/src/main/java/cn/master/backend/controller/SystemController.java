package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SystemStatisticData;
import cn.master.backend.service.SysProjectService;
import cn.master.backend.service.SysUserService;
import cn.master.backend.service.SysWorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by 11's papa on 2023/2/21-13:59
 */
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

    final SysUserService sysUserService;
    final SysWorkspaceService sysWorkspaceService;
    final SysProjectService sysProjectService;

    @GetMapping("/statistics/data")
    public ResponseInfo<SystemStatisticData> getStatisticsData() {
        SystemStatisticData systemStatisticData = new SystemStatisticData();
        systemStatisticData.setUserSize(sysUserService.getUserSize());
        systemStatisticData.setWorkspaceSize(sysWorkspaceService.getWorkspaceSize());
        systemStatisticData.setProjectSize(sysProjectService.getProjectSize());
        return ResponseInfo.success(systemStatisticData);
    }
}
