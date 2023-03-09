package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.TestPlan;
import cn.master.backend.entity.TestPlanDtoWithMetric;
import cn.master.backend.request.*;
import cn.master.backend.service.TestPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
@RestController
@RequestMapping("/test/plan")
@RequiredArgsConstructor
public class TestPlanController {
    final TestPlanService service;

    @PostMapping("list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getProjectList(@RequestBody QueryTestPlanRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestPlan> producePage = new Page<>(page, limit);
        List<TestPlanDtoWithMetric> list = service.findList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", list.size());
        result.put("records", list);
        return ResponseInfo.success(result);
    }

    @PostMapping("/dashboard/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> listByWorkspaceId(@RequestBody QueryTestPlanRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestPlan> producePage = new Page<>(page, limit);
        List<TestPlanDtoWithMetric> list = service.findList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", list.size());
        result.put("records", list);
        return ResponseInfo.success(result);
    }

    @PostMapping("/add")
    public ResponseInfo<TestPlan> addTestPlan(@RequestBody AddTestPlanRequest testPlan) {
        return ResponseInfo.success(service.addTestPlan(testPlan));
    }

    @PostMapping("/edit")
    public ResponseInfo<TestPlan> editTestPlan(@RequestBody AddTestPlanRequest testPlanDTO) {
        return ResponseInfo.success(service.editTestPlan(testPlanDTO));
    }

    @PostMapping({"/delete/{testPlanId}", "/delete/batch"})
    public ResponseInfo<Integer> deleteTestPlan(@PathVariable String testPlanId, @RequestBody BatchOperateRequest request) {
        List<String> planIds = new ArrayList<>();
        if (StringUtils.isNotBlank(testPlanId)) {
            planIds.add(testPlanId);
        }
        if (CollectionUtils.isNotEmpty(request.getIds())) {
            planIds.addAll(request.getIds());
        }
        return ResponseInfo.success(service.deleteTestPlan(planIds));
    }

    @GetMapping("get/stage/option/{projectId}")
    public ResponseInfo<List<Map<String, String>>> getStageOption(@PathVariable String projectId) {
        return ResponseInfo.success(service.getStageOption(projectId));
    }

    @GetMapping("/principal/{planId}")
    public ResponseInfo<List<SysUser>> getPlanPrincipal(@PathVariable String planId) {
        return ResponseInfo.success(service.getPlanPrincipal(planId));
    }

    @GetMapping("/follow/{planId}")
    public ResponseInfo<List<SysUser>> getPlanFollow(@PathVariable String planId) {
        return ResponseInfo.success(service.getPlanFollow(planId));
    }

    @PostMapping("/list/all")
    public ResponseInfo<List<TestPlan>> listAll(@RequestBody QueryTestPlanRequest request) {
        return ResponseInfo.success(service.listTestAllPlan(request));
    }

    @PostMapping("/relevance")
    public ResponseInfo<String> testPlanRelevance(@RequestBody PlanCaseRelevanceRequest request) {
        return ResponseInfo.success(service.testPlanRelevance(request));
    }
}
