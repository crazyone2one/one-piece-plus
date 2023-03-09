package cn.master.backend.request;

import cn.master.backend.entity.TestCase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author create by 11's papa on 2023/1/17-15:41
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class EditTestCaseRequest extends TestCase {
    private List<String> follows;
    private List<String> relateFileMetaIds = new ArrayList<>();
    // 取消关联文件应用ID
    private List<String> unRelateFileMetaIds = new ArrayList<>();
}
