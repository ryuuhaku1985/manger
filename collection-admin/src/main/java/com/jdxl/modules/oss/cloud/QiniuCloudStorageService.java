package com.jdxl.modules.oss.cloud;

import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.SysMsgInfo;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 */
public class QiniuCloudStorageService extends CloudStorageService {
    private UploadManager uploadManager;
    private String token;

    public QiniuCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new BizException(SysMsgInfo.OSS0002, res.toString());
            }
        } catch (Exception e) {
            throw new BizException(e, SysMsgInfo.OSS0003);
        }

        return config.getQiniuDomain() + "/" + path;
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
        String path = config.getQiniuPrefix();
        if(StringUtils.isNotBlank(prefix)){
            path = config.getQiniuPrefix() + "/" + prefix;
        }
        return upload(data, getPath(path, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return this.uploadSuffix(inputStream, null, suffix);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String prefix, String suffix) {
        String path = config.getQiniuPrefix();
        if(StringUtils.isNotBlank(prefix)){
            path = config.getQiniuPrefix() + "/" + prefix;
        }
        return upload(inputStream, getPath(path, suffix));
    }
}
