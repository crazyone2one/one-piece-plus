package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.CustomField;
import cn.master.backend.entity.SysProject;
import cn.master.backend.request.QueryCustomFieldRequest;
import cn.master.backend.service.CustomFieldService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义字段表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/custom/field")
@RequiredArgsConstructor
public class CustomFieldController {
    final CustomFieldService service;

    @PostMapping("/add")
    public ResponseInfo<String> add(@RequestBody CustomField customField) {
        return ResponseInfo.success(service.addCustomField(customField));
    }

    @PostMapping("list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getProjectList(@RequestBody QueryCustomFieldRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<CustomField> producePage = new Page<>(page, limit);
        IPage<CustomField> pageInfo = service.getPageList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("list/relate/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> listRelate(@RequestBody QueryCustomFieldRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<CustomField> producePage = new Page<>(page, limit);
        IPage<CustomField> pageInfo = service.listRelate(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @GetMapping("/delete/{id}")
    public ResponseInfo<String> delete(@PathVariable String id) {
        return ResponseInfo.success(service.deleteCustomField(id));
    }

    @PostMapping("/default")
    public ResponseInfo<List<CustomField>> getDefaultList(@RequestBody QueryCustomFieldRequest request) {
        return ResponseInfo.success(service.getDefaultField(request));
    }

    @PostMapping("/list")
    public ResponseInfo<List<CustomField>> getList(@RequestBody QueryCustomFieldRequest request) {
        return ResponseInfo.success(service.getDataList(request));
    }
}
