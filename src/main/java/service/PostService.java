package service;

import db.PostDatabase;
import java.time.LocalDateTime;
import java.util.Map;

public class PostService {

    /**
     * 포스트를 저장합니다.
     *
     * <p> 만약 제목이 입력되지 않았다면 오류를 발생시킵니다.
     *
     * @param writer 작성자
     * @param params 제목, 본문 정보
     * @param time 작성 시간
     * @throws IllegalArgumentException 제목이 비어있는 경우 발생
     */
    public void createPost(String writer, Map<String, String> params, LocalDateTime time)
            throws IllegalArgumentException {
        String title = params.get("title");
        String contents = params.get("contents");

        if (title.isEmpty()) {
            throw new IllegalArgumentException("title is required.");
        }

        PostDatabase.addPost(writer, title, contents, time);
    }
}
