package com.nisharp.web.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author ZM.Wang
 */
public class RandomUtils {

    public static String getRandomStr(int count) {
        return RandomStringUtils.randomNumeric(count);
    }
}
