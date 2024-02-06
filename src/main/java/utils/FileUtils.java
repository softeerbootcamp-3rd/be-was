package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtils {

    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                byte[] data = new byte[(int) file.length()];
                bis.read(data);
                return data;
            }
        }
        return null;
    }
    public static String readFileAsString(String filePath) throws IOException {
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next();
            }
        }
    }
}
