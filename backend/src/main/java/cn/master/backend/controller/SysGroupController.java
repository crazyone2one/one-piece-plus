package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysGroup;
import cn.master.backend.entity.SysUser;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.GroupRequest;
import cn.master.backend.service.SysGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-01
 */
@RestController
@RequestMapping("/user/group")
@RequiredArgsConstructor
public class SysGroupController {
    final SysGroupService service;

    @PostMapping("get/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getGroupList(HttpServletRequest httpServletRequest, @RequestBody EditGroupRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<SysGroup> producePage = new Page<>(page, limit);
        IPage<SysGroup> pageInfo = service.getGroupList(request, producePage, httpServletRequest);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", Objects.isNull(pageInfo) ? new ArrayList<>() : pageInfo.getTotal());
        result.put("records", Objects.isNull(pageInfo) ? 0 : pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("/add")
    public ResponseInfo<SysGroup> addGroup(HttpServletRequest httpServletRequest, @RequestBody EditGroupRequest request) {
        return ResponseInfo.success(service.addGroup(httpServletRequest, request));
    }

    @PostMapping("/get")
    public ResponseInfo<List<SysGroup>> getGroupByType(@RequestBody EditGroupRequest request) {
        return ResponseInfo.success(service.getGroupByType(request));
    }

    @GetMapping("/all/{userId}")
    public ResponseInfo<List<Map<String, Object>>> getAllUserGroup(@PathVariable String userId) {
        return ResponseInfo.success(service.getAllUserGroup(userId));
    }

    @GetMapping("/list/ws/{workspaceId}/{userId}")
    public ResponseInfo<List<SysGroup>> getWorkspaceMemberGroups(@PathVariable String workspaceId, @PathVariable String userId) {
        return ResponseInfo.success(service.getWorkspaceMemberGroups(workspaceId, userId));
    }

    @PostMapping("/list")
    public ResponseInfo<List<SysGroup>> getGroupsByType(@RequestBody GroupRequest request) {
        return ResponseInfo.success(service.getGroupsByType(request));
    }
}
