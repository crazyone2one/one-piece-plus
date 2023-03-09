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
 * @since 2023-01-28
 */
@Getter
@Setter
@TableName("test_plan_principal")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestPlanPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("test_plan_id")
    private String testPlanId;

    @TableField("principal_id")
    private String principalId;
}
