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
        for (int i: arr) {
            if(i == 1 || i == 4) {
                res = true;
                break;
            }
        }
        return res;
    }
}
