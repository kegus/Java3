package Lesson4;

public class Program4 {
    private final Object mon = new Object();
    private final int CNT = 5;
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        Program4 w = new Program4();
        Thread t1 = new Thread(() -> { w.printA(); });
        Thread t2 = new Thread(() -> { w.printB(); });
        Thread t3 = new Thread(() -> { w.printC(); });
        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Ok");
    }

    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < CNT; i++) {
                    while (currentLetter != 'A') {
                        mon.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < CNT; i++) {
                    while (currentLetter != 'B') {
                        mon.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < CNT; i++) {
                    while (currentLetter != 'C') {
                        mon.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }}
