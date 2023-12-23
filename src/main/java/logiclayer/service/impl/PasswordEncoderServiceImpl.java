package logiclayer.service.impl;


import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.PasswordEncodeException;
import logiclayer.service.PasswordEncoderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password encoder on "MD5" algorithm
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class PasswordEncoderServiceImpl implements PasswordEncoderService {
    private static final String ENCODING_ALGORITHM_KEY = "MD5";

    @Override
    public String encode(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance(ENCODING_ALGORITHM_KEY);
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordEncodeException("Password encode error");
        }
    }
}
