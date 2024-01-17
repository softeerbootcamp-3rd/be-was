package DTO;

public class ResponseDTO {

    byte[] body;
    String returnType;


    public void Setbody(byte[] body){
        this.body = body;
    }

    public void SetreturnType(String returnType){
        this.returnType = returnType;
    }
    public byte[] Getbody(){
        return this.body;
    }
    public String GetreturnType(){
        return this.returnType;
    }
}
