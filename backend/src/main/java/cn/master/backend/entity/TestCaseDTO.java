package cn.master.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/18-10:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestCaseDTO extends TestCase {
    private String maintainerName;
    private String apiName;
    private String lastResultId;
    private String projectName;
    private String createName;
    private String versionName;
    private List<CustomFieldDao> fields;
    private List<String> caseTags;
//    private List<IssuesDao> issueList = new ArrayList<>();
}
