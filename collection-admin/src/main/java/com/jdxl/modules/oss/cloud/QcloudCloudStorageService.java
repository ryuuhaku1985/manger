package com.jdxl.modules.oss.cloud;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.SysMsgInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
//import com.qcloud.cos.request.UploadFileRequest;
//import com.qcloud.cos.sign.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云存储
 */
@Slf4j
public class QcloudCloudStorageService extends CloudStorageService {
    private COSClient client;

    public QcloudCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
//    	Credentials credentials = new Credentials(config.getQcloudAppId(), config.getQcloudSecretId(),
//                config.getQcloudSecretKey());
//
//
//    	//初始化客户端配置
//        ClientConfig clientConfig = new ClientConfig();
//        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
//        clientConfig.setRegion(config.getQcloudRegion());

    	client = null;
//    	new COSClient(clientConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        return createQrcode(config,path,data);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new BizException(e, SysMsgInfo.OSS0004);
        }
    }
    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return this.uploadSuffix(data, null, suffix);
    }

    @Override
    public String uploadSuffix(byte[] data, String prefix, String suffix) {
        String path = config.getQcloudPrefix();
        if(StringUtils.isNotBlank(prefix)){
            path = config.getQcloudPrefix() + "/" + prefix;
        }
        return upload(data, getPath(path, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return this.uploadSuffix(inputStream, null, suffix);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String prefix, String suffix) {
        String path = config.getQcloudPrefix();
        if(StringUtils.isNotBlank(prefix)){
            path = config.getQcloudPrefix() + "/" + prefix;
        }
        return upload(inputStream, getPath(path, suffix));
    }

    /**
     * 上传腾讯云
     * @param config
     * @param key
     * @param data
     * @return
     */
    public static String createQrcode(CloudStorageConfig config, String key,byte[] data) {
        COSCredentials cred = new BasicCOSCredentials(config.getQcloudSecretId(), config.getQcloudSecretKey());
        Region region = new Region(config.getQcloudRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 指定要上传到的存储桶
        String bucketName = config.getQcloudBucketName();
        String url = null;

        try {
            InputStream os = new ByteArrayInputStream(data);
            // 指定要上传到 COS 上对象键
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            objectMetadata.setContentLength(data.length);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, os, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            if (!Strings.isNullOrEmpty(putObjectResult.getETag())) {
                url = config.getQcloudDomain() + key;
            }
        } catch (Exception e) {
            log.error("[Qcloud createQrcode] : 上传文件出错{}", e);
        } finally {
            cosClient.shutdown();
        }
        return url;
    }

}
