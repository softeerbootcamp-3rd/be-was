package controller;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import constant.HttpHeader;
import constant.HttpStatus;
import database.AttachmentRepository;
import model.Attachment;
import webserver.HttpResponse;

@Controller
public class AttachmentController {

    @RequestMapping(method = "GET", path = "/attachment")
    public static HttpResponse getAttachment(@RequestParam(value = "boardId", required = true) Long boardId) {
        Attachment attachment = AttachmentRepository.findByBoardId(boardId);
        if (attachment == null)
            return HttpResponse.of(HttpStatus.NOT_FOUND);

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, attachment.getMimeType())
                .body(attachment.getData())
                .build();
    }

    @RequestMapping(method = "GET", path = "/attachment/download")
    public static HttpResponse downloadAttachment(@RequestParam(value = "boardId", required = true) Long boardId) {
        Attachment attachment = AttachmentRepository.findByBoardId(boardId);
        if (attachment == null)
            return HttpResponse.of(HttpStatus.NOT_FOUND);

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, attachment.getMimeType())
                .addHeader(HttpHeader.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"")
                .body(attachment.getData())
                .build();
    }
}
