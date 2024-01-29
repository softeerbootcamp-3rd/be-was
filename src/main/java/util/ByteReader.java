package util;

import com.google.common.base.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteReader {

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
        if (Strings.isNullOrEmpty(result))
            return "";
        return result.substring(0, result.length() - 1);
    }

    public int read(byte[] bytes) throws IOException {
        return is.read(bytes);
    }
}
