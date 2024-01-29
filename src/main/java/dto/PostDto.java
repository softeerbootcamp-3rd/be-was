package dto;

public class PostDto {

    private long postId;
    private String title;
    private String content;
    private String userId;
    private String date;

    public PostDto(long postId, String title, String content, String userId, String date) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.date = date;
    }


}
