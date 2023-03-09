package cn.master.backend.service;

import cn.master.backend.entity.SysWorkspace;
import cn.master.backend.entity.WorkspaceResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
public interface SysWorkspaceService extends IService<SysWorkspace> {

    /**
     * desc: 查询列表数据
     *
     * @param workspace   查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysWorkspace>
     */
    IPage<SysWorkspace> getAllWorkspaceList(SysWorkspace workspace, Page<SysWorkspace> producePage);

    /**
     * desc: 添加workspace数据
     *
     * @param workspace          参数
     * @param httpServletRequest HttpServletRequest
     * @return cn.master.backend.entity.SysWorkspace
     */
    SysWorkspace addWorkspaceByAdmin(SysWorkspace workspace, HttpServletRequest httpServletRequest);

    /**
     * desc: 更新workspace数据
     *
     * @param workspace 参数
     * @return cn.master.backend.entity.SysWorkspace
     */
    SysWorkspace updateWorkspaceByAdmin(SysWorkspace workspace);

    /**
     * desc: 删除
     *
     * @param workspaceId workspaceId
     * @return java.lang.String
     */
    String deleteWorkspaceById(String workspaceId);

    List<SysWorkspace> getWorkspaceList(SysWorkspace workspace);

    WorkspaceResource listResource(String groupId, String type);

    /**
     * desc: 根据用户获取对应的workspace数据
     *
     * @param httpServletRequest httpServletRequest
     * @return java.util.List<cn.master.backend.entity.SysWorkspace>
     */
    List<SysWorkspace> getWorkspaceListByUserId(HttpServletRequest httpServletRequest);

    long getWorkspaceSize();

    void batchDeleteWorkspace(HttpServletRequest httpServletRequest, List<String> workspaceIds);
}
