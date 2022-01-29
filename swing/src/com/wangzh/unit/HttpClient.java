package com.wangzh.unit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public static String doGet(String httpUrl) {
        HttpURLConnection connection = null;
        URL url = null;
        String result = "";
        try {
            url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("GET");

            setHeader(connection);
        } catch (Exception exp) {
            exp.printStackTrace();
            return result;
        }

        try (InputStream is = connection.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            connection.connect();

            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() != 200) {
                return result;
            }

            StringBuilder sbf = new StringBuilder();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();

        } catch (IOException exp) {
            exp.printStackTrace();
        }

        connection.disconnect();

        return result;
    }

    public static String doPost(String httpUrl, String param) {
        HttpURLConnection connection = null;
        URL url = null;
        String result = "";
        try {
            url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            setHeader(connection);
        } catch (Exception exp) {
            exp.printStackTrace();
            return result;
        }

        try (OutputStream os = connection.getOutputStream()) {
            os.write(param.getBytes(StandardCharsets.UTF_8));
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() != 200) {
//                return result;
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        try (InputStream is = connection.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sbf = new StringBuilder();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();

        } catch (IOException exp) {
            exp.printStackTrace();
        }

        connection.disconnect();

        System.out.println(httpUrl + "   " + param + "     " + result);
        return result;
    }

    private static void setHeader(HttpURLConnection connection) {
        // 设置连接主机服务器的超时时间：15000毫秒
        connection.setConnectTimeout(15000);
        // 设置读取远程返回的数据时间：60000毫秒
        connection.setReadTimeout(60000);

        connection.setRequestProperty("authority", "zuoyenew.xinkaoyun.com:30001");
        connection.setRequestProperty("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
        connection.setRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        connection.setRequestProperty("origin", "https://homework.xinkaoyun.com");
        connection.setRequestProperty("sec-fetch-site", "same-site");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("referer", "https://homework.xinkaoyun.com/");
        connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
    }
}