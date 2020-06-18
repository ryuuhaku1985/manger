package com.jdxl.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


@Slf4j
@Service
public class HttpServiceUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String METHOD_TYPE_GET = "GET";
    public static final String CHARSET_GBK = "GBK";
    private static final int TIMEOUT = 45000;
    public static final String ENCODING = "UTF-8";
    /**
     * 默认连接超时为20秒
     */
    public static final int CONNECTION_TIME_OUT = 20000;
    /**
     * 默认数据传输超时为20秒
     */
    public static final int SO_TIME_OUT = 20000;

    public static final int DEFAULT_CONNECT_TIME_OUT = 10000;

    public static final int DEFAULT_READ_TIME_OUT = 30000;


    public static String sendRequest(String url, int connectTimeout, int readTimeout, String charset, boolean
            returnSingle) throws RemoteInvocationFailureException {
        return sendRequest(url, connectTimeout, readTimeout, charset, returnSingle, null, null);
    }

    public static String sendRequest(String url, int connectTimeout, int readTimeout, String charset, boolean
            returnSingle, String contentType, String acceptCharset) throws RemoteInvocationFailureException {
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }
            conn = getURLConnection(url, connectTimeout, readTimeout, contentType, acceptCharset);
            in = new BufferedReader(new InputStreamReader(connect(conn),
                    charset));

            String result = null;
            result = getReturnResult(in, returnSingle);
            return result;
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常[" + url + "]", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
    }

    public static String sendRequest(String url) throws RemoteInvocationFailureException {
        return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT,
                DEFAULT_CHARSET, false);
    }

    public static String sendRequest(String url, String encoding) throws RemoteInvocationFailureException {
        return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT,
                encoding, false);
    }


    public static String sendRequest(String url, boolean returnSingle) throws RemoteInvocationFailureException {
        return sendRequest(url, DEFAULT_CONNECT_TIME_OUT,
                DEFAULT_READ_TIME_OUT,
                DEFAULT_CHARSET, returnSingle);
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout, int
            readTimeout, boolean needCompress, String contentType, boolean returnSingle) {
        return sendPostRequest(url, content, charset, connectTimeout, readTimeout, needCompress, contentType,
                returnSingle, null);
    }

    public static String sendPostRequest(String url, String content, String charset, int connectTimeout, int
            readTimeout, boolean needCompress, String contentType, boolean returnSingle, String userAgent)
            throws RemoteInvocationFailureException {
        BufferedReader in = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = getURLConnection(url, connectTimeout, readTimeout,
                    contentType, null);
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }

            if (StringUtils.isNotBlank(userAgent)) {
                httpConn.setRequestProperty("User-agent", userAgent);
            }
            InputStream stream = postConnect(httpConn, content, charset,
                    needCompress);

            in = new BufferedReader(new InputStreamReader(stream, charset));
            String result = getReturnResult(in, returnSingle);
            // log.debug("请求返回结果:" + result);
            return result;
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常[" + url + "]", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                // if (httpConn != null)
                // {
                // httpConn.disconnect();
                // }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
    }

    public static String sendPostRequest(String url, String content, String charset) {
        return sendPostRequest(url, content, charset,
                DEFAULT_CONNECT_TIME_OUT,
                DEFAULT_READ_TIME_OUT);
    }

    public static String sendPostRequest(String url, String content, String charset,
                                         int connectTimeout, int readTimeout)
            throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset, connectTimeout,
                readTimeout, false);
    }

    public static String sendPostRequest(String url, String content, String charset,
                                         boolean needCompress) throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset,
                DEFAULT_CONNECT_TIME_OUT,
                DEFAULT_READ_TIME_OUT, needCompress);
    }

    public static String sendPostRequest(String url, String content, String charset,
                                         int connectTimeout, int readTimeout, String contentType)
            throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset, connectTimeout,
                readTimeout, false, contentType, false);
    }

    public static String sendPostRequest(String url, String content, String charset,
                                         int connectTimeout, int readTimeout, boolean needCompress)
            throws RemoteInvocationFailureException {
        return sendPostRequest(url, content, charset, connectTimeout,
                readTimeout, needCompress, null, false);
    }

    /**
     * post方式发送请求
     *
     * @param url
     * @param content
     * @param connectTimeout
     * @param readTimeout
     * @param needCompress
     * @param sslContext
     * @return
     * @throws IOException
     * @throws MalformedURLException
     */
    public static String sendPostRequest(String url, String content, String charset,
                                         int connectTimeout, int readTimeout, boolean needCompress,
                                         SSLContext sslContext) throws MalformedURLException, IOException {
        InputStream is = null;
        // BufferedReader in = null;
        HttpURLConnection httpConn = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        charset = StringUtils.isBlank(charset) ? DEFAULT_CHARSET
                : charset;
        try {
            httpConn = getURLConnection(url, connectTimeout, readTimeout, null, null);
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }
            is = postConnect(httpConn, content, charset, needCompress);
            int ch;
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(baos);
            while ((ch = bis.read()) != -1) {
                bos.write(ch);
            }
            bos.flush();
            bis.close();
            return new String(baos.toByteArray(), DEFAULT_CHARSET);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (Exception e) {
                throw new IOException("[连接关闭异常]", e);
            }
        }
    }

    public static String sendHttpsPostRequest(String url, String content,
                                              String charset, int connectTimeout, int readTimeout,
                                              boolean needCompress, String contentType, boolean returnSingle)
            throws RemoteInvocationFailureException {
        BufferedReader in = null;
        HttpsURLConnection httpsConn = null;
        try {
            httpsConn = getHttpsURLConnection(url, connectTimeout, readTimeout,
                    contentType);
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }
            InputStream stream = postConnect(httpsConn, content, charset, needCompress);

            in = new BufferedReader(new InputStreamReader(stream, charset));
            String result = getReturnResult(in, returnSingle);
            return result;
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常[" + url + "]", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
    }


    public static String sendHttpsRequest(String url, String charset, int connectTimeout, int readTimeout,
                                          String contentType, boolean returnSingle)
            throws RemoteInvocationFailureException {
        BufferedReader in = null;
        HttpsURLConnection httpsConn = null;
        try {
            httpsConn = getHttpsURLConnection(url, connectTimeout, readTimeout, contentType);
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }

            in = new BufferedReader(new InputStreamReader(connect(httpsConn), charset));
            String result = getReturnResult(in, returnSingle);
            return result;
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常[" + url + "]", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
    }


    public static String sendHttpsPostRequest(String url, String content,
                                              String charset) {
        return sendHttpsPostRequest(url, content, charset,
                DEFAULT_CONNECT_TIME_OUT,
                DEFAULT_READ_TIME_OUT, false, null, false);
    }

    public static String sendHttpsRequest(String url, String charset) {
        return sendHttpsRequest(url, charset, DEFAULT_CONNECT_TIME_OUT,
                DEFAULT_READ_TIME_OUT, null, false);
    }

    private static InputStream postConnect(HttpURLConnection httpConn, String content,
                                           String charset, boolean needCompress) {
        String urlStr = httpConn.getURL().toString();
        try {
            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,
            // 默认情况下是false;
            httpConn.setDoOutput(true);
            // Post 请求不能使用缓存
            httpConn.setUseCaches(false);
            // 设定请求的方法为"POST"，默认是GET
            httpConn.setRequestMethod("POST");
            if (needCompress) {
                sendCompressRequest(content, charset, httpConn);
            } else {
                sendNoCompressRequest(content, charset, httpConn);
            }
            // 接收数据
            if (needCompress) {
                return new GZIPInputStream(httpConn.getInputStream());
            }
            return httpConn.getInputStream();
        } catch (MalformedURLException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "远程访问异常[" + urlStr + "]", e);
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "网络IO异常[" + urlStr + "]", e);
        }
    }

    private static void sendCompressRequest(String content, String charset,
                                            HttpURLConnection httpConn) {
        GZIPOutputStream out = null;
        try {
            httpConn.setRequestProperty("Content-Type", "application/x-gzip");
            httpConn.setRequestProperty("Accept", "application/x-gzip");
            out = new GZIPOutputStream(httpConn.getOutputStream());
            out.write(content.getBytes("GBK"));
            out.flush();
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常["
                    + httpConn.getURL().toString() + "]", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * 发送原始消息
     *
     * @param content
     * @param charset
     * @param httpConn
     */
    private static void sendNoCompressRequest(String content, String charset,
                                              HttpURLConnection httpConn) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    httpConn.getOutputStream(), charset));
            out.write(content);
            out.flush();
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常["
                    + httpConn.getURL().toString() + "]", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 建立远程连接
     *
     * @return
     */
    private static InputStream connect(HttpURLConnection httpConn) {
        String urlStr = httpConn.getURL().toString();
        try {
            if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.info(urlStr + "|ResponseCode="
                        + httpConn.getResponseCode());
            }
            return httpConn.getInputStream();
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "网络IO异常[" + urlStr + "]", e);
        }
    }

    /**
     * 构造URLConnnection
     *
     * @param urlStr
     * @param connectTimeout
     * @param readTimeout
     * @param contentType    content-type类型
     * @return
     * @throws RemoteInvocationFailureException
     */
    private static HttpURLConnection getURLConnection(String urlStr, int connectTimeout, int readTimeout, String
            contentType, String acceptCharset)
            throws RemoteInvocationFailureException {
        try {
            URL remoteUrl = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) remoteUrl
                    .openConnection();
            httpConn.setConnectTimeout(connectTimeout);
            httpConn.setReadTimeout(readTimeout);
            if (StringUtils.isNotBlank(contentType)) {
                httpConn.setRequestProperty("Content-type", contentType);
            }
            if (StringUtils.isNotBlank(acceptCharset)) {
                httpConn.setRequestProperty("Accept-Charset", acceptCharset);
            }
            return httpConn;
        } catch (MalformedURLException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "远程访问异常[" + urlStr + "]", e);
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "网络IO异常[" + urlStr + "]", e);
        }
    }

    /**
     * 构造URLConnnection
     *
     * @param urlStr
     * @param connectTimeout
     * @param readTimeout
     * @param contentType    content-type类型
     * @return
     * @throws RemoteInvocationFailureException
     */
    private static HttpsURLConnection getHttpsURLConnection(String urlStr,
                                                            int connectTimeout, int readTimeout, String contentType)
            throws RemoteInvocationFailureException {
        try {
            URL remoteUrl = new URL(urlStr);
            HttpsURLConnection httpConn = (HttpsURLConnection) remoteUrl
                    .openConnection();
            httpConn.setConnectTimeout(connectTimeout);
            httpConn.setReadTimeout(readTimeout);
            if (contentType != null) {
                httpConn.setRequestProperty("content-type", contentType);
            }
            return httpConn;
        } catch (MalformedURLException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "远程访问异常[" + urlStr + "]", e);
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException(
                    "网络IO异常[" + urlStr + "]", e);
        }
    }

    private static HttpURLConnection getURLConnection(String urlStr, int connectTimeout, int readTimeout)
            throws RemoteInvocationFailureException {
        return getURLConnection(urlStr, connectTimeout, readTimeout, null, null);
    }

    private static String getReturnResult(BufferedReader in, boolean returnSingleLine)
            throws IOException {
        if (returnSingleLine) {
            return in.readLine();
        } else {
            StringBuffer sb = new StringBuffer();
            String result = "";
            while ((result = in.readLine()) != null) {
//                log.debug("从中心返回：" + result);
                sb.append(StringUtils.trimToEmpty(result));
            }
            return sb.toString();
        }
    }

    public static String getRequestFileStr(String urlStr) {
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            String charset = DEFAULT_CHARSET;
            conn = getURLConnection(urlStr, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT);
            in = new BufferedReader(new InputStreamReader(connect(conn),
                    charset));
            StringBuffer sb = new StringBuffer();
            String result = "";
            while ((result = in.readLine()) != null) {
                sb.append(StringUtils.trimToEmpty(result) + Constant.SEPARATOR_LINE);
            }
//            if (StringUtils.isBlank(sb.toString())) {
//                throw new RemoteInvocationFailureException("网络异常，" + urlStr
//                        + " 返回数据为空");
//            }
            return sb.toString();
        } catch (IOException e) {
            log.warn("", e);
            throw new RemoteInvocationFailureException("网络IO异常[" + urlStr + "]", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ips;
        if (!StringUtils.isBlank(ip)
                && (ips = ip.split(Constant.COMMA_SPLIT_STR)).length > 0) {
            ip = ips[0];
        }
        return ip;
    }
    /**
     * 创建HTTP连接
     *
     * @param url
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    private static HttpURLConnection createConnection(String url,
                                                      String method, Map<String, String> headerParameters, String body)
            throws Exception {
        URL Url = new URL(url);
        trustAllHttpsCertificates();
        HttpURLConnection httpConnection = (HttpURLConnection) Url
                .openConnection();
        // 设置请求时间
        httpConnection.setConnectTimeout(TIMEOUT);
        // 设置 header
        if (headerParameters != null) {
            Iterator<String> iteratorHeader = headerParameters.keySet()
                    .iterator();
            while (iteratorHeader.hasNext()) {
                String key = iteratorHeader.next();
                httpConnection.setRequestProperty(key,
                        headerParameters.get(key));
            }
        }
        httpConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + ENCODING);

        // 设置请求方法
        httpConnection.setRequestMethod(method);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        // 写query数据流
        if (!(body == null || body.trim().equals(""))) {
            OutputStream writer = httpConnection.getOutputStream();
            try {
                writer.write(body.getBytes(ENCODING));
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        }

        // 请求结果
        int responseCode = httpConnection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception(responseCode
                    + ":"
                    + inputStream2String(httpConnection.getErrorStream(),
                    ENCODING));
        }

        return httpConnection;
    }

    /**
     * POST请求
     * @param address 请求地址
     * @param headerParameters 参数
     * @param body
     * @return
     * @throws Exception
     */
    public static String post(String address,
                              Map<String, String> headerParameters, String body) throws Exception {

        return proxyHttpRequest(address, "POST", null,
                getRequestBody(headerParameters));
    }

    /**
     * GET请求
     * @param address
     * @param headerParameters
     * @param body
     * @return
     * @throws Exception
     */
    public static String get(String address,
                             Map<String, String> headerParameters, String body) throws Exception {

        return proxyHttpRequest(address + "?"
                + getRequestBody(headerParameters), "GET", null, null);
    }

    /**
     * 读取网络文件
     * @param address
     * @param headerParameters
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFile(String address,
                                 Map<String, String> headerParameters, File file) throws Exception {
        String result = "fail";

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = createConnection(address, "POST", null,
                    getRequestBody(headerParameters));
            result = readInputStream(httpConnection.getInputStream(), file);

        } catch (Exception e) {
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }

        }

        return result;
    }

    public static byte[] getFileByte(String address,
                                     Map<String, String> headerParameters) throws Exception {
        byte[] result = null;

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = createConnection(address, "POST", null,
                    getRequestBody(headerParameters));
            result = readInputStreamToByte(httpConnection.getInputStream());

        } catch (Exception e) {
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }

        }

        return result;
    }

    /**
     * 读取文件流
     * @param in
     * @return
     * @throws Exception
     */
    public static String readInputStream(InputStream in, File file)
            throws Exception {
        FileOutputStream out = null;
        ByteArrayOutputStream output = null;

        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            out = new FileOutputStream(file);
            out.write(output.toByteArray());

        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return "success";
    }

    public static byte[] readInputStreamToByte(InputStream in) throws Exception {
        FileOutputStream out = null;
        ByteArrayOutputStream output = null;
        byte[] byteFile = null;

        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            byteFile = output.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return byteFile;
    }

    /**
     * HTTP请求
     *
     * @param address
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    public static String proxyHttpRequest(String address, String method,
                                          Map<String, String> headerParameters, String body) throws Exception {
        String result = null;
        HttpURLConnection httpConnection = null;

        try {
            httpConnection = createConnection(address, method,
                    headerParameters, body);

            String encoding = "UTF-8";
            if (httpConnection.getContentType() != null
                    && httpConnection.getContentType().indexOf("charset=") >= 0) {
                encoding = httpConnection.getContentType()
                        .substring(
                                httpConnection.getContentType().indexOf(
                                        "charset=") + 8);
            }
            result = inputStream2String(httpConnection.getInputStream(),
                    encoding);
            // logger.info("HTTPproxy response: {},{}", address,
            // result.toString());

        } catch (Exception e) {
            // logger.info("HTTPproxy error: {}", e.getMessage());
            throw e;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }

    /**
     * 将参数化为 body
     * @param params
     * @return
     */
    public static String getRequestBody(Map<String, String> params) {
        return getRequestBody(params, true);
    }

    /**
     * 将参数化为 body
     * @param params
     * @return
     */
    public static String getRequestBody(Map<String, String> params,
                                        boolean urlEncode) {
        StringBuilder body = new StringBuilder();

        Iterator<String> iteratorHeader = params.keySet().iterator();
        while (iteratorHeader.hasNext()) {
            String key = iteratorHeader.next();
            String value = params.get(key);

            if (urlEncode) {
                try {
                    body.append(key + "=" + URLEncoder.encode(value, ENCODING)
                            + "&");
                } catch (UnsupportedEncodingException e) {
                    // e.printStackTrace();
                }
            } else {
                body.append(key + "=" + value + "&");
            }
        }

        if (body.length() == 0) {
            return "";
        }
        return body.substring(0, body.length() - 1);
    }

    /**
     * 读取inputStream 到 string
     * @param input
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String inputStream2String(InputStream input, String encoding)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,
                encoding));
        StringBuilder result = new StringBuilder();
        String temp = null;
        while ((temp = reader.readLine()) != null) {
            result.append(temp);
        }

        return result.toString();

    }


    /**
     * 设置 https 请求
     * @throws Exception
     */
    private static void trustAllHttpsCertificates() throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String str, SSLSession session) {
                return true;
            }
        });
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }


    //设置 https 请求证书
    static class miTM implements javax.net.ssl.TrustManager,javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }


    }


}
