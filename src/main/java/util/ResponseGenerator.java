package util;

import http.response.Response;

import java.io.*;

public class ResponseGenerator {
    public Response createHttpResponse(OutputStream out) throws IOException {
        return new Response(new DataOutputStream(out));
    }
}
