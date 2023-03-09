package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysWorkspace;
import cn.master.backend.entity.WorkspaceResource;
import cn.master.backend.service.SysUserService;
import cn.master.backend.service.SysWorkspaceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@RestController
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class SysWorkspaceController {
    final SysWorkspaceService service;
    final SysUserService sysUserService;

    @PostMapping("list/all/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getAllWorkspaceList(@RequestBody SysWorkspace request, @PathVariable long page, @PathVariable long limit) {
        Page<SysWorkspace> producePage = new Page<>(page, limit);
        IPage<SysWorkspace> pageInfo = service.getAllWorkspaceList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("special/add")
    public ResponseInfo<SysWorkspace> addWorkspaceByAdmin(HttpServletRequest httpServletRequest, @RequestBody SysWorkspace workspace) {
        return ResponseInfo.success(service.addWorkspaceByAdmin(workspace, httpServletRequest));
    }

    @PostMapping("special/update")
    public ResponseInfo<SysWorkspace> updateWorkspace(@RequestBody SysWorkspace workspace) {
        return ResponseInfo.success(service.updateWorkspaceByAdmin(workspace));
    }

    @GetMapping("special/delete/{workspaceId}")
    public ResponseInfo<String> deleteWorkspaceById(HttpServletRequest httpServletRequest, @PathVariable String workspaceId) {
        sysUserService.refreshSessionUser(httpServletRequest, "workspace", workspaceId);
        return ResponseInfo.success(service.deleteWorkspaceById(workspaceId));
    }

    @PostMapping("special/delete")
    public ResponseInfo<String> batchDeleteWorkspace(HttpServletRequest httpServletRequest,@RequestBody List<String> workspaceIds) {
        service.batchDeleteWorkspace(httpServletRequest, workspaceIds);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    public ResponseInfo<List<SysWorkspace>> getWorkspaceList() {
        return ResponseInfo.success(service.getWorkspaceList(new SysWorkspace()));
    }

    @GetMapping("/list/userworkspace")
    public ResponseInfo<List<SysWorkspace>> getWorkspaceListByUserId(HttpServletRequest httpServletRequest) {
        return ResponseInfo.success(service.getWorkspaceListByUserId(httpServletRequest));
    }

    @GetMapping("/list/resource/{groupId}/{type}")
    public ResponseInfo<WorkspaceResource> listResource(@PathVariable String groupId, @PathVariable String type) {
        return ResponseInfo.success(service.listResource(groupId, type));
    }
}
