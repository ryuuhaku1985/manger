package com.jdxl.common.utils;

import com.jdxl.shardingjdbc.keygen.IPSectionKeyGenerator;

import java.util.Stack;

import static com.jdxl.common.utils.DateUtil.getTimeMillis;


public class BuilderUtil {
    private static IPSectionKeyGenerator iPSectionKeyGenerator =new IPSectionKeyGenerator();

    private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * 生成user id
     * 生成规则：
     * 渠道号 + 子渠道号 + 时间戳（精确到毫秒）+ 8位随机数
     * 3 + 2 + 13 + 8 = 26
     *
     * @param channelId    渠道Id
     * @param subChannelId 子渠道id
     * @return UserId
     */
    public static String buildUserId(String channelId, String subChannelId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(channelId, 0, 2);
        stringBuilder.append(subChannelId, 0, 2);
        stringBuilder.append(getTimeMillis());
        stringBuilder.append(RandomUtil.buildRandomNum(8));

        return stringBuilder.toString();
    }

    public static String buildCommonId(String userId){
        if(iPSectionKeyGenerator == null){
            iPSectionKeyGenerator =new IPSectionKeyGenerator();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(iPSectionKeyGenerator.generateKey());
        stringBuilder.append(userId.substring(userId.length()-2));
        return stringBuilder.toString();
    }

    public static String buildRegisterId(String clientTypeCode){
        if(iPSectionKeyGenerator == null){
            iPSectionKeyGenerator =new IPSectionKeyGenerator();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(iPSectionKeyGenerator.generateKey());
        String result = convertTo_62(Long.valueOf(stringBuilder.toString()), 10);
        result = result + clientTypeCode;
        if (result.length() > 2) {
            result = result.substring(2, result.length());
        }
        return result;
    }

    /**
     * 将10进制转化为62进制
     * @param number
     * @param length 转化成的62进制长度，不足length长度的话高位补0，否则不改变什么
     * @return
     */
    public static String convertTo_62(long number, int length){
        Long rest=number;
        Stack<Character> stack=new Stack<Character>();
        StringBuilder result=new StringBuilder(0);
        while(rest!=0){
            stack.add(charSet[new Long((rest-(rest/62)*62)).intValue()]);
            rest=rest/62;
        }
        for(;!stack.isEmpty();){
            result.append(stack.pop());
        }
        int result_length = result.length();
        StringBuilder temp0 = new StringBuilder();
        for(int i = 0; i < length - result_length; i++){
            temp0.append('0');
        }

        return temp0.toString() + result.toString();
    }

    /**
     * 将62进制转换成10进制数 ，我重新写了这个函数，原先版本有问题
     *
     * @param ident62
     * @return
     */
    public static String convertTo_10( String ident62 ) {
        Long dst = 0L;
        for(int i=0; i<ident62.length(); i++)
        {
            char c = ident62.charAt(i);
            for(int j=0; j<charSet.length; j++)
            {
                if(c == charSet[j])
                {
                    dst = (dst * 62) + j;
                    break;
                }
            }
        }
        String str = String.format( "%08d", dst);
        return str;
    }
}
