package Lesson1;

import java.util.Arrays;

public class ParamArray<T> {
    private T[] data;

    public ParamArray(T[] data) {
        this.data = data;
    }

    public void replace(int n1, int n2) {
        if (n1 < 0 || n2 < 0 || n1 >= data.length || n2 >= data.length)
            throw new ArrayIndexOutOfBoundsException();
        T val = data[n1];
        data[n1] = data[n2];
        data[n2] = val;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
