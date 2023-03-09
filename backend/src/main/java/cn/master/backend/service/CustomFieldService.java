package cn.master.backend.service;

import cn.master.backend.entity.CustomField;
import cn.master.backend.request.QueryCustomFieldRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-09
 */
public interface CustomFieldService extends IService<CustomField> {

    /**
     * desc: add custom field
     *
     * @param customField custom field parameter
     * @return java.lang.String
     */
    String addCustomField(CustomField customField);

    /**
     * desc: 分页查询数据
     *
     * @param request     查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.CustomField>
     */
    IPage<CustomField> getPageList(QueryCustomFieldRequest request, Page<CustomField> producePage);

    /**
     * desc: 删除
     *
     * @param id custom field id
     * @return java.lang.String
     */
    String deleteCustomField(String id);

    /**
     * desc: default field
     *
     * @param request parameter
     * @return java.util.List<cn.master.backend.entity.CustomField>
     */
    List<CustomField> getDefaultField(QueryCustomFieldRequest request);

    /**
     * desc: get field by project id and field name
     *
     * @param projectId project id
     * @param fieldName field name
     * @return cn.master.backend.entity.CustomField
     */
    CustomField getCustomFieldByName(String projectId, String fieldName);

    IPage<CustomField> listRelate(QueryCustomFieldRequest request, Page<CustomField> producePage);

    List<CustomField> getDataList(QueryCustomFieldRequest request);
}
