package controller.dto;

import java.util.LinkedList;
import java.util.Map;

public class ListMapData {

    private LinkedList<Map<String, String>> list;

    public ListMapData() {
        list = new LinkedList<Map<String, String>>();
    }

    public void putMap(Map<String, String> map) {
        list.add(map);
    }

    public Integer getListSize() {
        return list.size();
    }

    public Map<String, String> getMap(int index) {
        return list.get(index);
    }
}
