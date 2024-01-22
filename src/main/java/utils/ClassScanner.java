package utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> findClasses(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();

        String path = basePackage.replace('.', '/');
        URI uri;

        try {
            uri = ClassLoader.getSystemResource(path).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error getting URI for package: " + basePackage, e);
        }

        File root = new File(uri);

        // 재귀적으로 패키지 내의 클래스를 스캔
        scanClasses(root, basePackage, classes);

        return classes;
    }

    private static void scanClasses(File root, String basePackage, List<Class<?>> classes) {
        File[] files = root.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 디렉토리인 경우 재귀적으로 스캔
                scanClasses(file, basePackage + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                // 클래스 파일인 경우 클래스로 로딩하여 리스트에 추가
                String className = basePackage + "." + file.getName().replace(".class", "");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Error loading class: " + className, e);
                }
            }
        }
    }
}

