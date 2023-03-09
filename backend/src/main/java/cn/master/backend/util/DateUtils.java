package cn.master.backend.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/18-10:33
 */
@Slf4j
public class DateUtils {
    public static final String DATE_PATTERM = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date getDate(String dateString) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERM);
        return dateFormat.parse(dateString);
    }

    public static Date getTime(String timeString) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        return dateFormat.parse(timeString);
    }

    /**
     * 获取入参日期所在周的周一周末日期。 日期对应的时间为当日的零点
     *
     * @return Map<String, String>(2); key取值范围：firstTime/lastTime
     */
    public static Map<String, Date> getWeedFirstTimeAndLastTime(Date date) {
        Map<String, Date> returnMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayWeek == 1) {
                dayWeek = 8;
            }
            calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayWeek);
            //第一天的时分秒是 00:00:00 这里直接取日期，默认就是零点零分
            Date thisWeekFirstTime = getDate(getDateString(calendar.getTime()));

            //计算七天过后的日期
            calendar.add(Calendar.DATE, 7);
            Date nextWeekFirstDay = getDate(getDateString(calendar.getTime()));
            Date thisWeekLastTime = getTime(getTimeString(nextWeekFirstDay.getTime() - 1));

            returnMap.put("firstTime", thisWeekFirstTime);
            returnMap.put("lastTime", thisWeekLastTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return returnMap;

    }

    public static String getDateString(Date date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERM);
        return dateFormat.format(date);
    }

    public static String getTimeString(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN);
        return dateFormat.format(timeStamp);
    }
}
