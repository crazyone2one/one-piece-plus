package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.TestCase;
import cn.master.backend.entity.TestCaseReview;
import cn.master.backend.request.QueryCaseReviewRequest;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.request.ReviewRelevanceRequest;
import cn.master.backend.service.TestCaseReviewService;
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
 * @since 2023-02-01
 */
@RestController
@RequestMapping("/test/case/review")
@RequiredArgsConstructor
public class TestCaseReviewController {
    final TestCaseReviewService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> list(@RequestBody QueryCaseReviewRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestCaseReview> producePage = new Page<>(page, limit);
        IPage<TestCaseReview> iPage = service.getTestCaseReviewsList(producePage, request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("/save")
    public ResponseInfo<TestCaseReview> saveCaseReview(@RequestBody TestCaseReview testCaseReview) {
        return ResponseInfo.success(service.saveTestCaseReview(testCaseReview));
    }

    @PostMapping("/edit")
    public ResponseInfo<TestCaseReview> editCaseReview(@RequestBody TestCaseReview testCaseReview) {
        return ResponseInfo.success(service.editCaseReview(testCaseReview));
    }

    @GetMapping("/delete/{reviewId}")
    public ResponseInfo<Integer> deleteCaseReview(@PathVariable String reviewId) {
        return ResponseInfo.success(service.deleteCaseReview(reviewId));
    }

    @PostMapping("/list/all")
    public ResponseInfo<List<TestCaseReview>> listAll() {
        return ResponseInfo.success(service.listCaseReviewAll());
    }

    @PostMapping("/relevance")
    public ResponseInfo<String> testReviewRelevance(@RequestBody ReviewRelevanceRequest request) {
        return ResponseInfo.success(service.testReviewRelevance(request));
    }
}
