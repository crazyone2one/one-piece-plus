package cn.master.backend.request;

import cn.master.backend.entity.TestPlan;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/24-15:45
 */
@Getter
@Setter
public class AddTestPlanRequest extends TestPlan {
    private List<String> projectIds;
    private List<String> principals;
    private List<String> follows;
}
