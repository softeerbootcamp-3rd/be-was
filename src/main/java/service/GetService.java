package service;

import db.Database;
import model.RequestHeader;


public class GetService implements HTTPMethod{

    private String detailPath;
    private String contentType;
    public void process(String path, RequestHeader rh){
        if (path.contains("/create")) {
            CreateUserService createUserService = new CreateUserService(new Database(), path);
            path = createUserService.getUrl();
            rh.setPath(path);
        }

        // 리소스 처리
        ResourceService resourceService = new ResourceService();
        String fileExtension = resourceService.separatedFileExtension(rh);
        this.detailPath = resourceService.getDetailPath(fileExtension,path);
        this.contentType = resourceService.getContextType(fileExtension);
    }

    public String getContentType() {
        return contentType;
    }
    public String getDetailPath() {
        return detailPath;
    }

}
