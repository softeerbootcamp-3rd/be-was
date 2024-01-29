package controller;

import annotation.*;
import dao.FileDao;
import dao.PostDao;
import dto.PostDto;
import model.File;
import model.Post;
import util.SessionManager;
import util.StringParser;
import webserver.MultipartFile;
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

        List<MultipartFile> files = httpRequest.getFiles();
        Map<String, String> body = httpRequest.getParams();

        String SID = StringParser.getCookieValue(httpRequest.getCookie(), "SID");
        String userId = (String) SessionManager.getAttribute(SID, "user");

        Post post = new Post(
                body.get("title"),
                body.get("contents"),
                userId
        );
        long postId = PostDao.insertPost(post);

        for (MultipartFile f : files) {
            File file = new File(
                    postId,
                    f.getContent()
            );
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


}
