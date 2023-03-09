package cn.master.backend.mapper;

import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import cn.master.backend.request.QueryTestCaseRequest;
import cn.master.backend.request.QueryTestPlanCaseRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
public interface TestCaseNodeMapper extends BaseMapper<TestCaseNode> {
    List<TestCaseNodeDTO> getCountNodes(@Param("request") QueryTestCaseRequest request);

    /**
     * desc: 根据项目id查询对应的节点数据
     *
     * @param projectId project id
     * @return java.util.List<cn.master.backend.entity.TestCaseNodeDTO>
     */
    @Select("select id, id as `key`, project_id, `name`, `name` as label, parent_id, `level`, create_time, update_time, pos, create_user from test_case_node where test_case_node.project_id = #{projectId} order by pos asc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "parent_id", property = "parentId"),
            @Result(column = "parent_id", property = "parentName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.TestCaseNodeMapper.getNameById")),
    })
    List<TestCaseNodeDTO> getNodeTreeByProjectId(@Param("projectId") String projectId);

    List<TestCaseNodeDTO> getTestPlanCountNodes(@Param("request") QueryTestPlanCaseRequest request);

    List<TestCaseNodeDTO> getNodeTreeByProjectIds(@Param("projectIds") List<String> projectIds);

    List<TestCaseNodeDTO> getTestPlanRelateCountNodes(@Param("request") QueryTestCaseRequest request);

    @Select("select `name` from test_case_node where id=#{nodeId}")
    String getNameById(String nodeId);
}
