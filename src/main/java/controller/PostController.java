package controller;

import annotation.*;
import dao.FileDao;
import dao.PostDao;
import dto.PostDto;
import file.MultipartFile;
import model.File;
import model.Post;
import util.MultipartParser;
import util.SessionManager;
import util.StringParser;
import webserver.http.HttpHeader;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.ResponseEntity;

import java.util.*;

@Controller
@RequestMapping("/post")
public class PostController {

    @PostMapping(path = "/upload")
    public static ResponseEntity postUpload(HttpRequest httpRequest) {

        String SID = StringParser.getCookieValue(httpRequest.getCookie(), "SID");
        String userId = (String) SessionManager.getAttribute(SID, "user");

        List<MultipartFile> files = MultipartParser.parseMultipartData(httpRequest.getByteBody(), httpRequest.getBoundary());
        String title = "";
        String contents = "";
        File file = new File();

        for (MultipartFile m : files) {
            if (m.getFieldName().equals("title")) {
                title = new String(m.getContent());
                continue;
            }

            if (m.getFieldName().equals("contents")) {
                contents = new String(m.getContent());
                continue;
            }

            if (m.getFieldName().equals("file") && m.getFileName().length() > 1) {
                file.setFileName(m.getFileName());
                file.setContentType(m.getContentType());
                file.setFileContent(m.getContent());
            }
        }

        Post post = new Post(title, contents, userId);
        int postId = PostDao.insertPost(post);

        if (file.getFileContent() != null) {
            file.setPostId(postId);
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

    @GetMapping(path = "/file")
    public static ResponseEntity getPostFile(@RequestParam(name = "postId") int postId) {

        byte[] file = FileDao.findFileByPostId(postId);

        Map<String, List<String>> headerMap = new HashMap<>();
        return new ResponseEntity<>(
                HttpStatus.OK,
                headerMap,
                file
        );
    }

}
