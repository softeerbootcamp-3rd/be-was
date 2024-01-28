package exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String path;

    public ResourceNotFoundException(String path) {
        super();
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
