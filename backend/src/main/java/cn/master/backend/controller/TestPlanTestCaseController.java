package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.TestPlanCaseDTO;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import cn.master.backend.service.TestPlanTestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-29
 */
@RestController
@RequestMapping("/test/plan/case")
@RequiredArgsConstructor
public class TestPlanTestCaseController {
    final TestPlanTestCaseService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getTestPlanCases(@RequestBody QueryTestPlanCaseRequest request, @PathVariable long page, @PathVariable long limit) {
        List<TestPlanCaseDTO> list = service.getTestPlanCases(request, page, limit);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", list.size());
        result.put("records", list);
        return ResponseInfo.success(result);
    }
}
