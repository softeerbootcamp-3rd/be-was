package HTTPModel;

import java.util.Random;

public class Response {

    byte[] body;
    String returnType;

    private HttpStatus status;

    private String redirectUrl;

    private String sid;

    private boolean sidSet;

    private static long serverStartupTime = System.currentTimeMillis();


    public Response(){
        this.sidSet = false;
    };

    public Response(HttpStatus httpStatus){
        this.status = httpStatus;
    }

    public Response(HttpStatus httpStatus, byte[] body){
        this.status = httpStatus;
        this.body = body;
    }
    public Response(HttpStatus httpStatus, String redirectUrl){
        this.status = httpStatus;
        this.redirectUrl = redirectUrl;
    }

    public void Setbody(byte[] body){
        this.body = body;
    }

    public void SetreturnType(String returnType){
        this.returnType = returnType;
    }

    public void SetRedirectUrl(HttpStatus httpStatus, String redirectUrl){
        this.status = httpStatus;
        this.redirectUrl = redirectUrl;
    }

    public void SetSid(){
        Random random = new Random();
        this.sid = String.valueOf(random.nextInt(900000) + 100000);

    }

    public String getRedirectUrl(){
        return this.redirectUrl;
    }
    public void SetHttpStatus(HttpStatus httpStatus){
        this.status = httpStatus;
    }


    public void SetSidSet(){
        this.sidSet = true;
    }


    public HttpStatus getHttpStatus(){
        return this.status;
    }

    public boolean getSidSet(){
        return this.sidSet;
    }

    public String getSid(){
        return this.sid;
    }

    public byte[] getBody(){
        return this.body;
    }

    public HttpStatus getStatus(){
        return this.status;
    }

    public String getReturnType(){
        return this.returnType;
    }
}
