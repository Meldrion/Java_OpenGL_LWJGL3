package lu.innocence.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {


    private Utils() {

    }

    public static String loadResource(String fileName) throws IOException {

        String result;
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

}
