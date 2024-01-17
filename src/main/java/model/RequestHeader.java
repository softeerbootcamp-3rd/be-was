package model;

public class RequestHeader {
//Accept : 클라이언트가 서버로부터 받기 원하는 미디어 타입을 나타냄
//Accept-Encoding: 클라이언트가 지원하는 콘텐츠 인코딩 방법을 나타냄
//Accept-Language: 클라이언트가 지원하는 언어
//Cache-Control: 캐시 동작을 지정하며, 캐시 제어에 대한 지시를 서버에 전달
//Connection: 클라이언트와 서버 간의 연결 상태를 나타냄
//Cookie: 이전 서버로부터 받은 쿠키를 서버에게 다시 제공
//Host: 요청이 전공되는 대상 서버의 호스트 이름과 포트 번호를 지정
//Sec-Ch-Ua: 클라이언트의 사용자 에이전트인 크롬과 같은 브라우저에서 정의한 정보
//Sec-Ch-Ua-Mobile: 모바일 장치인지 여부를 나타냄
//Sec-Ch-Ua-Platform: 클라이언트의 플랫폼 정보를 나타냄
//Sec-Fetch-Dest: 요청의 목적지를 나타냄
//Sec-Fetch-Mode: 리소스 획득 시 사용되는 프로세스를 나타냄
//Sec-Fetch-Site: 요청의 출처를 식별
//Sec-Fetch-User: 사용자의 행동에 기반하여 리소를 가져오는 방식을 나타냄
//Upgrade-Insecure-Requests: 1이라면 http버전의 페이지를 열었을 때 https버전으로 업그레이드하도록 지시
//User-Agent: 클라이언트의 사용자 에이전트, 브라우저나 애플리케이션의 정보를 나타냄

    private String generalHeader;
    private String httpMethod;
    private String path;
    private String accpet;
    private String acceptEncoding;
    private String acceptLanguage;
    private String upgrade_insecure_requests;

    public String getPath(){

        String[] tokens = this.generalHeader.split(" ");
        this.path = tokens[1];
        this.httpMethod = tokens[0];
        // localhost:8080 호출하더라도 index.html로 이어짐
        if(this.path.equals("/")) this.path = "/index.html";
        return this.path;
    }
    public void setGeneralHeader(String generalHeader) {
        this.generalHeader = generalHeader;
    }

    public void setAccpet(String accpet) {
        this.accpet = accpet;
    }
    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public void setUpgrade_insecure_requests(String upgrade_insecure_requests) {
        this.upgrade_insecure_requests = upgrade_insecure_requests;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public String getGeneralHeader() {
        return generalHeader;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getAccpet() {
        return accpet;
    }

    public String getUpgrade_insecure_requests() {
        return upgrade_insecure_requests;
    }


    public String getHttpMethod() {
        return httpMethod;
    }
}
