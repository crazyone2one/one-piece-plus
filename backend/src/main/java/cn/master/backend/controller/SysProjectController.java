package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.SysWorkspace;
import cn.master.backend.request.AddProjectRequest;
import cn.master.backend.request.ProjectRequest;
import cn.master.backend.service.SysProjectService;
import cn.master.backend.util.SessionUtils;
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
 * @since 2023-01-04
 */
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class SysProjectController {
    final SysProjectService service;

    @PostMapping("list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getProjectList(@RequestBody ProjectRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<SysProject> producePage = new Page<>(page, limit);
        IPage<SysProject> pageInfo = service.getProjectList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("/add")
    public ResponseInfo<SysProject> addProject(HttpServletRequest httpServletRequest, @RequestBody AddProjectRequest project) {
        SysProject sysProject = service.addProject(httpServletRequest, project);
        return ResponseInfo.success(sysProject);
    }

    @PostMapping("/update")
    public ResponseInfo<SysProject> updateProject(@RequestBody AddProjectRequest project) {
        SysProject sysProject = service.updateProject(project);
        return ResponseInfo.success(sysProject);
    }

    @GetMapping("/delete/{projectId}")
    public ResponseInfo<String> deleteProject(@PathVariable String projectId) {
        return ResponseInfo.success(service.deleteProject(projectId));
    }

    @PostMapping("/list/related")
    public ResponseInfo<List<SysProject>> getUserProject(@RequestBody ProjectRequest request) {
        return ResponseInfo.success(service.getUserProject(request));
    }

    @GetMapping("/list/all")
    public ResponseInfo<List<SysProject>> listAll() {
        String currentWorkspaceId = SessionUtils.getCurrentWorkspaceId();
        ProjectRequest request = new ProjectRequest();
        request.setWorkspaceId(currentWorkspaceId);
        return ResponseInfo.success(service.getProjectList(request));
    }

    @GetMapping("/get/{projectId}")
    public ResponseInfo<SysProject> getProject(@PathVariable String projectId) {
        return ResponseInfo.success(service.getById(projectId));
    }
}
