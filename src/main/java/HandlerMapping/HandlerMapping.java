package HandlerMapping;

import Controller.User.UserController;
import DTO.RequestDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import DTO.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;


public class HandlerMapping {

    private RequestDTO requestDTO;
    private String filePath = "./src/main/resources/templates";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HandlerMapping(RequestDTO requestDTO){
        this.requestDTO = requestDTO;
    }

    public ResponseDTO Controller(){

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.SetreturnType("text/html");

        byte[] body = null;
        try {
            String URI = requestDTO.GetURI();
            System.out.println("here    " + URI.split("/")[1]);

            if("css".equals(URI.split("/")[1])){
                responseDTO.SetreturnType("text/css");
                body = Files.readAllBytes(new File("./src/main/resources/static/css/" + URI.split("/")[2]).toPath());

            }else if("js".equals(URI.split("/")[1])){
                responseDTO.SetreturnType("application/javascript");
                body = Files.readAllBytes(new File("./src/main/resources/static/js/" + URI.split("/")[2]).toPath());
            }else if("fonts".equals(URI.split("/")[1])){
                responseDTO.SetreturnType("application/font-woff");
                body = Files.readAllBytes(new File("./src/main/resources/static/fonts/" + URI.split("/")[2]).toPath());
            }


            else if (URI.equals("/index.html")) {

                body = Files.readAllBytes(new File(filePath + "/index.html").toPath());

            }else if(URI.split("/")[1].equals("user")){


                UserController userController = new UserController(URI.split("/")[2]);
                body = userController.UserLogic();
            }

        }
        catch (IOException e) {
                logger.error(e.getMessage());
        }
        responseDTO.Setbody(body);
        return responseDTO;

    }



}
