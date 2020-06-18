//package com.mojieai.common.utils;
//
//import com.qiniu.common.QiniuException;
//import com.qiniu.common.Zone;
//import com.qiniu.http.Response;
//import com.qiniu.storage.BucketManager;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//import org.apache.commons.io.IOUtils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class UploadImgUtil {
//    //构造一个带指定Zone对象的配置类
//    private static Configuration cfg = new Configuration(Zone.zone0());
//
//    private static UploadManager uploadManager = new UploadManager(cfg);
//
//    public static String BUCKET_NAME = "static";
//
//    //设置好账号的ACCESS_KEY和SECRET_KEY
//    public static String ACCESS_KEY = "cPSC_aA8oybkt0Gzi6icpMirU-TMKgQiP_EQTwO7";
//    public static String SECRET_KEY = "ZTJxJsfSK1Ddd3uUWuI5EyF3XrsfUVavSYBaNIs3";
//
//    //http://oiqkdrthg.bkt.clouddn.com//currency/icon/testfeixiaohao.jpg
//
//
//    public static String getToken(String bucketName, String key) throws IOException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        return auth.uploadToken(bucketName, key);
//    }
//
//    /**
//     * @param url        网络上一个资源文件的URL
//     * @param bucketName 空间名称
//     * @param key        空间内文件的key[唯一的]
//     * @param @return
//     * @return String
//     * @throws QiniuException
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: fetchToBucket
//     * @Description: 提取网络资源并上传到七牛空间里
//     */
//    public static String fetchToBucket(String url, String bucketName, String key)
//            throws QiniuException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        DefaultPutRet putret = bucketManager.fetch(url, bucketName, key);
//        return URL_PRE + putret.key;
//    }
//
//    /**
//     * @param @param  inputStream 待上传文件输入流
//     * @param @param  bucketName 空间名称
//     * @param @param  key 空间内文件的key
//     * @param @param  mimeType 文件的MIME类型，可选参数，不传入会自动判断
//     * @param @return
//     * @param @throws IOException
//     * @return String
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: uploadFile
//     * @Description: 七牛图片上传
//     */
//    public static String uploadFile(InputStream inputStream, String bucketName,
//                                    String key, String mimeType) throws IOException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        String token = auth.uploadToken(bucketName);
//        byte[] byteData = IOUtils.toByteArray(inputStream);
//        Response response = uploadManager.put(byteData, key, token, null,
//                mimeType, false);
//        inputStream.close();
//        return response.bodyString();
//    }
//
//    /**
//     * @param @param  inputStream 待上传文件输入流
//     * @param @param  bucketName 空间名称
//     * @param @param  key 空间内文件的key
//     * @param @return
//     * @param @throws IOException
//     * @return String
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: uploadFile
//     * @Description: 七牛图片上传
//     */
//    public static String uploadFile(InputStream inputStream, String bucketName,
//                                    String key) throws IOException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        String token = auth.uploadToken(bucketName);
//        byte[] byteData = IOUtils.toByteArray(inputStream);
//        Response response = uploadManager.put(byteData, key, token, null, null,
//                false);
//        inputStream.close();
//        return response.bodyString();
//    }
//
//    /**
//     * @param filePath   待上传文件的硬盘路径
//     * @param fileName   待上传文件的文件名
//     * @param bucketName 空间名称
//     * @param key        空间内文件的key
//     * @param @return
//     * @param @throws    IOException
//     * @return String
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: uploadFile
//     * @Description: 七牛图片上传
//     */
//    public static String uploadFile(String filePath, String fileName,
//                                    String bucketName, String key) throws IOException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        String token = auth.uploadToken(bucketName);
//        InputStream is = new FileInputStream(new File(filePath + fileName));
//        byte[] byteData = IOUtils.toByteArray(is);
//        Response response = uploadManager.put(byteData,
//                (key == null || "".equals(key)) ? fileName : key, token);
//        is.close();
//        return response.bodyString();
//    }
//
//    /**
//     * @param filePath   待上传文件的硬盘路径
//     * @param fileName   待上传文件的文件名
//     * @param bucketName 空间名称
//     * @param @return
//     * @param @throws    IOException
//     * @return String
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: uploadFile
//     * @Description: 七牛图片上传[若没有指定文件的key, 则默认将fileName参数作为文件的key]
//     */
//    public static String uploadFile(String filePath, String fileName,
//                                    String bucketName) throws IOException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        String token = auth.uploadToken(bucketName);
//        InputStream is = new FileInputStream(new File(filePath + fileName));
//        byte[] byteData = IOUtils.toByteArray(is);
//        Response response = uploadManager.put(byteData, fileName, token);
//        is.close();
//        return response.bodyString();
//    }
//
//    /**
//     * @param url
//     * @param bucketName
//     * @param @return
//     * @param @throws    QiniuException
//     * @return String
//     * @throws
//     * @Author: Lanxiaowei(736031305@qq.com)
//     * @Title: fetchToBucket
//     * @Description: 提取网络资源并上传到七牛空间里, 不指定key，则默认使用url作为文件的key
//     */
//    public static String fetchToBucket(String url, String bucketName)
//            throws QiniuException {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        DefaultPutRet putret = bucketManager.fetch(url, bucketName);
//        return URL_PRE + putret.key;
//    }
//
//    public static void main(String[] args) throws QiniuException {
//        String digital = fetchToBucket("http://static.feixiaohao.com/coin/4919a4a3d6d3eb2c7e3728fc9dc9679f_mid.png",
//                "static",
//                "testfeixiaohao1.jpg");
//        System.out.println(digital);
//    }
//}