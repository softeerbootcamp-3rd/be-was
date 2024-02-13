package view;

import controller.dto.ListMapData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ViewMaker {

    private String path;
    private View view;

    public ViewMaker(String path, View view) {
        this.path = path;
        this.view = view;
    }

    //파일 동적으로 읽어오기
    public String readFile(View view) throws IOException {
        File file = new File(path);
        if (!file.exists())
            return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            boolean inFor = false;
            String forParam = null;
            StringBuilder forStringBuilder = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<!--ifLoggedIn-->")) {
                    String username = view.get("username", String.class);
                    if (username != null) {
                        line = line.strip().substring("<!--ifLoggedIn-->".length());
                        line = changeWord(line, "${username}", username);
                        line = changeWord(line, "${userId}", view.get("userId", String.class));
                        line = removeCommentSymbols(line);
                    }
                }
                if (line.contains("<!--ifNotLoggedIn-->")) {
                    if (view.get("username", String.class) == null) {
                        line = line.strip().substring("<!--ifNotLoggedIn-->".length());
                        line = removeCommentSymbols(line);
                    }
                }

                if (line.contains("<!--change-->")) {
                    line = line.strip().substring("<!--change-->".length());
                    String[] from = line.split("\\$");
                    for (String f : from) {
                        int startIndex = f.indexOf("{");
                        int endIndex = f.indexOf("}");
                        if(startIndex!=-1 && endIndex!=-1) {
                            String token = f.substring(startIndex+1, endIndex);
                            line = changeWord(line, "${"+token+"}", view.get(token, String.class));
                        }
                    }
                }

                if (line.contains("<!--forStart-->")) {
                    line = line.strip().substring("<!--forStart-->".length());
                    line = removeCommentSymbols(line);
                    forParam = line;
                    inFor = true;
                    forStringBuilder = new StringBuilder();
                    continue;
                } else if (line.contains("<!--forEnd-->")) {
                    if (forStringBuilder != null) {
                        String forForm = forStringBuilder.toString();
                        ListMapData listMapData = view.get(forParam, ListMapData.class);
                        for (int i = 0; i < listMapData.getListSize(); i++) {
                            String changed = new String(forForm);
                            changed = changeHtml(changed, listMapData.getMap(i));
                            changed = changeWord(changed, "${seq}", Integer.toString(i + 1));
                            stringBuilder.append(changed);
                        }
                    }
                    inFor = false;
                    forStringBuilder = null;
                    forParam=null;
                    continue;
                }
                if (inFor) {
                    forStringBuilder.append(removeCommentSymbols(line));
                } else {
                    stringBuilder.append(line).append("\n");
                }
            }

            // 마지막의 "\n"을 제거
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

    }

    private String removeCommentSymbols(String line) {
        if (line.contains("<!--")) line = line.strip().substring("<!--".length());
        if (line.contains("-->")) line = line.substring(0, line.length() - "-->".length());
        return line;
    }

    private String changeWord(String line, String from, String to) {
        if (from == null || from.isEmpty() || line == null || line.isEmpty()) {
            return line;
        }
        return line.replace(from, to);
    }

    private String changeHtml(String line, Map<String, String> data) {
        for (String key : data.keySet()) {
            line = changeWord(line, "${" + key + "}", data.get(key));
        }
        return line;
    }
}

