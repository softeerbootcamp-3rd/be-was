package webserver.view;

import http.HttpContentType;
import http.HttpStatus;
import http.Request;
import http.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;

import java.io.*;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThymeleafView implements View {
    private static final Logger logger = LoggerFactory.getLogger(ThymeleafView.class);
    private String viewPath;
    public ThymeleafView(String viewPath) {
        this.viewPath = viewPath;
    }
    @Override
    public String getContentType() {
        int dotIndex = viewPath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < viewPath.length() - 1) {
            String fileExtension = viewPath.substring(dotIndex + 1);
            return HttpContentType.getValue(fileExtension);
        }
        return null;
    }

    @Override
    public void render(Request request, Response response, Model model) {
        try {
            forward(request, response, model);
        } catch (IOException e){
            return;
        }
    }

    public String getViewPath() {
        return viewPath;
    }

    private void forward(Request request, Response response,Model model) throws IOException {
        File file = new File(viewPath);

        byte[] body;
        if (file.exists() && file.isFile()) {
            String fileContent = new String(readAllBytes(file));

            if(model!=null) {
                fileContent = processFileContent(fileContent, model);
            }
            body = fileContent.getBytes();
            response.setStatus(HttpStatus.OK);
        }
        else{
            body = readAllBytes(new File("/not-found.html"));
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        response.send(body,request);
    }
    private String processFileContent(String fileContent, Model model) {
        // 정규 표현식을 사용하여 <script> 태그 내의 {{표현식}} 찾기
        String patternStr = "<script[^>]*>([\\s\\S]*?)<\\/script>";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(fileContent);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            // 매치된 스크립트 내부에서 {{표현식}} 찾기
            String expressionPatternStr = "\\{\\{([^}]*)\\}\\}";
            Pattern expressionPattern = Pattern.compile(expressionPatternStr);
            Matcher expressionMatcher = expressionPattern.matcher(matcher.group(1));
            StringBuffer scriptResult = new StringBuffer();
            while (expressionMatcher.find()) {
                String expression = expressionMatcher.group(1);
                String evaluatedValue = evaluateInnerExpression(expression, model);
                expressionMatcher.appendReplacement(scriptResult, "\'"+evaluatedValue+"\'");

            }
            expressionMatcher.appendTail(scriptResult);
            String replacement = scriptResult.length() > 0 ? "<script>" + scriptResult.toString() : matcher.group(0);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement + "</script>"));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String evaluateInnerExpression(String innerExpression, Model model) {
        String[] expressionParts = innerExpression.split("\\.");
        Object target = model.getAttribute(expressionParts[0]);
        Object result = target;
        if(result == null){
            return "";
        }
        try {
            for (int i = 1; i < expressionParts.length; i++) {
                String propertyName = expressionParts[i];

                if (propertyName.endsWith("()")) {
                    // 메소드 호출인 경우
                    propertyName = propertyName.substring(0, propertyName.length() - 2);
                    Class<?> targetClass = result.getClass();
                    result = targetClass.getMethod(propertyName).invoke(result);
                } else {
                    // 필드 또는 속성 접근인 경우
                    Field field = result.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    result = field.get(result);
                }
            }

            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private byte[] readAllBytes(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
}
