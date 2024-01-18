package dto;

public class ResourceDto {
    private String path;

    public ResourceDto(String path) {
        this.path = path;
    }

    public static ResourceDto of(String path) {
        return new ResourceDto(path);
    }

    public String getPath() {
        return path;
    }
}
