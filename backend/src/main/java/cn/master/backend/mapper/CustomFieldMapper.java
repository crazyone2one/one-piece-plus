package cn.master.backend.mapper;

import cn.master.backend.entity.CustomField;
import cn.master.backend.request.QueryCustomFieldRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 自定义字段表 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-09
 */
@Mapper
public interface CustomFieldMapper extends BaseMapper<CustomField> {

    /**
     * desc: 分页查询
     *
     * @param producePage 分页条件
     * @param request     查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.CustomField>
     */
    IPage<CustomField> selectPageVO(Page<CustomField> producePage, @Param("request") QueryCustomFieldRequest request);
    IPage<CustomField> listRelate(Page<CustomField> producePage, @Param("request") QueryCustomFieldRequest request);

    List<CustomField> selectDataList(@Param("request") QueryCustomFieldRequest request);
}
