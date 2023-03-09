package cn.master.backend.mapper;

import cn.master.backend.entity.SysWorkspace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@Mapper
public interface SysWorkspaceMapper extends BaseMapper<SysWorkspace> {

    /**
     * desc: search name by workspace id
     *
     * @param workspaceId id of the workspace
     * @return java.lang.String
     */
    @Select("select name from sys_workspace where id=#{workspaceId}")
    String getNameById(String workspaceId);
}
