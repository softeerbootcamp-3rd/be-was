package model;

import java.util.HashMap;

public class File {
    private String name;
    private String type;
    private static HashMap<String, String> contentType = new HashMap<>() {{
        put("doc", "application/msword");
        put("pdf", "application/pdf");
        put("xls", "application/vnd.ms-excel");
        put("js", "text/javascript");
        put("zip", "application/zip");
        put("jpeg", "image/jpeg");
        put("css", "text/css");
        put("html", "text/html");
        put("txt", "text/plain");
        put("xml", "text/xml");
        put("xsl", "text/xsl");
    }};
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public File(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public String getMimeType() {
        return contentType.get(this.type);
    }
}
