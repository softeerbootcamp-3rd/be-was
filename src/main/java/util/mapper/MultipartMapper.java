package util.mapper;

import com.google.common.base.Strings;
import constant.ParamType;
import util.CharArraySplitter;
import util.web.SharedData;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultipartMapper {
    public static <T> T mapMultipartFile(String[] entries, Class<T> clazz) throws IOException {
        Map<String, String> paramMap = new HashMap<>();
        Map<String, MultipartFile> fileMap = new HashMap<>();

        mapMultipartFileEntries(entries, paramMap, fileMap);
        return createObject(clazz, paramMap, fileMap);
    }

    private static void mapMultipartFileEntries(String[] entries, Map<String, String> paramMap, Map<String, MultipartFile> fileMap) throws IOException {
        String boundary = entries[1].substring(entries[1].indexOf('=') + 1);

        char[][] parts = CharArraySplitter.splitByDelimiter(SharedData.request.get().getBody(), ("--" + boundary).toCharArray());
        for (char[] part : parts) {
            part = CharArraySplitter.trim(part);
            if (part.length != 0) {
                parsePart(part, paramMap, fileMap);
            }
        }
    }

    private static void parsePart(char[] part, Map<String, String> paramMap, Map<String, MultipartFile> fileMap) throws IOException {
        InputStream inputStream = charArrayToInputStream(part);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println(new String(part));
        String contentDisposition = reader.readLine();
        String name = extractEntry(contentDisposition, "name").replace("\"", "");
        if (Strings.isNullOrEmpty(name))
            return;
        boolean isFile = contentDisposition.contains("filename");

        String s;
        int contentLength = part.length;
        while ((s = reader.readLine()) != null && !s.isEmpty()) {
            System.out.println(s);
            contentLength -= s.getBytes().length;
        }
        char[] data = new char[contentLength];
        int readLength = reader.read(data);
        data = Arrays.copyOfRange(data, 0, readLength);
        System.out.println(data.length);

        if (isFile) {
            MultipartFile file = new MultipartFile();
            file.setFilename(extractEntry(contentDisposition, "name"));
            file.setData(charArrayToByteArray(data));
            fileMap.put(name, file);
        } else {
            paramMap.put(name.replace("\"", ""), new String(data).trim());
        }
    }

    private static InputStream charArrayToInputStream(char[] charArray) {
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        return new ByteArrayInputStream(byteArray);
    }

    private static byte[] charArrayToByteArray(char[] charArray) {
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        return byteArray;
    }

    private static String extractEntry(String contentDisposition, String entry) {
        String[] tokens = contentDisposition.split(";");

        for (String token : tokens) {
            if (token.trim().startsWith(entry + "=")) {
                return token.trim().substring((entry + "=").length());
            }
        }
        return "";
    }

    private static <T> T createObject(Class<T> clazz, Map<String, String> paramMap, Map<String, MultipartFile> fileMap) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                if (fileMap.containsKey(fieldName)) {
                    field.set(instance, fileMap.get(fieldName));
                } else if (paramMap.containsKey(fieldName)) {
                    ParamType paramType = ParamType.getByClass(field.getType());
                    field.set(instance, paramType.map(paramMap.get(fieldName)));
                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Error creating object", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
