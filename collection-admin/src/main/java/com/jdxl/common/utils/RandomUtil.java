package com.jdxl.common.utils;

import java.util.Random;

public class RandomUtil {

    /**
     * 生成随机数
     *
     * @param start 开始的点
     * @param end   结束的点
     * @return int类型的随机数
     */
    public static int buildRandomNum(int start, int end) {
        Random random = new Random();
        return random.nextInt(end) % (end - start + 1) + start;
    }

    /**
     * 生成随机数
     * 目前能生成的随机数的长度最长是16
     *
     * @param randomLong 随机数的长度
     * @return 随机数的字符串
     */
    public static String buildRandomNum(int randomLong) {
        if (randomLong > 10)
            return null;
        int sum = 1;
        for(int m=1;m<randomLong; m++) {
            sum = sum * 10 ;
        }
        return String.valueOf((int)((Math.random()*9+1)*sum));
    }
}
