package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.CustomField;
import cn.master.backend.entity.CustomFieldDao;
import cn.master.backend.entity.CustomFieldTemplate;
import cn.master.backend.entity.CustomFieldTemplateDao;
import cn.master.backend.service.CustomFieldTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字段模板关联表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/custom/field/template")
@RequiredArgsConstructor
public class CustomFieldTemplateController {

    final CustomFieldTemplateService service;

    @PostMapping("/list")
    public ResponseInfo<List<CustomFieldTemplateDao>> list(@RequestBody CustomFieldTemplate request) {
        return ResponseInfo.success(service.listTemplate(request));
    }

    @GetMapping("/{id}")
    public ResponseInfo<CustomField> get(@PathVariable String id) {
        return ResponseInfo.success(service.getCustomField(id));
    }

    @GetMapping("/list/{templateId}")
    public ResponseInfo<List<CustomFieldDao>> getCustomFieldByTemplateId(@PathVariable String templateId) {
        return ResponseInfo.success(service.getCustomFieldByTemplateId(templateId));
    }
}
