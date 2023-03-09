package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysUser;
import cn.master.backend.request.EditGroupRequest;
import cn.master.backend.request.EditGroupUserRequest;
import cn.master.backend.service.SysUserGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@RestController
@RequestMapping("/user/group")
@RequiredArgsConstructor
public class SysUserGroupController {
    final SysUserGroupService service;

    @PostMapping("/user/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getGroupUser(@RequestBody EditGroupRequest editGroupRequest, @PathVariable long page, @PathVariable long limit) {
        Map<String, Object> result = new LinkedHashMap<>();
        IPage<SysUser> iPage = service.getGroupUser(editGroupRequest, new Page<>(page, limit));
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseInfo.success(result);
    }

    @GetMapping("/rm/{userId}/{groupId}")
    public ResponseInfo<Integer> removeGroupMember(@PathVariable String userId, @PathVariable String groupId) {
        return ResponseInfo.success(service.removeGroupMember(userId, groupId));
    }

    @PostMapping("/add/member")
    public ResponseInfo<Integer> addGroupUser(@RequestBody EditGroupUserRequest request) {
        return ResponseInfo.success(service.addGroupUser(request));
    }
}
