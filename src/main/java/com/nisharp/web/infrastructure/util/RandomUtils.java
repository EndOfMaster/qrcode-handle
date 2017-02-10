package com.nisharp.web.infrastructure.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author ZM.Wang
 */
public class RandomUtils {

    public static String getRandomNum(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public static String getRandomStr(int conut){
        return RandomStringUtils.randomAlphanumeric(conut);
    }

}
