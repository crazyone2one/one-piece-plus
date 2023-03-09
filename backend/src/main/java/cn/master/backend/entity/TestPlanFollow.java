package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-24
 */
@Getter
@Setter
@TableName("test_plan_follow")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestPlanFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("test_plan_id")
    private String testPlanId;

    /**
     * 关注人
     */
    @TableField("follow_id")
    private String followId;
}
