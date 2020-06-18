package com.jdxl.modules.utils;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Strings;
import com.jdxl.constant.DingMsgConstant;
import com.jdxl.modules.utils.vo.AlarmMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class SendDingMsgUtils {

    // 发送钉钉告警(有验签)模版类型
    public static void sendDingMsgSecret(String title, String why, String msgUrl, String secret) {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            AlarmMessage alarmMessage = new AlarmMessage();
            alarmMessage.setTitle(title);
            alarmMessage.setErrorWhy(why);
            dingTalkAlarm(alarmMessage, msgUrl
                    +"&timestamp="+timestamp+"&sign="+URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 发送钉钉告警(有验签)文本类型
    public static void sendDingTextSecret(String msg, String msgUrl, String secret) {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            dingTalkText(msg, msgUrl
                    +"&timestamp="+timestamp+"&sign="+URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 发送钉钉告警
    public static void sendDingMsg(String title, String why) {
        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.setTitle(title);
        alarmMessage.setErrorWhy(why);
//        dingTalkAlarm(alarmMessage, DingMsgConstant.DING_MSG_URL_LUCKY_BAOJINGJIANKONG);
    }

    /**
     * 发送钉钉消息
     * @param alarmMessage
     */
    public static void dingTalkAlarm(AlarmMessage alarmMessage, String msgUrl) {


        String dingMsgUrl = msgUrl;

        // 获取 ding talk markdown 模板
        String dingtalkMdFormat = DingMsgConstant.DING_TALK_MD_FORMAT;

        if (Strings.isNullOrEmpty(dingtalkMdFormat)) {
            log.error("钉钉模板无数据. 请相关人员查看。");
            return;
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(dingMsgUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        // 拼接 msg content
        String msg = ExceptionNoticeFormatBuilder.getDingTalkMdFormatMsg(dingtalkMdFormat, alarmMessage);
        log.info("dingTalkAlarm : ding talk send message: ", msg);
        StringEntity se = new StringEntity(msg, "utf-8");
        httppost.setEntity(se);
        try{
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("dingTalkAlarm : dingtalk return result: ", result);
            }
        }catch(IOException e){
            log.warn("异常类型: "+e);
        }
    }

    public static void dingTalkText(String msg, String msgUrl) {
//        String msgUrl = "https://oapi.dingtalk.com/robot/send?access_token=" + token;
        String dingTalk = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"<text>\"}, \"at\": {\"isAtAll\": \"false\"}}";
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(msgUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        // 拼接 msg content
        dingTalk = dingTalk.replace("<text>", msg);
        StringEntity se = new StringEntity(dingTalk, "utf-8");
        httppost.setEntity(se);
        try{
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("dingTalkAlarm : dingtalk return result: ", result);
            }
        }catch(IOException e){
            log.warn("异常类型: "+e);
        }
    }
}
