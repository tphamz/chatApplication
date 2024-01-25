package com.example.api_server.service;

import org.apache.commons.lang3.RandomStringUtils;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherService {
    private static int SALT_SIZE = 32;
    private static int ENTITY_SIZE = 64;
    private static String FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";       
    private static String ALGORITHM = "AES/ECB/PKCS5Padding";
    static Logger logger = LoggerFactory.getLogger(CipherService.class);

    private static String generateEntityKey(){
        return CipherService.generateString(CipherService.ENTITY_SIZE);
    }

    private static String generateString(){
        return CipherService.generateString(CipherService.SALT_SIZE);
    }

    public static String generateString(int size){
        return RandomStringUtils.randomAlphanumeric(size);
    }
    
    public static String stringEncoded(byte [] input){
        return Base64.getEncoder().encodeToString(input);
    }

    public static byte[] stringDecoded(String input){
        return Base64.getDecoder().decode(input);
    }

    public static SecretKey generateSecret(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance(CipherService.FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }  

    public static String encrypt(String algorithm, String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] cipherResult = cipher.doFinal(input.getBytes());
        return CipherService.stringEncoded(cipherResult);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        byte [] cipherResult = CipherService.stringDecoded(cipherText);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        String result = new String(cipher.doFinal(cipherResult));
        return result;
    }

    public static String generateEntityKeyHash(String password) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException{
        String entityKey = CipherService.generateEntityKey();
        return CipherService.generateEntityKeyHash(password, entityKey);
    }

    public static String generateEntityKeyHash(String password, String entityKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException{
        String salt = CipherService.generateString();
        SecretKey secret = CipherService.generateSecret(password, salt);
        String entityKeyHash = CipherService.encrypt(CipherService.ALGORITHM, entityKey, secret);
        return salt + "::" + entityKeyHash;
    }

    public static String decryptEntityKeyHash(String password, String keyHash) throws Exception{
        try {
            String [] keyStrings = keyHash.split("::", 2);  
            SecretKey secret = CipherService.generateSecret(password, keyStrings[0]);
            String entityKey = CipherService.decrypt(CipherService.ALGORITHM, keyStrings[1], secret);    
            return entityKey;
        } catch (Exception e) {
            logger.error("catching error::" + e.getMessage());
            throw new Exception("Invalid keyHash/password");
        }
        
    }
}
