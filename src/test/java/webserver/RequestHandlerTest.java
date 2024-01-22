package webserver;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static config.ResponseCode.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestHandlerTest {

//    @Test
//    void run() {
//
//        ControllerHandler controllerHandler = null;
//        for (ControllerHandler handler : ControllerHandler.values()) {
//            if (handler.url.equals("/index.html")) {
//                System.out.println("good");
//                controllerHandler = handler;
//                break;
//            }
//        }
//    }
//
//    @Test
//    void run2() {
//        System.out.println(OK.code);
//        System.out.println(OK);
//    }
//
//    @Test
//    void run3() {
//        byte[] body = new byte[0];
//        System.out.println(body.length);
//    }



//    public static class Person {
//        private String name;
//        private int age;
//    }
//
//    @Test
//    public void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
//        Object person = new Person();
//        Field[] fields = person.getClass().getDeclaredFields();
//
//        List<String> actualFieldNames = getFieldNames(fields);
//
//        assertTrue(Arrays.asList("name", "age")
//                .containsAll(actualFieldNames));
//    }


    private static List<String> getFieldNames(Field[] fields) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields)
            fieldNames.add(field.getName());
        return fieldNames;
    }

    public interface Eating {
        String eats();
    }

    public static abstract class Animal implements Eating {

        public static String CATEGORY = "domestic";
        private String name;

        protected abstract String getSound();

        public Animal(String name) {
            this.name = name;
        }

        public static String getCATEGORY() {
            return CATEGORY;
        }

        public static void setCATEGORY(String CATEGORY) {
            Animal.CATEGORY = CATEGORY;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface Locomotion {
        String getLocomotion();
    }

    public class Goat extends Animal implements Locomotion{
        @Override
        protected String getSound() {
            return "bleat";
        }

        @Override
        public String getLocomotion() {
            return "walks";
        }

        @Override
        public String eats() {
            return "grass";
        }

        public Goat(String name) {
            super(name);
        }
    }

//    @Test
//    public void givenObject(){
//        Object goat = new Goat("goata");
//        Class<?> clazz = goat.getClass();
//        System.out.println(clazz.getName());
//        assertEquals("Goat", clazz.getSimpleName());
//        assertEquals("webserver.RequestHandlerTest.Goat", clazz.getName());
//        assertEquals("webserver.RequestHandlerTest.Goat", clazz.getCanonicalName());
//
//
//    }


//    @Test
//    public void givenClass() throws ClassNotFoundException {
//        //Class<?> goatClass = Class.forName("webserver.RequestHandlerTest.Goat");
//        Class<?> animalClass = Class.forName("webserver.RequestHandlerTest.Animal");
//
//        //int goatMods = goatClass.getModifiers();
//        int animalMods = animalClass.getModifiers();
//
//        //assertTrue(Modifier.isPublic(goatMods));
//        assertTrue(Modifier.isPublic(animalMods));
//        assertTrue(Modifier.isAbstract(animalMods));
//
//    }


//    @Test
//    public void givenClass_whenGetsPackageInfo_thenCorrect() {
//        Goat goata = new Goat("goat");
//        Class<?> goataClass = goata.getClass();
//        Package pkg = goataClass.getPackage();
//        System.out.println(pkg.getClass());
//    }


    private static List<String> getMethodNames(Method[] methods) {
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods)
            methodNames.add(method.getName());
        return methodNames;
    }

    public class Bird extends Animal {
        private boolean walks;

        public Bird() {
            super("bird");
        }

        public Bird(String name, boolean walks) {
            super(name);
            setWalks(walks);
        }

        public Bird(String name) {
            super(name);
        }

        public boolean walks() {
            return walks;
        }

        public boolean isWalks() {
            return walks;
        }

        public void setWalks(boolean walks) {
            this.walks = walks;
        }

        @Override
        protected String getSound() {
            return null;
        }

        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public void setName(String name) {
            super.setName(name);
        }

        @Override
        public String eats() {
            return null;
        }
// standard setters and overridden methods
    }

//    @Test
//    public void givenClass_whenGetsOnlyDeclaredMethods_thenCorrect(){
//        Class<?> birdClass = Class.forName("com.baeldung.reflection.Bird");
//        List<String> actualMethodNames
//                = getMethodNames(birdClass.getDeclaredMethods());
//
//        List<String> expectedMethodNames = Arrays
//                .asList("setWalks", "walks", "getSound", "eats");
//
//        assertEquals(expectedMethodNames.size(), actualMethodNames.size());
//        assertTrue(expectedMethodNames.containsAll(actualMethodNames));
//        assertTrue(actualMethodNames.containsAll(expectedMethodNames));
//    }





}