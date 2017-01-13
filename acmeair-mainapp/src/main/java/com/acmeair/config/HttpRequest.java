package com.acmeair.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6.
 */
public class HttpRequest {

    private static String param = "uid0@email.com";
    private static String url = "http://192.168.0.88/customer/acmeair-cs/rest/api/customer/byid/" + param;


    public static String sendGet(String sessionId, long time) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url+"?" + "sendtime=" + time);
            // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
            URLConnection connection = realUrl.openConnection();
            // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
            connection.setRequestProperty("Cookie","sessionid="+sessionId);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            // 寤虹珛瀹為檯鐨勮繛鎺�
            connection.connect();
            // 鑾峰彇鎵�鏈夊搷搴斿ご瀛楁
            Map<String, List<String>> map = connection.getHeaderFields();
            // 閬嶅巻鎵�鏈夌殑鍝嶅簲澶村瓧娈�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 瀹氫箟 BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("鍙戦�丟ET璇锋眰鍑虹幇寮傚父锛�" + e);
            e.printStackTrace();
        }
        // 浣跨敤finally鍧楁潵鍏抽棴杈撳叆娴�
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
