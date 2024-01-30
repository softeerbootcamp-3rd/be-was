package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ByteReader {
    private static final Logger logger = LoggerFactory.getLogger(ByteReader.class);


    private final InputStream is;

    public ByteReader(InputStream is) {
        this.is = is;
    }

    public String readLine() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int currentByte;
        boolean wasCarriageReturn = false;
        while ((currentByte = is.read()) != -1) {
            if (wasCarriageReturn && currentByte == '\n') {
                break;
            }
            byteArrayOutputStream.write(currentByte);
            wasCarriageReturn = (currentByte == '\r');
        }
        String result = byteArrayOutputStream.toString();
        if (result.isEmpty()) {
            return "";
        }
        return result.substring(0, result.length() - 1);
    }

    public int read(byte[] bytes) throws IOException {
        return is.read(bytes);
    }

    public byte[] readMultipartFileContent(ByteReader byteReader, int contentLength, String boundary) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        int totalBytesRead = 0;

        try {
            totalBytesRead += byteReader.readLine().length();
            totalBytesRead += byteReader.readLine().length();
            while (totalBytesRead < contentLength) {
                int bytesToRead = Math.min(buffer.length, contentLength - totalBytesRead);
                bytesRead = is.read(buffer, 0, bytesToRead);

                if (bytesRead <= 0) {
                    // 읽은 바이트 수가 0 이하인 경우 루프를 종료합니다.
                    break;
                }

                byteArrayOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // 바운더리 확인
                int boundaryIndex = indexOf(byteArrayOutputStream.toByteArray(), ("--" + boundary).getBytes(), 0);
                if (boundaryIndex != -1) {
                    byte[] tmp_buffer = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.reset();
                    byteArrayOutputStream.write(Arrays.copyOf(tmp_buffer, boundaryIndex));
                    break;  // 바운더리를 만나면 더 이상 읽지 않도록 종료
                }
            }
        } catch (IOException e) {
            logger.error("error: {}", e.getMessage());
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static int indexOf(byte[] source, byte[] target, int fromIndex) {
        outer:
        for (int i = fromIndex; i <= source.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (source[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
