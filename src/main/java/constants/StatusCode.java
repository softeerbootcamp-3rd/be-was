package constants;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatusCode {

    OK(200, "OK"),
    REDIRECT(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    UNPROCESSABLE_CONTENT(422, "Unprocessable Content");

    private final int code;
    private final String name;

    /**
     * 상태코드와 열거체의 이름을 각각 키와 값으로 갖는 자료구조입니다.
     */
    private static final Map<Integer, String> STATUS_CODE = Collections.unmodifiableMap(
            Stream.of(values())
                    .collect(Collectors.toMap(StatusCode::getCode, StatusCode::name)));

    StatusCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 상태코드를 파라미터로 받아 상태코드의 설명문을 얻습니다.
     *
     * <p> 찾지 못한다면 오류를 발생시킵니다.
     *
     * @param code 상태코드
     * @return 설명문
     * @throws NullPointerException 상태코드를 찾지 못한 경우 발생
     */
    public static String getCodeName(int code) {
        try {
            String type = STATUS_CODE.get(code);
            StatusCode statusCode = StatusCode.valueOf(type);
            return statusCode.getName();
        } catch (NullPointerException e) {
            throw new NullPointerException("no such code.");
        }
    }
}
