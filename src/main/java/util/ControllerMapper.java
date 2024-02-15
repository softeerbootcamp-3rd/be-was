package util;

import annotation.Controller;
import annotation.RequestMapping;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    private static final String DEFAULT_PATH = "controller";
    public static final Map<String, Class<?>> CONTROLLER_MAP = new HashMap<>();

    static {
        File directory = new File(ClassLoader.getSystemResource(DEFAULT_PATH).getFile());
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = DEFAULT_PATH + '.' + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        String basePath = clazz.getAnnotation(RequestMapping.class).value();
                        CONTROLLER_MAP.put(basePath, clazz);
                    }
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
    }

    public static Class<?> getController(String path) {
        String mappingPath = "/" + path.split("/")[1];
        return CONTROLLER_MAP.containsKey(mappingPath) ? CONTROLLER_MAP.get(mappingPath) : null;
    }

}