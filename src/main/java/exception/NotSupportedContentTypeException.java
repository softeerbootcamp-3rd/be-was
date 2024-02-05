package exception;

public class NotSupportedContentTypeException extends RuntimeException{
    public NotSupportedContentTypeException() {
        super("지원하지 않는 컨텐츠 타입입니다.");
    }
}
