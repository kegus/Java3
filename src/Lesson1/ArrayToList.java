package Lesson1;

import java.util.ArrayList;

public class ArrayToList<T> {
    private T[] data;
    private ArrayList<T> dataList;

    public ArrayToList(T[] data) {
        this.data = data;
        dataList = new ArrayList<T>();
    }
    
    public ArrayList<T> convert() {
        for (T value : data) dataList.add(value);
        return dataList;
    } 
}
