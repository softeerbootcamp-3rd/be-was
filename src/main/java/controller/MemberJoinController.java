package controller;

import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberJoinService;

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
        response.setResponse(HttpResponseStatus.FOUND, null, headers);
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
