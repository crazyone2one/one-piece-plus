package cn.master.backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author create by 11's papa on 2023/1/29-11:24
 */
public class CommonUtils {
    public static double getPercentWithDecimal(double value) {
        return new BigDecimal(value * 100)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
