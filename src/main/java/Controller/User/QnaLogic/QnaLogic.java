package Controller.User.QnaLogic;

import Functions.FileBytes;
import HTTPModel.HttpStatus;
import HTTPModel.Request;
import HTTPModel.Response;
import db.Database;
import db.QnaDatabase;
import model.Qna;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QnaLogic {

    private static final Logger logger = LoggerFactory.getLogger(webserver.RequestHandler.class);

    private static final String qnaFile = "./src/main/resources/templates/qna";
    private static final String filePath = "./src/main/resources/templates";



    public static Response qnaShow(Request request, User logined_user){

        byte [] body;
        body = FileBytes.FilesreadAllBytes(qnaFile + "/show.html", logined_user, true);
        Response response = new Response(HttpStatus.OK);
        response.Setbody(body);
        return response;
    }


    public static Response qnaForm(Request request, User logined_user){

        byte [] body;
        body = FileBytes.FilesreadAllBytes(qnaFile + "/form.html", logined_user, true);
        Response response = new Response(HttpStatus.OK);
        response.Setbody(body);
        return response;
    }


    public static Response qnaPost(Request request, User logined_user){

        Response response = new Response();

        String requestBody = request.GetBody();
        String writer = requestBody.split("&")[0].split("=")[1];
        String title = requestBody.split("&")[1].split("=")[1];
        String contents = requestBody.split("&")[2].split("=")[1];

        User user = Database.findUserByName(writer);

        Qna qna = new Qna(user, title, contents);
        QnaDatabase.addQnas(qna);


        byte [] body;
        body = FileBytes.FilesreadAllBytes(filePath + "/index.html", logined_user, true);


        response.SetRedirectUrl(HttpStatus.REDIRECT, "/index.html");
        return response;
    }

}
