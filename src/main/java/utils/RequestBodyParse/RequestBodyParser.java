package utils.RequestBodyParse;

import webserver.http.ContentType;

import java.util.List;
import java.util.Map;

public class RequestBodyParser {
    private final char[] bodyContent;
    public RequestBodyParser(char[] bodyContent) {
        this.bodyContent = bodyContent;
    }

    public Map<String, String> contentTypeBodyParse(ContentType contentType){
        ParserFactory.ContentParser parser = ParserFactory.getParser(contentType);
        return parser.parse(bodyContent);
    }

}
