package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.TestCaseReviewTestCase;
import cn.master.backend.request.QueryCaseReviewRequest;
import cn.master.backend.service.TestCaseReviewTestCaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/test/review/case")
@RequiredArgsConstructor
public class TestCaseReviewTestCaseController {
    final TestCaseReviewTestCaseService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getTestPlanCases(@PathVariable long page, @PathVariable long limit, @RequestBody QueryCaseReviewRequest request) {
        Page<TestCaseReviewTestCase> producePage = new Page<>(page, limit);
        IPage<TestCaseReviewTestCase> iPage = service.getTestCaseReviewCase(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", Objects.nonNull(iPage) ? iPage.getTotal() : 0);
        result.put("records", Objects.nonNull(iPage) ? iPage.getRecords() : new ArrayList<>());
        return ResponseInfo.success(result);
    }
}
