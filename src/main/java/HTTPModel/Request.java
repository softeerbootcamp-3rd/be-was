package HTTPModel;

import java.util.ArrayList;

public class Request {

    String HTTPMethod;
    String URI;
    String Version;
    ArrayList<String> Accept = new ArrayList<>();
    String body;
    String sid;


    public void SetHTTPMethod(String HTTPMethod){
        this.HTTPMethod = HTTPMethod;
    }

    public void SetURI(String URI){
        this.URI = URI;
    }

    public void SetVersion(String Version){
        this.Version = Version;
    }

    public void SetAccept(String Accept){
        this.Accept.add(Accept);
    }

    public void SetBody(String body){
        this.body = body;
    }

    public void SetSid(String sid) { this.sid = sid; }

    public String GetHTTPMethod(){
        return this.HTTPMethod;
    }

    public String GetURI(){
        return this.URI;
    }

    public String GetVersion(){
        return this.Version;
    }

    public String GetSid() {return this.sid; }

    public ArrayList<String> GetAccept(){
        return this.Accept;
    }

    public String GetBody(){
        return this.body;
    }
}
