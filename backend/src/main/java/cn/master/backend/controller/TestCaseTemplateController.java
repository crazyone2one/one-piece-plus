package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.TestCaseTemplate;
import cn.master.backend.request.BaseQueryRequest;
import cn.master.backend.request.UpdateCaseFieldTemplateRequest;
import cn.master.backend.service.TestCaseTemplateService;
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
 * @since 2023-01-08
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/field/template/case")
public class TestCaseTemplateController {
    final TestCaseTemplateService service;

    @PostMapping("list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> list(@RequestBody BaseQueryRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<TestCaseTemplate> producePage = new Page<>(page, limit);
        IPage<TestCaseTemplate> pageInfo = service.getPageList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("/add")
    public ResponseInfo<String> add(@RequestBody UpdateCaseFieldTemplateRequest request) {
        return ResponseInfo.success(service.addTemplate(request));
    }

    @GetMapping("/delete/{id}")
    public ResponseInfo<String> delete(@PathVariable String id) {
        return ResponseInfo.success(service.deleteTemplate(id));
    }

    @GetMapping("/get/relate/{projectId}")
    public ResponseInfo<TestCaseTemplate> getTemplate(@PathVariable String projectId) {
        return ResponseInfo.success(service.getTemplate(projectId));
    }

    @PostMapping({"list/all/{projectId}","list/all"})
    public ResponseInfo<List<TestCaseTemplate>> getTemplateList(@PathVariable(value = "projectId",required = false) String projectId) {
        return ResponseInfo.success(service.getTemplateList(projectId));
    }
}
