package controller;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import constant.HttpHeader;
import constant.HttpStatus;
import constant.MimeType;
import database.ImageRepository;
import model.Image;
import webserver.HttpResponse;

@Controller
public class ImageController {

    @RequestMapping(method = "GET", path = "/post/image")
    public static HttpResponse getImage(@RequestParam(value = "postId", required = true) Long postId) {
        Image image = ImageRepository.findByPostId(postId);
        if (image == null)
            return HttpResponse.of(HttpStatus.NOT_FOUND);

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, MimeType.PNG.getMimeType())
                .body(image.getData())
                .build();
    }
}
