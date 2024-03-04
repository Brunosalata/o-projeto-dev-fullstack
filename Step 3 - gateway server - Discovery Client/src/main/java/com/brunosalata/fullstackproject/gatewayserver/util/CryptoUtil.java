package com.brunosalata.fullstackproject.gatewayserver.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class CryptoUtil {
    
    public static String encrypt(String plainText) throws Exception {
        MessageDigest md = null;

        try{
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e){
            throw new Exception(e.getMessage());
        }

        try{
            md.update(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }

        byte raw[] = md.digest();
        try{
            return new String(Base64.encodeBase64(raw), "UTF-8");
        } catch (Exception use) {
            throw new Exception(use);
        }
    }

}
