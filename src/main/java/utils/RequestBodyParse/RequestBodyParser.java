package utils.RequestBodyParse;

import webserver.http.constants.ContentType;

import java.util.Map;

public class RequestBodyParser {
    private RequestBodyParser() {}

    public static Map<String, String> contentTypeBodyParse(ContentType contentType, char[] bodyContent){
        ParserFactory.ContentParser parser = ParserFactory.getParser(contentType);
        return parser.parse(bodyContent);
    }

}
