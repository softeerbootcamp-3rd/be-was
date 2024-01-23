package DTO;

public class Response {

    byte[] body;
    String returnType;

    private HttpStatus status;

    private String redirectUrl;

    public Response(){};
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

    public String getRedirectUrl(){
        return this.redirectUrl;
    }
    public void SetHttpStatus(HttpStatus httpStatus){
        this.status = httpStatus;
    }


    public HttpStatus getHttpStatus(){
        return this.status;
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
