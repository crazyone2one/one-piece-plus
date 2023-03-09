package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/2/1-8:56
 */
@Setter
@Getter
public class PlanCaseRelevanceRequest {
    private String planId;
    private String executor;

    private List<String> ids;

    /**
     * 当选择关联全部用例时把加载条件送到后台，从后台查询
     */
    private QueryTestCaseRequest request;

    /**
     * 具体要关联的用例
     */
    private List<String> testCaseIds = new ArrayList<>();

    /**
     * 是否同步关联功能用例下关联的接口场景性能ui用例
     */
    private Boolean checked;
}
