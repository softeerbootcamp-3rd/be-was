package util;

import java.util.Arrays;

public class CharArraySplitter {

    public static char[] trim(char[] charArray) {
        int start = 0;
        int end = charArray.length - 1;
        while (start <= end && (charArray[start] == ' ' || charArray[start] == '\r' || charArray[start] == '\n')) {
            start++;
        }
        while (end >= start && (charArray[end] == ' ' || charArray[end] == '\r' || charArray[end] == '\n')) {
            end--;
        }
        char[] trimmedArray = new char[end - start + 1];
        System.arraycopy(charArray, start, trimmedArray, 0, trimmedArray.length);

        return trimmedArray;
    }

    public static char[][] splitByDelimiter(char[] input, char[] delimiter) {
        int count = 0;
        int index = 0;
        while ((index = indexOf(input, delimiter, index)) != -1) {
            count++;
            index += delimiter.length;
        }
        char[][] result = new char[count + 1][];
        int start = 0;
        int partIndex = 0;
        for (int i = 0; i < input.length; i++) {
            if (startsWith(input, delimiter, i)) {
                result[partIndex++] = Arrays.copyOfRange(input, start, i);
                start = i + delimiter.length;
                i += delimiter.length - 1; // 다음 반복에서 중복된 문자를 방지
            }
        }
        result[partIndex] = Arrays.copyOfRange(input, start, input.length);
        return result;
    }

    private static int indexOf(char[] source, char[] target, int fromIndex) {
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

    private static boolean startsWith(char[] source, char[] prefix, int fromIndex) {
        for (int i = 0; i < prefix.length; i++) {
            if (source[fromIndex + i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}