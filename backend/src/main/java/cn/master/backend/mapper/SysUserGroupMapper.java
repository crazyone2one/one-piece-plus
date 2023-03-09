package cn.master.backend.mapper;

import cn.master.backend.entity.RelatedSource;
import cn.master.backend.entity.SysGroup;
import cn.master.backend.entity.SysUserGroup;
import cn.master.backend.entity.UserGroupDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@Mapper
public interface SysUserGroupMapper extends BaseMapper<SysUserGroup> {
    List<RelatedSource> getRelatedSource(@Param("userId") String userId);

    List<SysGroup> getWorkspaceMemberGroups(@Param("workspaceId") String workspaceId, @Param("userId") String userId);

    List<UserGroupDTO> getUserGroup(@Param("userId") String userId, @Param("projectId") String projectId);

    /**
     * desc: 查询用户权限
     *
     * @param userId user id
     * @return java.util.List<java.lang.String>
     */
    @Select("select distinct group_id from sys_user_group where user_id=#{userId}")
    List<String> getUserGroupByUserId(@Param("userId") String userId);
}
