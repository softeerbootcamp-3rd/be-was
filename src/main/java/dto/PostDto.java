package dto;

public class PostDto {

    private int postId;
    private String title;
    private String content;
    private String userName;
    private String date;

    public PostDto(int postId, String title, String content, String userName, String date) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.date = date;
    }


}
