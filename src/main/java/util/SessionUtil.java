package util;

import java.util.Random;

public class SessionUtil {
    // length 자릿수 만큼의 랜덤한 숫자 문자열 생성
    public static String createSessionId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<length; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }
}
