package util;

import constant.ErrorCode;
import constant.MimeType;
import constant.FileConstant;
import exception.WebServerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManager {

    public static byte[] getFileBytes(String filePath) throws IOException {
        // 파일이 존재하는 폴더 (기본 경로) 설정
        String folder = FileConstant.HTML_BASE_FOLDER;
        if (!filePath.endsWith(".html")) { folder = FileConstant.SUPPORT_FILE_BASE_FOLDER; }

        // 파일이 존재하면 읽고 바이트코드로 변환해 반환
        File file = new File(folder + filePath);
        if (!file.exists() || !file.isFile()) {
            throw new WebServerException(ErrorCode.PAGE_NOT_FOUND);
        }

        return fileToByte(file);
    }

    public static MimeType getMimeType(String path) {
        if (path == null || !path.contains(".") )
            throw new WebServerException(ErrorCode.PAGE_NOT_FOUND);

        String extension = path.substring(path.lastIndexOf("."));
        return MimeType.of(extension);
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
