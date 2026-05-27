package mycode;

import java.util.HashMap;
import java.util.Map;

public abstract class BasePredictObject {

    private int id;
    private String name;

    public BasePredictObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("id", id);
        info.put("name", name);

        return info;
    }
}