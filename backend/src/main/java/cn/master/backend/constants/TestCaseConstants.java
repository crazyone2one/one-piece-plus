package cn.master.backend.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author create by 11's papa on 2023/1/11-11:02
 */
public class TestCaseConstants {
    public static final int MAX_NODE_DEPTH = 8;
    /** *类型枚举*/
    public enum Type {
        Functional("functional"), Performance("performance"), Aapi("api");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static List<String> getValues() {
            List<Type> types = Arrays.asList(Type.values());
            return types.stream().map(Type::getValue).collect(Collectors.toList());
        }
    }

    /** *方法枚举*/
    public enum Method {
        Manual("manual"), Auto("auto");

        private String value;

        Method(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static List<String> getValues() {
            List<Method> types = Arrays.asList(Method.values());
            return types.stream().map(Method::getValue).collect(Collectors.toList());
        }
    }

    /**
     * 测试步骤模式
     */
    public enum StepModel {
        TEXT, STEP
    }
}
