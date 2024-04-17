package com.yxq.carpark.serviceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yxq.carpark.entity.BaiduConfig;
import com.yxq.carpark.service.BaiduService;
import com.yxq.carpark.utils.Base64Util;
import com.yxq.carpark.utils.FileUtil;
import com.yxq.carpark.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


import static com.yxq.carpark.utils.FileUtil.readConfig;
import static com.yxq.carpark.utils.FileUtil.writeConfig;

@Service
@Slf4j
public class BaiduServiceImpl implements BaiduService {
    @Value("${licensePlate.baidu.base_url}")
    private String baseUrl;
    @Value("${licensePlate.baidu.client_id}")
    private String clientId;
    @Value("${licensePlate.baidu.client_secret}")
    private String clientSecret;
    @Value("${licensePlate.baidu.get_token}")
    private String getToken;
    @Value("${licensePlate.baidu.license_plate}")
    private String licensePlateUri;
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    @Override
    public String getAccessToken() {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        String requestUrl = baseUrl + getToken + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";
        Request request = new Request.Builder()
                .url(requestUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println("responseBody: " + responseBody);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(responseBody).getAsJsonObject();
            String accessToken = jsonObject.get("access_token").getAsString();
            // 写入配置文件
            writeConfig(accessToken);
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("百度API获取token异常" + e);
        }
        return null;
    }

    /**
     * 车牌识别
     * @param filePath
     * @return
     */
    @Override
    public String licensePlate(String filePath) {

        String url = baseUrl + licensePlateUri;
        try {
            LocalDateTime now = LocalDateTime.now();
            long nowEpochSecond = now.toEpochSecond(ZoneOffset.UTC);
            String accessToken = "";

            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            BaiduConfig baiduConfig = readConfig();
            String expireIn = baiduConfig.getExpireIn();
            long expireLong = Long.parseLong(expireIn);
            accessToken = expireLong - nowEpochSecond > 0 ? baiduConfig.getAccessToken() : getAccessToken();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("车牌识别结果: " + result);
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            JsonObject wordsResult = jsonObject.getAsJsonObject("words_result");
            if (wordsResult != null) {
                String number = wordsResult.get("number").getAsString();
                return number;
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("车牌识别异常: " + e);
        }
        return null;
    }


    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusDays(30);
        writeConfig("asdpl");
        BaiduConfig baiduConfig = readConfig();
        String expireIn = baiduConfig.getExpireIn();
        long expireLong = Long.parseLong(expireIn);
        long epochSecond = now.toEpochSecond(ZoneOffset.UTC);
        System.out.println(expireLong - epochSecond);
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

    }
}
