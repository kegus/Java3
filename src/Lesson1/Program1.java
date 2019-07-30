package Lesson1;

import java.lang.reflect.Array;

public class Program1 {
    public static void main(String[] args) {
        // 1. меняем 2 элемента местами
        Integer[] intArr = {2,3,4};
        ParamArray<?> paramIntArray = new ParamArray<>(intArr);
        System.out.println(paramIntArray);
        paramIntArray.replace(1,2);
        System.out.println(paramIntArray);

        String[] strArr = {"2as","3as","4as"};
        ParamArray<?> paramStrArray = new ParamArray<>(strArr);
        System.out.println(paramStrArray);
        paramStrArray.replace(1,2);
        System.out.println(paramStrArray);

        System.out.println("Ok");
    }

}
