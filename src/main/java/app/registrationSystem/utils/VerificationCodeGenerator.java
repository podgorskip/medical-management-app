package app.registrationSystem.utils;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class VerificationCodeGenerator {
    private static final int MAX_NUMBER = 999999999;

    public static String generateVerificationCode() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(MAX_NUMBER + 1));
    }
}
