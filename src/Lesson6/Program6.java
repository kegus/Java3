package Lesson6;

public class Program6 {
    public static void main(String[] args) {

        System.out.println("Ok");
    }

    private int[] getArrAfterLast4(int[] arr){
        return arr;
    }

    public boolean checkArr1_4(int[] arr){
        boolean res = false;
        int cnt1 = 0;
        int cnt4 = 0;
        for (int i: arr) {
            if(i == 1) cnt1++;
            if(i == 4) cnt4++;
        }
        return cnt1 == cnt4;
    }
}
