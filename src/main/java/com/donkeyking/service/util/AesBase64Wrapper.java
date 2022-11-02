package com.donkeyking.service.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesBase64Wrapper{

    private static byte iv[];

    private static String SALT = "12345";
    static {
        try {
            iv = getBytes("79994A6EF73DA76C");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptAndEncode(String raw,String password)
    {
        try
        {
            Cipher c = getCipher(1,password);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            return Base64.getEncoder().encodeToString(encryptedVal);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    public static String decodeAndDecrypt(String encrypted,String password) throws Exception
    {
        byte[] decodedValue = Base64.getDecoder().decode(encrypted);
        Cipher c = getCipher(2,password);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static byte[] getBytes(String str) throws UnsupportedEncodingException
    {
        return str.getBytes("UTF-8");
    }

    private static Cipher getCipher(int mode,String password)throws Exception
    {

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(mode, generateKey(SALT,password), new IvParameterSpec(iv));
        return c;
    }


    private static Key generateKey(String salt,String password) throws Exception
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] saltb = getBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltb, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }



}