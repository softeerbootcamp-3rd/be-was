package controller;

import annotation.*;
import dao.FileDao;
import dao.PostDao;
import dto.PostDto;
import file.MultipartFile;
import model.File;
import model.Post;
import type.MimeType;
import util.MultipartParser;
import util.SessionManager;
import util.StringParser;
import webserver.http.HttpHeader;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/post")
public class PostController {

    @PostMapping(path = "/upload")
    public static ResponseEntity postUpload(HttpRequest httpRequest) throws IOException {

        String requestBody = new String(httpRequest.getByteBody());
        String SID = StringParser.getCookieValue(httpRequest.getCookie(), "SID");
        String userId = (String) SessionManager.getAttribute(SID, "user");

        List<MultipartFile> files = MultipartParser.parseMultipartData(requestBody, httpRequest.getBoundary());
        String title = "";
        String contents = "";
        byte[] fileContent = null;
        for (MultipartFile m : files) {
            if (m.getFieldName().equals("title")) {
                title = new String(m.getContent());
                continue;
            }

            if (m.getFieldName().equals("contents")) {
                contents = new String(m.getContent());
                continue;
            }

            if (m.getFieldName().equals("file")) {
                fileContent = m.getContent();
            }
        }

        Post post = new Post(title, contents, userId);
        int postId = PostDao.insertPost(post);
        if (fileContent != null) {
            File file = new File(postId, fileContent);
            FileDao.insertFile(file);
        }

        Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/index.html"));
        return new ResponseEntity<>(
                HttpStatus.FOUND,
                headerMap
        );
    }

    @GetMapping(path = "/list")
    @ResponseBody
    public static ResponseEntity getAllPostList() {

        List<PostDto> postList = PostDao.findAll();

        Map<String, List<String>> headerMap = new HashMap<>();
        return new ResponseEntity<>(
                HttpStatus.OK,
                headerMap,
                postList
        );

    }

    @GetMapping(path = "/detail")
    @ResponseBody
    public static ResponseEntity getPostDetail(@RequestParam(name = "postId") int postId) {

        PostDto postDto = PostDao.findPostByPostId(postId);

        Map<String, List<String>> headerMap = new HashMap<>();
        return new ResponseEntity<>(
                HttpStatus.OK,
                headerMap,
                postDto
        );
    }

}
