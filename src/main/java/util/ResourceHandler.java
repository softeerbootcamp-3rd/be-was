package util;

import ch.qos.logback.core.util.FileUtil;
import content.FileContent;
import dto.ResourceDto;
import exception.SourceException;
import model.Model;
import model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResourceHandler {

    public static byte[] resolveResource(ResourceDto resource) throws IOException {
        String resourcePath = getResourcePath(resource.getPath());
        FileInputStream fis = new FileInputStream(resourcePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bodyData = changeFileToByte(fis, bos);
        if (!resourcePath.contains(".html")) {
            return bodyData;
        }

        // 로그인시의 뷰 수정
        String bodyString = changeMenuHtmlFile(resource, bodyData);
        bodyString = changeUserListHtmlFile(resource, bodyString);

        return bodyString.getBytes();
    }

    private static String changeUserListHtmlFile(ResourceDto resource, String bodyString) {
        if (resource.getPath().contains("/user/list")) {
            List<User> userList = (List<User>) Model.getAttribute("userList").get();

            for (int i = 3; i < userList.size() + 3; i++) {
                User user = userList.get(i - 3);
                bodyString = bodyString.replace("{{data}}",
                        FileContent.USER_LIST.getText(i, user.getUserId(), user.getName(), user.getEmail()));
                System.out.println("ttt");
            }
        }
        return bodyString;
    }

    private static String changeMenuHtmlFile(ResourceDto resource, byte[] bodyData) {
        String bodyString = new String(bodyData);
        if (resource.isIsloggined()) {
            bodyString = bodyString.replace("{{menu}}",
                    FileContent.LOGIN.getText(String.valueOf(Model.getAttribute("username").get())));
        } else {
            bodyString = bodyString.replace("{{menu}}",
                    FileContent.NON_LOGIN.getText());
        }
        return bodyString;
    }

    private static byte[] changeFileToByte(FileInputStream fis, ByteArrayOutputStream bos) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        fis.close();
        byte[] bodyData = bos.toByteArray();
        return bodyData;
    }

    private static String getResourcePath(String path) {
        String sourceDirectory = path.contains(".html") ? "./templates" : "./static";
        URL resource = ResourceHandler.class.getClassLoader().getResource(sourceDirectory + path);
        if (resource == null) {
            throw new SourceException(ErrorCode.NOT_VALID_PATH);
        }
        return resource.getPath();
    }
}