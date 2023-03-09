package cn.master.backend.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author create by 11's papa on 2023/1/24-15:15
 */
@Setter
@Getter
public class TestPlanDtoWithMetric extends TestPlanDTO{
    private Double passRate;
    private Double testRate;
    private Integer passed;
    private Integer tested;
    private Integer total;
    private String createUser;
    private Integer testPlanTestCaseCount;
    private Integer testPlanApiCaseCount;
    private Integer testPlanApiScenarioCount;
    private Integer testPlanUiScenarioCount;
    private Integer testPlanLoadCaseCount;
}
