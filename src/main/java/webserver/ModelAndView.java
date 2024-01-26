package webserver;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private String viewName;
    private Model model = new Model();

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
