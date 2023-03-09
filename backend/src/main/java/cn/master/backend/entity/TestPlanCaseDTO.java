package cn.master.backend.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author create by 11's papa on 2023/1/31-11:17
 */
@Getter
@Setter
public class TestPlanCaseDTO extends TestCase {
    private String executor;
    private String executorName;
    private String results;
    private String planId;
    private String planName;
    private String caseId;
    private String issues;
    private String reportId;
    private String model;
    private String projectName;
    private String actualResult;
    private String maintainerName;
    private Boolean isCustomNum;
    private Integer issuesCount;
    private String versionName;
    private String creatorName;

}
