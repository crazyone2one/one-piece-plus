package cn.master.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author create by 11's papa on 2023/1/11-11:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModuleNode extends TestCaseNode{
    private Integer caseNum;
    private String protocol;
    private String modulePath;
    private String scenarioType;
}
