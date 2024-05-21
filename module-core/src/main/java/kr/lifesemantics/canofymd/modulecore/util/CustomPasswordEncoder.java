package kr.lifesemantics.canofymd.modulecore.util;

import kr.lifesemantics.canofymd.modulecore.constants.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class CustomPasswordEncoder {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {

                String saltedPassword = rawPassword.toString() + KeyConstants.SALT;

                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hashedBytes = digest.digest(saltedPassword.getBytes());

                    log.info("encode pwd :: {}", new String(Hex.encode(hashedBytes)));

                    return new String(Hex.encode(hashedBytes));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Error encoding password", e);
                }

            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {

                String hashedPassword = encode(rawPassword);

                return encodedPassword.equals(hashedPassword);
            }

        };

    }

}