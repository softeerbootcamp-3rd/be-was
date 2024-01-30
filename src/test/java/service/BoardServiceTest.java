package service;

import db.BoardDatabase;
import exception.PostException;
import exception.WebException;
import model.Comment;
import model.Post;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardServiceTest {
    private final BoardService boardService = new BoardService();

    @Test
    void 글쓰기성공() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        String title = "오늘의 점심은";
        String contents = "순대국밥입니다.";

        //when
        Long postId = boardService.write(writer, title, contents);

        //then
        Post post = BoardDatabase.findPostById(postId);
        Assertions.assertEquals(writer, post.getWriter());
        Assertions.assertEquals(title, post.getTitle());
        Assertions.assertEquals(contents, post.getContents());
    }

    @Test
    void 글쓰기null값이들어왔을때() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        String title = "오늘의 점심은";
        String contents = null;

        //When
        PostException e = assertThrows(PostException.class,
                () -> boardService.write(writer, title, contents));


        //Then
        assertEquals(PostException.NULL_CONTENTS, e.getMessage());
    }

    @Test
    void 게시글수정성공() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        String title = "오늘의 점심은";
        String contents = "순대국밥입니다.";
        Long postId = boardService.write(writer, title, contents);
        String newContents = "피자와 파스타입니다.";

        //when
        Long id = boardService.update(postId, title, newContents);

        //then
        Post post = BoardDatabase.findPostById(postId);
        Assertions.assertEquals(writer, post.getWriter());
        Assertions.assertEquals(title, post.getTitle());
        Assertions.assertEquals(newContents, post.getContents());
    }

    @Test
    void 댓글달기성공() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");
        User commenter = new User("ronaldo", "1234", "호날두", "ronaldo@hyundai.com");
        String commentBody = "순대국밥 좋아요!";

        //when
        Long commentId = boardService.comment(postId, commenter, commentBody);

        //then
        Post post = BoardDatabase.findPostById(postId);
        List<Comment> comments = post.getComments();
        Comment comment1 = comments.get(0);

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(commentId, comment1.getCommentId());
        Assertions.assertEquals(commenter, comment1.getWriter());
        Assertions.assertEquals(commentBody, comment1.getBody());
    }

    @Test
    void 댓글두개() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");
        User commenter = new User("ronaldo", "1234", "호날두", "ronaldo@hyundai.com");

        //when
        boardService.comment(postId, commenter, "순대국밥 좋아요!");
        boardService.comment(postId, writer, "감사합니다!");

        //then
        Post post = BoardDatabase.findPostById(postId);
        List<Comment> comments = post.getComments();

        Assertions.assertEquals(2, comments.size());
    }

    @Test
    void 게시글삭제성공() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");

        //when
        boardService.delete(postId, writer);

        //then
        Post post = BoardDatabase.findPostById(postId);

        Assertions.assertNull(post);
    }

    @Test
    void 게시글삭제미로그인시() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");

        //when
        WebException e = assertThrows(WebException.class,
                () -> boardService.delete(postId, null));

        //then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), e.getStatus());
        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }

    @Test
    void 게시글삭제게시글작성자와다른유저가접근시() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");
        User other = new User("ronaldo", "1234", "호날두", "ronaldo@hyundai.com");

        //when
        WebException e = assertThrows(WebException.class,
                () -> boardService.delete(postId, other));

        //then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), e.getStatus());
        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage());
    }

    @Test
    void 댓글삭제성공() {
        //given
        User writer = new User("gyeongsu", "1234", "최경수", "gyeongsu@hyundai.com");
        Long postId = boardService.write(writer, "오늘의 점심은", "순대국밥입니다.");
        User commenter = new User("ronaldo", "1234", "호날두", "ronaldo@hyundai.com");
        Long commentId = boardService.comment(postId, commenter, "순대국밥 좋아요!");

        //when
        boardService.deleteComment(postId, commentId);

        //then
        Post post = BoardDatabase.findPostById(postId);
        List<Comment> comments = post.getComments();

        Assertions.assertEquals(0, comments.size());
    }
}
