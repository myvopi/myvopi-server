package com.commoncoremodule.util;

import com.commoncoremodule.exception.ErrorCode;
import com.commoncoremodule.exception.InternalException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class Cipher {

    private final Key key;
    private final javax.crypto.Cipher cipher;
    private final byte[] iv;

    public Cipher(String aesSecretKey, String iv) {
        try {
            String transformation = "AES/CBC/PKCS5Padding";
            this.iv = iv.getBytes();
            String sk = toHexString(aesSecretKey.getBytes());
            this.key = generateKey(toBytes(sk, 16), "AES");
            this.cipher = javax.crypto.Cipher.getInstance(transformation);
        } catch (Exception e) {
            throw new InternalException(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getEngErrorMsg());
        }
    }

    public String encrypt(String encryptStr) {
        try {
            IvParameterSpec spec = new IvParameterSpec(iv);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, spec);
            byte[] plain = encryptStr.getBytes(StandardCharsets.UTF_8);
            byte[] encrypt = cipher.doFinal(plain);
            return new String(Base64.encodeBase64(encrypt));
        } catch (Exception e) {
            throw new InternalException(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getEngErrorMsg());
        }
    }

    public String decrypt(String decryptStr) {
        try {
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] decryptByte =  Base64.decodeBase64(decryptStr);
            byte[] decrypt = cipher.doFinal(decryptByte);
            return new String(decrypt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InternalException(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getEngErrorMsg());
        }
    }

    private Key generateKey(byte[] keyData, String algorithm) {
        return new SecretKeySpec(keyData, algorithm);
    }

    private String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xF0) >> 4, 16));
            result.append(Integer.toString(b & 0x0F, 16));
        }
        return result.toString();
    }

    private byte[] toBytes(String digits, int radix) throws IllegalArgumentException {
        if (digits == null) {
            return null;
        }
        if (radix != 16 && radix != 10 && radix != 8) {
            throw new InternalException(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getEngErrorMsg());
        }
        int divLen = (radix == 16) ? 2 : 3;
        int length = digits.length();
        if (length % divLen == 1) {
            throw new InternalException(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getEngErrorMsg());
        }
        length = length / divLen;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * divLen;
            bytes[i] = (byte)(Short.parseShort(digits.substring(index, index+divLen), radix));
        }
        return bytes;
    }
}
