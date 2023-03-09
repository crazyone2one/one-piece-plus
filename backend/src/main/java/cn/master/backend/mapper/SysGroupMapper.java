package cn.master.backend.mapper;

import cn.master.backend.entity.SysGroup;
import cn.master.backend.request.EditGroupRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-01
 */
@Mapper
public interface SysGroupMapper extends BaseMapper<SysGroup> {

    IPage<SysGroup> getGroupList(IPage<?> page, @Param("request") EditGroupRequest request);
}
