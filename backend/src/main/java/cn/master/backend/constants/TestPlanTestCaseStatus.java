package cn.master.backend.constants;

/**
 * @author create by 11's papa on 2023/1/29-11:20
 */
public enum TestPlanTestCaseStatus {
    Prepare("未开始"),
    Pass("通过"),
    Failure("失败"),
    Blocking("阻塞"),
    Skip("跳过"),
    Underway("未开始");
    private String temp;
    TestPlanTestCaseStatus(String status) {
        this.temp = status;
    }
}
