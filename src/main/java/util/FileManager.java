package util;

import constant.FileContentType;
import constant.FilePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManager {

    public static byte[] getFileByPath(FilePath basePath, String path) throws IOException {
        File file = new File(basePath.getPath() + path);
        if (file.exists()) {
            return fileToByte(file);
        }

        return null;
    }

    public static String getContentType(String path) throws IOException {
        int extensionPoint = path.lastIndexOf(".");
        return FileContentType.of(path.substring(extensionPoint)).getContentType();
    }

    private static byte[] fileToByte(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            long fileSize = file.length();

            if (fileSize > Integer.MAX_VALUE) {
                throw new IOException("파일 크기가 너무 큽니다.");
            }

            int fileSizeInt = (int) fileSize;
            byte[] fileBytes = new byte[fileSizeInt];

            int bytesRead;
            int offset = 0;

            while (offset < fileSizeInt && (bytesRead = fileInputStream.read(fileBytes, offset, fileSizeInt - offset)) >= 0) {
                offset += bytesRead;
            }

            if (offset < fileSizeInt) {
                throw new IOException(file.getName() + "파일을 완전히 읽지 못했습니다.");
            }

            return fileBytes;
        }
    }
}
