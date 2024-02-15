package util;

import file.MultipartFile;
import type.MimeType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class MultipartParser {

    public static List<MultipartFile> parseMultipartData(byte[] multipartData, String boundary) {
        List<MultipartFile> files = new ArrayList<>();

        int currentIndex = 0;
        while ((currentIndex = findNextBoundaryIndex(multipartData, boundary, currentIndex)) != -1) {
            int start = currentIndex + boundary.length();
            int end = findNextBoundaryIndex(multipartData, boundary, start);

            if (end == -1) {
                break;
            }

            MultipartFile file = parsePartData(multipartData, start, end);
            if (file != null) {
                files.add(file);
            }

            currentIndex = end;
        }

        return files;
    }

    private static MultipartFile parsePartData(byte[] multipartData, int start, int end) {
        int headerEndIndex = indexOf(multipartData, "\r\n\r\n".getBytes(), start);

        if (headerEndIndex == -1 || headerEndIndex >= end) {
            return null;
        }

        // Header 파싱
        byte[] headersBytes = Arrays.copyOfRange(multipartData, start, headerEndIndex);
        String headers = new String(headersBytes);

        String fieldName = extractFieldFromHeaders(headers);
        String fileName = extractFileNameFromHeaders(headers);
        String contentType = extractContentTypeFromHeaders(headers);

        // Body 파싱
        int contentStartIndex = headerEndIndex + 4;
        byte[] content = Arrays.copyOfRange(multipartData, contentStartIndex, end - 4);

        if (fieldName.isEmpty()) {
            return null;
        }

        return new MultipartFile(fieldName, fileName, contentType, content);
    }


    private static String extractFieldFromHeaders(String headers) {
        String[] dispositionParts = headers.split(";");

        for (String part : dispositionParts) {
            if (part.trim().startsWith("name=")) {
                return part.trim().substring(6, part.length() - 2);
            }
        }

        return null;
    }

    private static String extractFileNameFromHeaders(String headers) {
        String[] dispositionParts = headers.split(";");

        for (String part : dispositionParts) {
            if (part.trim().startsWith("filename=")) {
                String filename = part.trim().substring("filename=".length()).split("\\r?\\n")[0];
                return filename.replaceAll("\"", "");
            }
        }

        return null;
    }

    private static String extractContentTypeFromHeaders(String headers) {
        String[] headerLines = headers.split("\\r?\\n");

        for (String line : headerLines) {
            if (line.startsWith("Content-Type:")) {
                return line.substring("Content-Type:".length()).trim();
            }
        }

        return MimeType.TEXT.getContentType();
    }

    private static int findNextBoundaryIndex(byte[] data, String boundary, int startIndex) {
        if (startIndex > data.length)
            return -1;

        int boundaryIndex = indexOf(data, boundary.getBytes(), startIndex);
        return (boundaryIndex == -1) ? data.length : boundaryIndex;
    }

    private static int indexOf(byte[] haystack, byte[] needle, int startIndex) {
        for (int i = startIndex; i < haystack.length - needle.length + 1; i++) {
            boolean found = true;
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

}