package com.games.job.common.utils;

import com.games.job.common.enums.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/24 17:38
 * @project:job-center
 * @full_name:com.games.job.common.utils.NetUtil
 * @ide:IntelliJ IDEA
 */
public class NetUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);

    public static String getPublicAddress() {
        String addr = null;
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    String ip = i.getHostAddress();
                    if (addr == null && !ip.startsWith("127.") && !ip.startsWith("192.")
                            && !ip.startsWith("10.") && ip.indexOf(":")==-1) {
                        addr = ip;
                    }
                }
            }
            return addr;
        } catch (Exception e) {
            LOGGER.error("Exception",e);
            return "";
        }
    }


    public static String getPrivateAddress() {
        String addr = null;
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    String ip = i.getHostAddress();
                    if (addr == null && (ip.startsWith("192.") || ip.startsWith("10.") || ip.startsWith("172."))) {
                        addr = ip;
                    }
                }
            }
            return addr;
        } catch (Exception e) {
            LOGGER.error("Exception",e);
            return "";
        }
    }

    /**
     * http 请求
     * @param url
     * @param data
     * @param timeout ms
     * @return
     */
    public static String send(String url, String data, String method, int timeout) {
        LOGGER.debug("send [{}] to [{}]", data, url);

        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        OutputStream os = null;

        try {
            URL u = new URL(url);
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method.toUpperCase());
            conn.setDoOutput(true);
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);

            if(StringUtils.isNotBlank(data)){
                byte[] bytes = data.getBytes("UTF-8");
                os = conn.getOutputStream();
                os.write(bytes);
                os.flush();
            }

            is = conn.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;

            while (null != (line = buf.readLine())) {
                sb.append(line);
            }
        } catch (Exception e) {
            LOGGER.warn(ExceptionUtils.getMessage(e));
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }

            if (null != os) {
                try {
                    os.close();
                } catch (Exception e) {

                }
            }
        }

        String res = sb.toString();
        LOGGER.debug("get [{}] from [{}]", res, url);

        return res;
    }

    /**
     * http 请求
     * 默认post ,超时3s
     * @param url
     * @param data
     * @return
     */
    public static String send(String url, String data,String httpMethod) {
        return send(url, data,httpMethod, 3000);
    }

    public static String send(String url, String data,String httpMethod,Integer timeout) {
        return send(url, data,httpMethod, timeout);
    }

    public static String sendJson(String url,String json,HttpMethod httpMethod){
        Map<String,String> map = JsonUtils.fromJsonToMap(json,String.class,String.class);
        return sendMap(url,map,httpMethod);
    }

    public static String sendMap(String url, Map<String,String> map,HttpMethod httpMethod){
        StringBuilder sb = new StringBuilder();
        if(null != map && map.size() > 0){
            Set<Map.Entry<String, String>> set = map.entrySet();
            for(Map.Entry<String, String> entry : set){
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return send(url,sb.toString(),httpMethod.getMethod());
    }

}
