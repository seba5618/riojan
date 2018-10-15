package com.riojan.demo.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class SendgridKey {

    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    private static final String PART1 = "KvAFajYfDFtnVbsoFutnjKbSyBb/HsC1sUruduiQbH0b+Wx65SG+";
    private static final String PART2 = "6AAweo6iesoqgzRGce+HDhoPuq7RUw8AHQIK4KQchgg3SR1Qa3rZwDY=";

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    private String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String result = new String(decValue);
        return result;
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    private String encrypt(String plainData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(plainData.getBytes());
        return Base64.getEncoder().encodeToString(encValue);
    }

    public String getKey() throws Exception{
        return decrypt(PART1+PART2);
    }


    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() {
        return new SecretKeySpec(keyValue, ALGO);
    }


    public static void main (String args[]) throws Exception{
        SendgridKey sendgridKey = new SendgridKey();
        System.out.println(sendgridKey.getKey());
        System.out.println(sendgridKey.encrypt(sendgridKey.getKey()));
    }
}
