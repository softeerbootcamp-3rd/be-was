package view;

import db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewResolver {

    public static byte[] resolve(String url, Boolean isAuth, Map<String, Object> model) {
        String originalUrl = url;
        if (url.endsWith(".html")) {
            url = "src/main/resources/templates" + url;
        } else {
            url = "src/main/resources/static" + url;
        }

        File file = new File(url);
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] body = fis.readAllBytes();
                if (url.endsWith(".html")) {
                    String content = new String(body, "UTF-8");
                    content = replaceWord(content, "<ul class=\"list\"></ul>", View.CONTENT_LIST.generate(Database.getPostList()));
                    if(isAuth) {
                        content = replaceWord(content, "로그인", (String)model.get("name"));

                        if (originalUrl.equals("/user/list.html")) {
                            content = replaceWord(content, "<tbody></tbody>", View.USER_LIST.generate(Database.findAll()));
                        }

                        if (originalUrl.equals("/post_detail.html")) {
                            content = replaceWord(content, "<div class=\"panel panel-default\"><div class=\"article-utils\">", View.POST_DETAIL.generate(model.get("postId")));
                        }
                    }
                    body = content.getBytes("UTF-8");
                }
                return body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }

    }

    private static String replaceWord(String content, String targetWord, String replacement) {
        // 정규 표현식 패턴 생성
        String regex = "\\Q" + targetWord + "\\E";  // 정규 표현식 특수 문자를 이스케이프
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

        // 패턴과 일치하는 단어 찾기
        Matcher matcher = pattern.matcher(content);

        // 대체된 내용으로 문자열 빌드
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

}
