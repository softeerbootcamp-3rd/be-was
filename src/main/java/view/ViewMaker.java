package view;

import http.HttpRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ViewMaker {

    private String path;
    private View view;

    public ViewMaker(String path, View view) {
        this.path = path;
        this.view = view;
    }
    //파일 동적으로 읽어오기
    public String readFile(HttpRequest request) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (request.getUserId()!=null && line.startsWith("<!--ifLoggedIn-->")) {
                        line = line.substring("<!--ifLoggedIn-->".length());
                        line = changeWord(line, "${username}", request.getUserId());
                        line = removeCommentSymbols(line);
                    }
                    if (request.getUserId()==null && line.startsWith("<!--ifNotLoggedIn-->")) {
                        line = line.substring("<!--ifNotLoggedIn-->".length());
                        line = removeCommentSymbols(line);
                    }
                    stringBuilder.append(line).append("\n");
                }

                // 마지막의 "\n"을 제거
                stringBuilder.setLength(stringBuilder.length() - 1);
                return stringBuilder.toString();
            }
        }
        return null;
    }

    private String removeCommentSymbols(String line) {
        if (line.startsWith("<!--"))  line = line.substring("<!--".length());
        if (line.endsWith("-->"))  line = line.substring(0, line.length() - "-->".length());
        return line;
    }

    private String changeWord(String line, String from, String to) {
        if (from == null || from.isEmpty() || line == null || line.isEmpty()) {
            return line;
        }
        return line.replace(from, to);
    }
}

