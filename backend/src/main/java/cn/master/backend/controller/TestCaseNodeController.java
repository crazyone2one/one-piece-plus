package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import cn.master.backend.request.DragNodeRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import cn.master.backend.service.TestCaseNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
@RestController
@RequestMapping("/case/node")
@RequiredArgsConstructor
public class TestCaseNodeController {
    final TestCaseNodeService service;

    @GetMapping("/list/{projectId}")
    public ResponseInfo<List<TestCaseNodeDTO>> getNodeByProjectId(@PathVariable String projectId) {
        return ResponseInfo.success(service.getNodeTreeByProjectId(projectId));
    }

    @PostMapping("/list/{projectId}")
    public ResponseInfo<List<TestCaseNodeDTO>> getNodeByCondition(@PathVariable String projectId, @RequestBody(required = false) QueryTestCaseRequest request) {
        return ResponseInfo.success(service.getNodeTreeByProjectId(projectId, Optional.ofNullable(request).orElse(new QueryTestCaseRequest())));
    }

    @PostMapping("/add")
    public ResponseInfo<String> addNode(@RequestBody TestCaseNode node) {
        return ResponseInfo.success(service.addNode(node));
    }

    @PostMapping("/edit")
    public ResponseInfo<String> editNode(@RequestBody DragNodeRequest node) {
        return ResponseInfo.success(service.editNode(node));
    }

    @PostMapping("/list/plan/{planId}")
    public ResponseInfo<List<TestCaseNodeDTO>> getNodeByPlanId(@PathVariable String planId, @RequestBody(required = false) QueryTestPlanCaseRequest request) {
        return ResponseInfo.success(service.getNodeByPlanId(planId, Optional.ofNullable(request).orElse(new QueryTestPlanCaseRequest())));
    }

    @PostMapping("/list/plan/relate")
    public ResponseInfo<List<TestCaseNodeDTO>> getRelatePlanNodes(@RequestBody QueryTestCaseRequest request) {
        return ResponseInfo.success(service.getRelatePlanNodes(request));
    }
}
