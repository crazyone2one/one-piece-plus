package cn.master.backend.mapper;

import cn.master.backend.entity.TestCaseTemplate;
import cn.master.backend.request.BaseQueryRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-08
 */
@Mapper
public interface TestCaseTemplateMapper extends BaseMapper<TestCaseTemplate> {

    /**
     * desc: 分页查询数据
     *
     * @param page         分页条件
     * @param queryRequest 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.TestCaseTemplate>
     */
    IPage<TestCaseTemplate> queryPageList(IPage<TestCaseTemplate> page, @Param("request") BaseQueryRequest queryRequest);
}
