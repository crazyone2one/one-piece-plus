package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/2/2-12:32
 */
@Setter
@Getter
public class ReviewRelevanceRequest {
    /**
     * 评审ID
     */
    private String reviewId;

    /**
     * 当选择关联全部用例时把加载条件送到后台，从后台查询
     */

    private QueryTestCaseRequest request;
    /**
     * 具体选择要关联的用例
     */
    private List<String> testCaseIds ;

    private  Boolean checked;
}
