package com.nisharp.web.util;

import java.util.Date;

/**
 * @author ZM.Wang
 */
public class DateUtils {

    public static int differenceByHour(Date before, Date back) {
        long beforeTime = before.getTime();
        long backTime = back.getTime();
        long difference = backTime - beforeTime;
        return (int) (difference / 1000 / 60 / 60);
    }
}
