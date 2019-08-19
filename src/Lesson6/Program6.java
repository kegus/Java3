package Lesson6;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Program6 {
    public static void main(String[] args) {
        /*String source = "ПУТин";
        String str = "(?i)путин";
        String newStr = "молодец";
        String resMsg = Pattern.compile(str, Pattern.UNICODE_CASE).matcher(source).replaceAll(newStr);
        System.out.println(resMsg);*/
        System.out.println("Ok");
    }

    public Integer[] getArrAfterLast4(int[] arr) throws RuntimeException {
        Integer[] res;
        List<Integer> resList = new ArrayList<>();
        boolean found4 = false;
        for (int i = arr.length-1; i >= 0; i--) {
            if (arr[i] == 4) {
                found4 = true;
                for (int j = i; j < arr.length; j++) {
                    resList.add(arr[j]);
                }
            }
        }
        if (found4) {
            res = new Integer[resList.size()];
            resList.toArray(res);
            return res;
        } else
            throw new RuntimeException();

    }

    public boolean checkArr1_4(int[] arr){
        int cnt1 = 0;
        int cnt4 = 0;
        for (int i: arr) {
            if(i == 1) cnt1++;
            if(i == 4) cnt4++;
        }
        return cnt1 == cnt4;
    }
}
