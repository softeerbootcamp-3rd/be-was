package util;

import file.MultipartFile;
import type.MimeType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MultipartParser {

    public static List<MultipartFile> parseMultipartData(String multipartString, String boundary) {
        String[] parts = multipartString.split(boundary);
        List<MultipartFile> files = new ArrayList<>();

        for (String part : parts) {
            part = part.trim();
            if (part.length() == 0) {
                continue;
            }

            MultipartFile file = null;
            if (part.contains("filename=")) {
                // 파일 데이터 파싱
                file = parseFilePart(part);
            } else {
                // 일반 텍스트 데이터 파싱
                file = parseTextPart(part);
            }

            if (file != null)
                files.add(file);
        }

        return files;
    }

    private static MultipartFile parseTextPart(String textPart) {
        String[] lines = textPart.split("\\r?\\n");

        String fieldName = "";
        String fieldValue = "";

        for (String line : lines) {
            if (line.startsWith("Content-Disposition")) {
                fieldName = line.split("\"")[1];
            } else if (!line.startsWith("--")) {
                fieldValue = line;
            }
        }

        if (fieldName == "" ||  fieldValue == "") {
            return null;
        }

        return new MultipartFile(fieldName, MimeType.TEXT.getContentType(), fieldValue.getBytes());
    }

    private static MultipartFile parseFilePart(String filePart) {
        String[] lines = filePart.split("\\r?\\n");

        String fieldName = "";
        String fileName = "";
        String contentType = "";
        StringBuilder fileContent = new StringBuilder();

        boolean contentStarted = false;

        for (String line : lines) {
            if (line.startsWith("Content-Disposition")) {
                fieldName = line.split("\"")[1];
                String[] parts = line.split("\"");
                if (parts.length >= 4) {
                    fileName = parts[3];
                } else {
                    return null;
                }
            } else if (line.startsWith("Content-Type:")) {
                contentType = line.split(":")[1].trim();
                contentStarted = true;
            } else if (contentStarted && !line.startsWith("--")) {
                fileContent.append(line);
            }
        }

        return new MultipartFile(fieldName, contentType, fileContent.toString().getBytes());
    }

}