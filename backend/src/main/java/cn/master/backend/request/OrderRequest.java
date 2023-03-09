package cn.master.backend.request;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author create by 11's papa on 2023/1/8-12:56
 */
@Setter
public class OrderRequest {
    private String name;
    private String type;
    /**
     * 表前缀
     */
    private String prefix;
    private static Pattern p = Pattern.compile("^[\\w|\\-]+$");

    public String getName() {
        if (checkSqlInjection(name)) {
            return "1";
        }
        return name;
    }

    public String getType() {
        if (StringUtils.equalsIgnoreCase(type, "asc")) {
            return "ASC";
        } else {
            return "DESC";
        }
    }

    public String getPrefix() {
        if (checkSqlInjection(prefix)) {
            return StringUtils.EMPTY;
        }
        return prefix;
    }

    private boolean checkSqlInjection(String script) {
        if (StringUtils.isEmpty(script)) {
            return false;
        }
        Matcher matcher = p.matcher(script.toLowerCase());
        return !matcher.find();
    }
}
