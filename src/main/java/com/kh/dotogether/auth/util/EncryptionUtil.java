package com.kh.dotogether.auth.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EncryptionUtil {
	
	@Value("${aes.secret}")
    private String secretKey;

    private static final String ALGORITHM = "AES";
    private SecretKeySpec keySpec;

    @PostConstruct
    public void init() {
        // 16자리 확인
        if (secretKey == null || secretKey.length() != 16) {
            throw new IllegalArgumentException("AES secret key 는 반드시 16자리여야 합니다.");
        }
        keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        log.info("AESUtil 초기화 완료 (key length = {})", secretKey.length());
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 암호화 오류", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 복호화 오류", e);
        }
    }

}
