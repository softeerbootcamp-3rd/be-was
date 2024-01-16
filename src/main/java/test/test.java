package test;

import org.junit.Test;

public class test {
    @Test
    public void verifyUser3() {
        String s = "dasdasd/";
        String[] arr = s.split("/");
        System.out.println(arr.length);
        System.out.println(arr[0]);
    }
}
