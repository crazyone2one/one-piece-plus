package cn.master.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/24-15:14
 */
@Setter
@Getter
public class TestPlanDTO extends TestPlan{
    private String projectName;
    private String userName;
    private List<String> projectIds;
    private List<String> principals;
    private List<String> follows;
    /**
     * 定时任务ID
     */
    private String scheduleId;
    /**
     * 定时任务是否开启
     */
    private boolean scheduleOpen;
    /**
     * 定时任务状态
     */
    private String scheduleStatus;
    /**
     * 定时任务规则
     */
    private String scheduleCorn;
    /**
     * 定时任务下一次执行时间
     */
    private Long scheduleExecuteTime;
}
