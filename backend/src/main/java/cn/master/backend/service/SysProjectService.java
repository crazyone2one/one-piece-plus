package cn.master.backend.service;

import cn.master.backend.entity.SysProject;
import cn.master.backend.request.AddProjectRequest;
import cn.master.backend.request.ProjectRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-04
 */
public interface SysProjectService extends IService<SysProject> {

    /**
     * desc: 分页查询
     *
     * @param request     查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysProject>
     */
    IPage<SysProject> getProjectList(ProjectRequest request, Page<SysProject> producePage);

    /**
     * desc: 添加
     *
     * @param httpServletRequest httpServletRequest
     * @param project            参数
     * @return cn.master.backend.entity.SysProject
     */
    SysProject addProject(HttpServletRequest httpServletRequest, AddProjectRequest project);

    /**
     * desc: update project
     *
     * @param project 参数
     * @return cn.master.backend.entity.SysProject
     */
    SysProject updateProject(AddProjectRequest project);

    /**
     * desc: 删除
     *
     * @param projectId project id
     * @return java.lang.String
     */
    String deleteProject(String projectId);

    /**
     * desc: 根据测试用例模板id查询
     *
     * @param templateId test case template id
     * @return java.util.List<cn.master.backend.entity.SysProject>
     */
    List<SysProject> getByCaseTemplateId(String templateId);

    /**
     * desc: search project by user
     *
     * @param request parameter
     * @return java.util.List<cn.master.backend.entity.SysProject>
     */
    List<SysProject> getUserProject(ProjectRequest request);

    /**
     * desc: get project by id
     *
     * @param ids project ids
     * @return java.util.List<cn.master.backend.entity.SysProject>
     */
    List<SysProject> getProjectByIds(List<String> ids);

    /**
     * desc: get project list
     *
     * @param request parameter
     * @return java.util.List<cn.master.backend.entity.SysProject>
     */
    List<SysProject> getProjectList(ProjectRequest request);
    long getProjectSize();
}
