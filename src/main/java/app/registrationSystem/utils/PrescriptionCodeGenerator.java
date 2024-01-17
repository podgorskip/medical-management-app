package app.registrationSystem.utils;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class PrescriptionCodeGenerator {
    private static final int LENGTH = 4;
    private static final int MAX_CODE = 99999;
    private static final Set<Integer> usedCodes = new HashSet<>();

    public static String generatePrescriptionCode() {
        Random random = new Random();
        int code;

        do {
            code = random.nextInt(MAX_CODE + 1);
            System.out.println(code);
        } while (usedCodes.contains(code));

        usedCodes.add(code);

        return String.format("%05d", code);
    }
}
