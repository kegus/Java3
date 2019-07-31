package Lesson1;

import java.util.ArrayList;
import java.util.List;

public class ArrayToList<T> {
    private T[] data;
    private List<T> dataList;

    public ArrayToList(T[] data) {
        this.data = data;
        dataList = new ArrayList<>();
    }
    
    public ArrayList<T> convert() {
        for (T value : data) dataList.add(value);
        return (ArrayList<T>)dataList;
    } 
}
