package webApplicationServer.controller;

import dto.HttpResponseDto;
import model.http.Body;
import model.http.ContentType;
import model.http.Status;
import model.http.response.HttpResponse;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;

public class FakeHttpResponseDto extends HttpResponseDto {
    public FakeHttpResponseDto() {
        super();
    }
}
