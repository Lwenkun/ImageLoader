package com.lwenkun.imageloadinglibrary.utils;

import java.security.MessageDigest;

/**
 * Created by 15119 on 2015/11/24.
 */
public class MD5 {

    /**
     *
     * @param key 需要转换的字符串
     * @return 转换后的字符串
     */
    public String hashKeyForDisk(String key) {

        String cacheKey;

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            cacheKey = byteToHexString(md.digest());

        } catch(Exception e) {

            e.printStackTrace();
            cacheKey = String .valueOf(key.hashCode());
        }

        return cacheKey;
    }


    /**
     *
     * @param bytes 带转换的字节数组
     * @return 转换后的字符串
     */
    public String byteToHexString(byte[] bytes) {

        StringBuilder hexString = new StringBuilder();

        for(int aByte : bytes) {

            //将字节转换成十六进制的字符串形式
            String str = Integer.toHexString(0xff & aByte);

            //如果十六进制字符串是一位，则在其前加0
            if(str.length() == 1) {
                hexString.append('0');
            }
            hexString.append(str);
        }

          return hexString.toString();
    }
}
