package com.seungjun.randomox.utils;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.codec.binary.Base64;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 암호화를 위한 지원 클래스
 * Created by SeungJun on 2017-07-19.
 */

public class AESUtils {

    private final String iv;
    private final Key keySpec;

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     * @param context
     * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
     */
    public AESUtils(Context context) throws Exception {
        String key = getBase64Text(context);

        iv = key;
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");

        int len = b.length;
        if(len > keyBytes.length){
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }

    /**
     * AES256 으로 암호화 한다.
     * @param str 암호화할 문자열
     * @return 암호화 된 문자열
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     * @param str 복호화할 문자열
     * @return 복호화 된 문자열
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }

    /**
     * Base64 암호화 파일 복호화
     * @param context
     * @return 복호화된 문자
     */
    private static String getBase64Text(Context context){

        AssetManager am = context.getResources().getAssets();

        InputStream is = null;

        // 읽어들인 문자열이 담길 변수
        String result = null;

        try {
            is = am.open("base64.txt");
            int size = is.available();

            if (size > 0) {
                byte[] data = new byte[size];
                is.read(data);
                result = new String(Base64.decodeBase64(data));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {}
            }
        }

        am = null;
        return result;

    }
}

