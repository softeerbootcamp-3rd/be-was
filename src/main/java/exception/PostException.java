package exception;

public class PostException extends RuntimeException {
    public static final String NULL_WRITER = "Handsome Honux";
    public static final String NULL_TITLE = "제목을 입력해 주세요.";
    public static final String NULL_CONTENTS = "내용을 입력해 주세요.";


    public PostException(String message) {
        super(message);
    }
}
