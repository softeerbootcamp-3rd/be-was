package controller;

import java.io.IOException;
import java.io.OutputStream;
import service.HomeService;

public class HomeController {

    private static final HomeService homeService = new HomeService();

    public void route(String urn, OutputStream out) throws IOException {
        if (urn.equals("/") || urn.equals("/index.html")) {
            home(urn, out);
        } else {
            getStatic(urn, out);
        }
    }

    private void home(String urn, OutputStream out) throws IOException {
        homeService.getIndex(urn, out);
    }

    private void getStatic(String urn, OutputStream out) throws IOException {
        homeService.getStatic(urn, out);
    }
}
