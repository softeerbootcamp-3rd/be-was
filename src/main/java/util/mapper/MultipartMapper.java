package util.mapper;

import com.google.common.base.Strings;
import constant.HttpHeader;
import constant.ParamType;
import util.ByteArrayUtils;
import util.ByteReader;
import util.web.SharedData;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

        byte[][] parts = ByteArrayUtils.splitByDelimiter(SharedData.request.get().getBody(), ("--" + boundary).getBytes());
        for (byte[] part : parts) {
            part = ByteArrayUtils.trim(part);
            if (part.length != 0) {
                parsePart(part, paramMap, fileMap);
            }
        }
    }

    private static void parsePart(byte[] part, Map<String, String> paramMap, Map<String, MultipartFile> fileMap) throws IOException {
        InputStream inputStream = byteArrayToInputStream(part);
        ByteReader reader = new ByteReader(inputStream);

        String contentDisposition = reader.readLine();
        String name = extractEntry(contentDisposition, "name");
        if (Strings.isNullOrEmpty(name))
            return;
        boolean isFile = contentDisposition.contains("filename");

        String s;
        Map<HttpHeader, String> partValues = new HashMap<>();
        while ((s = reader.readLine()) != null && !s.isEmpty()) {
            String[] splitParts = s.split(":\\s*", 2);
            if (splitParts.length == 2) {
                try {
                    partValues.put(HttpHeader.of(splitParts[0]), splitParts[1]);
                } catch (IllegalArgumentException ignored) {}
            }
        }
        byte[] data = new byte[part.length];
        int readLength = reader.read(data);
        data = Arrays.copyOfRange(data, 0, readLength);

        if (isFile) {
            MultipartFile file = new MultipartFile();
            file.setFilename(extractEntry(contentDisposition, "filename"));
            file.setContentType(partValues.get(HttpHeader.CONTENT_TYPE));
            file.setData(data);
            fileMap.put(name, file);
        } else {
            paramMap.put(name.replace("\"", ""), new String(data).trim());
        }
    }

    private static InputStream byteArrayToInputStream(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }

    private static String extractEntry(String contentDisposition, String entry) {
        String[] tokens = contentDisposition.split(";");

        for (String token : tokens) {
            if (token.trim().startsWith(entry + "=")) {
                String result = token.trim().substring((entry + "=").length());
                return result.substring(1, result.length() - 1);
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
