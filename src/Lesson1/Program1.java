package Lesson1;

import java.util.ArrayList;
import java.util.Arrays;

public class Program1 {
    public static void main(String[] args) {
        // п. 1 меняем 2 элемента местами
        // с помощью класса
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

        // с помощью метода
        Integer[] intArr1 = {2,3,4};
        System.out.println(Arrays.toString(intArr1));
        replace(1,2, intArr1);
        System.out.println(Arrays.toString(intArr1));

        String[] strArr1 = {"2as","3as","4as"};
        System.out.println(Arrays.toString(strArr1));
        replace(1,2, strArr1);
        System.out.println(Arrays.toString(strArr1));

        // п. 2 массив в лист
        // с помощью класса
        Integer[] intArr2 = {2,3,4};
        ArrayToList<?> intArrList = new ArrayToList<>(intArr2);
        System.out.println(Arrays.toString(intArr2));
        System.out.println(Arrays.toString(intArrList.convert().toArray()));

        String[] strArr2 = {"2as","3as","4as"};
        ArrayToList<?> strArrList = new ArrayToList<>(strArr2);
        System.out.println(Arrays.toString(strArr2));
        System.out.println(Arrays.toString(strArrList.convert().toArray()));

        // с помощью метода
        Integer[] intArr3 = {2,3,4};
        System.out.println(Arrays.toString(intArr3));
        System.out.println(Arrays.toString(convert(intArr3).toArray()));

        String[] strArr3 = {"2as","3as","4as"};
        System.out.println(Arrays.toString(strArr3));
        System.out.println(Arrays.toString(convert(strArr3).toArray()));

        System.out.println("Ok");
    }

    // п. 1 меняем 2 элемента местами
    public static <T> void replace(int n1, int n2, T[] data) {
        if (n1 < 0 || n2 < 0 || n1 >= data.length || n2 >= data.length)
            throw new ArrayIndexOutOfBoundsException();
        T val = data[n1];
        data[n1] = data[n2];
        data[n2] = val;
    }

    // п. 2 массив в лист
    public static <T> ArrayList<T> convert(T[] data) {
        ArrayList<T> dataList = new ArrayList<>();
        for (T value : data) dataList.add(value);
        return dataList;
    }
}
