package util.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

public class HtmlTemplate {
    private static final Logger logger = LoggerFactory.getLogger(HtmlTemplate.class);
    private static final String routePath = "src/main/resources/templates/templates/";

    private String filePath;
    private String template;
    private Function<String, String> loggedInFunction;
    private Function<String, String> loggedOutFunction;

    public HtmlTemplate(String filePath, Function<String, String> loggedInFunction, Function<String, String> loggedOutFunction) {
        this.filePath = routePath + filePath;
        this.template = readFileToString(this.filePath);
        this.loggedInFunction = loggedInFunction;
        this.loggedOutFunction = loggedOutFunction;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Function<String, String> getLoggedInFunction() {
        return loggedInFunction;
    }

    public void setLoggedInFunction(Function<String, String> loggedInFunction) {
        this.loggedInFunction = loggedInFunction;
    }

    public Function<String, String> getLoggedOutFunction() {
        return loggedOutFunction;
    }

    public void setLoggedOutFunction(Function<String, String> loggedOutFunction) {
        this.loggedOutFunction = loggedOutFunction;
    }


    private static String readFileToString(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "";
        }
        return content.toString();
    }
}
