package com.jdxl.modules.utils;

import com.google.common.base.Strings;
import com.jdxl.modules.utils.vo.AlarmMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class ExceptionNoticeFormatBuilder {

    public static String getDingTalkMdFormatMsg(String dingTalkMdFormat, AlarmMessage alarmMessage) {
        // 替换 <title> 和 <text>
        String errorWhy = Strings.isNullOrEmpty(alarmMessage.getErrorWhy()) ?
                alarmMessage.getE().getMessage() : alarmMessage.getErrorWhy();
        String title = Strings.isNullOrEmpty(alarmMessage.getTitle()) ?
                alarmMessage.getErrorWhat().getMessage() : alarmMessage.getTitle();
        String hostAddress="";
        try {
            hostAddress= InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("** title：【").append(hostAddress).append("】").append(title).append(" **<br/>\n\n");
        stringBuilder.append(" **时间：** ").append(alarmMessage.getErrorWhen()).append("<br/>\n\n");
        stringBuilder.append(" **位置：** <br/>\n\n").append(alarmMessage.getErrorWhere()).append("<br/>\n\n");
        stringBuilder.append(" **原因：** <br/>\n\n").append(errorWhy).append("<br/>\n\n");
        if (!Strings.isNullOrEmpty(alarmMessage.getErrorHow())) {
            stringBuilder.append(" **扩展信息：** <br/>\n\n").append(alarmMessage.getErrorHow()).append("<br/>\n\n");
        }


        dingTalkMdFormat = dingTalkMdFormat.replaceAll("<title>", title);
        dingTalkMdFormat = dingTalkMdFormat.replaceAll("<text>", stringBuilder.toString());
        if (StringUtils.isNotBlank(alarmMessage.getAtMobiles())) {
            dingTalkMdFormat = dingTalkMdFormat.replaceAll("<atMobiles>", alarmMessage.getAtMobiles());
        }
        if (StringUtils.isNotBlank(alarmMessage.getIsAtAll())) {
            dingTalkMdFormat = dingTalkMdFormat.replaceAll("<isAtAll>", alarmMessage.getIsAtAll());
        }
        return dingTalkMdFormat;
    }

}
