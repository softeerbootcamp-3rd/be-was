package util;

import java.util.Arrays;

public class ByteArrayUtils {

    public static byte[] trim(byte[] byteArray) {
        int start = 0;
        int end = byteArray.length - 1;
        while (start <= end && (byteArray[start] == ' ' || byteArray[start] == '\r' || byteArray[start] == '\n')) {
            start++;
        }
        while (end >= start && (byteArray[end] == ' ' || byteArray[end] == '\r' || byteArray[end] == '\n')) {
            end--;
        }
        byte[] trimmedArray = new byte[end - start + 1];
        System.arraycopy(byteArray, start, trimmedArray, 0, trimmedArray.length);
        return trimmedArray;
    }

    public static byte[][] splitByDelimiter(byte[] input, byte[] delimiter) {
        int count = 0;
        int index = 0;
        while ((index = indexOf(input, delimiter, index)) != -1) {
            count++;
            index += delimiter.length;
        }
        byte[][] result = new byte[count + 1][];
        int start = 0;
        int partIndex = 0;
        for (int i = 0; i < input.length; i++) {
            if (startsWith(input, delimiter, i)) {
                result[partIndex++] = Arrays.copyOfRange(input, start, i);
                start = i + delimiter.length;
                i += delimiter.length - 1;
            }
        }
        result[partIndex] = Arrays.copyOfRange(input, start, input.length);
        return result;
    }

    private static int indexOf(byte[] source, byte[] target, int fromIndex) {
        outer:
        for (int i = fromIndex; i <= source.length - target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (source[i + j] != target[j])
                    continue outer;
            }
            return i;
        }
        return -1;
    }

    private static boolean startsWith(byte[] source, byte[] prefix, int fromIndex) {
        for (int i = 0; i < prefix.length; i++) {
            if (source[fromIndex + i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}