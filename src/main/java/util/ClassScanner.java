package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    public static List<Class<?>> scanControllers(String basePackage) {
        List<Class<?>> controllerClasses = new ArrayList<>();

        String basePath = basePackage.replace(".", File.separator);
        String packagePath = "src/main/java/" + basePath;

        List<File> classFiles = listFiles(packagePath);

        for (File file : classFiles) {
            String className = getClassName(file, basePath);
            try {
                Class<?> clazz = Class.forName(className);
                if (hasControllerAnnotation(clazz)) controllerClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }

        return controllerClasses;
    }

    private static List<File> listFiles(String directoryPath) {
        List<File> classFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".java") || file.getName().endsWith(".class")))
                        classFiles.add(file);
                }
            }
        }

        return classFiles;
    }

    private static String getClassName(File file, String basePath) {
        String fileName = file.getAbsolutePath().replace(File.separator, ".");
        int startIndex = fileName.indexOf(basePath);
        int endIndex = fileName.lastIndexOf(".");
        return fileName.substring(startIndex, endIndex);
    }

    private static boolean hasControllerAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Controller")) return true;
        }
        return false;
    }
}
