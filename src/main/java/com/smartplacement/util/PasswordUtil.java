package com.smartplacement.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing with PBKDF2 (built into the JDK).
 *
 * Stored format: pbkdf2$<iterations>$<base64 salt>$<base64 hash>
 *
 * Accounts created before hashing was introduced still hold plain-text
 * passwords; verify() accepts those so existing logins keep working.
 */
public final class PasswordUtil {

    private static final String PREFIX = "pbkdf2";
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String password) {

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt, ITERATIONS);

        Base64.Encoder encoder = Base64.getEncoder();

        return PREFIX + "$" + ITERATIONS + "$"
                + encoder.encodeToString(salt) + "$"
                + encoder.encodeToString(hash);
    }

    public static boolean verify(String password, String stored) {

        if (password == null || stored == null) {
            return false;
        }

        if (!isHashed(stored)) {

            // Legacy plain-text password
            return MessageDigest.isEqual(
                    password.getBytes(StandardCharsets.UTF_8),
                    stored.getBytes(StandardCharsets.UTF_8)
            );
        }

        String[] parts = stored.split("\\$");

        if (parts.length != 4) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] salt = decoder.decode(parts[2]);
            byte[] expected = decoder.decode(parts[3]);

            byte[] actual = pbkdf2(password, salt, iterations);

            return MessageDigest.isEqual(expected, actual);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX + "$");
    }

    private static byte[] pbkdf2(String password,
                                 byte[] salt,
                                 int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_BITS
        );

        try {
            return SecretKeyFactory
                    .getInstance(ALGORITHM)
                    .generateSecret(spec)
                    .getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Password hashing is unavailable.",
                    e
            );

        } finally {
            spec.clearPassword();
        }
    }
}
