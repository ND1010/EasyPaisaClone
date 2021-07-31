package com.app.bhaskar.easypaisa.utils;

import com.easypay.epmoney.epmoneylib.utils.Utility;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Checksum {

    public static Long currentTime = 0L;
    public static String  randomNumber ="";

    public static String getCheckSum(String agentCode) throws DecoderException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        // TODO Auto-generated method stub

        currentTime = Calendar.getInstance().getTimeInMillis();
        randomNumber = Utility.Companion.generateToken(15);
        System.out.println(currentTime);
        String data = agentCode.concat("|").concat(currentTime.toString()).concat("|").concat(randomNumber);
        String secretKey = "f85acd0c3c";//aefc05467d
        byte[] decodedKey = Hex.decodeHex(secretKey.toCharArray());
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(keySpec);
        byte[] dataBytes = data.getBytes("UTF-8");
        byte[] signatureBytes = mac.doFinal(dataBytes);
        String signature = new String(Base64.encodeBase64(signatureBytes), "UTF-8");
        System.out.println("CheckSum--> " + signature);

        return signature;
    }
}
