package Lesson7;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Program7 {
    @Test(testMethod = 1, beforeTest = false, afterTest = false)
    public void myTest() {
        try {
            Method testSome = this.getClass().getDeclaredMethod("myTest");
            Test test = testSome.getAnnotation(Test.class);
            if (test.testMethod() == 1) {
                System.out.println("Ok1");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Program7 program7 = new Program7();
        program7.myTest();
        System.out.println("Ok2");
    }
}
