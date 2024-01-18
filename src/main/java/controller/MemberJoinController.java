package controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import response.HttpResponse;
import service.MemberJoinService;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class MemberJoinController implements Controller {
    MemberJoinService memberJoinService = new MemberJoinService();
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String fullUrl = request.getUrl();
        String queryString = fullUrl.substring(fullUrl.indexOf("?") + 1);
        Map<String, String> params = parse(queryString);

        memberJoinService.createUser(params);


        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        response.setResponse("302", "FOUND", null, headers);
    }

    public static Map<String, String> parse(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            params.put(keyValue[0], keyValue[1]);
        }
        return params;
    }

}
