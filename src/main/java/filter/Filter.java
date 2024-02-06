package filter;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface Filter {
    public void init();
    public void doFilter(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
    public void destroy();
}
