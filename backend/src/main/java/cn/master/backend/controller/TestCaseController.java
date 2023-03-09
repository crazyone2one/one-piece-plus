package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseDTO;
import cn.master.backend.request.EditTestCaseRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.service.TestCaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2023-01-17
 */
@RestController
@RequestMapping("/test/case")
@RequiredArgsConstructor
public class TestCaseController {
    final TestCaseService service;

    @PostMapping("list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getProjectList(@RequestBody QueryTestCaseRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestCase> producePage = new Page<>(page, limit);
        IPage<TestCase> pageInfo = service.listTestCase(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("relate/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getTestCaseRelateList(@RequestBody QueryTestCaseRequest request, @PathVariable long page, @PathVariable long limit) {
        IPage<TestCaseDTO> iPage = service.getTestCaseRelateList(request, page, limit);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("/save")
    public ResponseInfo<TestCase> saveTestCase(@RequestBody EditTestCaseRequest request) {
        return ResponseInfo.success(service.addTestCase(request));
    }

    @GetMapping("/get/{testCaseId}")
    public ResponseInfo<TestCase> getTestCase(@PathVariable String testCaseId) {
        return ResponseInfo.success(service.getTestCase(testCaseId));
    }

    @PostMapping(value = "/edit")
    public ResponseInfo<TestCase> editTestCase(@RequestBody EditTestCaseRequest request) {
        return ResponseInfo.success(service.editTestCase(request));
    }

    @GetMapping("versions/{caseId}")
    public ResponseInfo<List<TestCase>> getTestCaseVersions(@PathVariable String caseId) {
        return ResponseInfo.success(service.getTestCaseVersions(caseId));
    }

    @PostMapping("/deleteToGc/{testCaseId}")
    public ResponseInfo<Integer> deleteToGc(@PathVariable String testCaseId) {
        return ResponseInfo.success(service.deleteTestCaseToGc(testCaseId));
    }

    @PostMapping("/reviews/case/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getReviewCase(@RequestBody QueryTestCaseRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestCase> producePage = new Page<>(page, limit);
        IPage<TestCaseDTO> iPage = service.getTestReviewCase(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseInfo.success(result);
    }
}
