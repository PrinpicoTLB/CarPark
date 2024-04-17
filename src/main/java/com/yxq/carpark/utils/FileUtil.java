package com.yxq.carpark.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.binding.StringFormatter;
import com.yxq.carpark.entity.BaiduConfig;
import org.apache.ibatis.javassist.compiler.ast.Variable;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class FileUtil {
    public static void writeConfig(String accessToken) {
        Properties prop = new Properties();
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime plusDays = now.plusDays(30);
            long plusDaysEpochSecond = plusDays.toEpochSecond(ZoneOffset.UTC);

            prop.setProperty("access_token", accessToken);
            prop.setProperty("expires_in", String.valueOf(plusDaysEpochSecond));

            prop.store(output, null);
            System.out.println("配置文件保存成功");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static BaiduConfig readConfig() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);

            String accessToken = prop.getProperty("access_token");
            String expiresIn = prop.getProperty("expires_in");

            return new BaiduConfig(accessToken,expiresIn);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ( (hasRead = fis.read(bbuf)) > 0 ) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }
}
