package model;

import java.util.HashMap;

public class File {
    private String name;
    private String type;
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public File(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public File(String name) {
        int indexOfType = name.lastIndexOf(".");
        this.name = name;
        this.type = name.substring(indexOfType+1);
    }
}
