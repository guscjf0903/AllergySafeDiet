package org.api.util;

import static org.api.exception.ErrorCodes.DECRYPTION_ERROR;
import static org.api.exception.ErrorCodes.ENCRYPTION_ERROR;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.api.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EncryptionUtil {
    private final String ALGORITHM = "AES";
    private final byte[] KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes(StandardCharsets.UTF_8);

    public String encrypt(String value)  {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().encodeToString(encryptedValue);  // URL 안전한 Base64 인코딩
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ENCRYPTION_ERROR);
        }

    }

    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedValue = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedValue));

            return new String(decryptedValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomException(DECRYPTION_ERROR);
        }

    }
}
