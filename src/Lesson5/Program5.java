package Lesson5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Program5 {
    public static final int CARS_COUNT = 6;
    public static final boolean PAIRS_TRANZIT = CARS_COUNT % 2 == 0;
    public static Thread[] arrThrds = new Thread[CARS_COUNT];
    public static final CountDownLatch cdlReady = new CountDownLatch(CARS_COUNT);
    public static final CountDownLatch cdlStart = new CountDownLatch(CARS_COUNT);
    public static final CountDownLatch cdlReadyAll = new CountDownLatch(1);
    public static final CountDownLatch cdlFinish = new CountDownLatch(CARS_COUNT);
    public static final CyclicBarrier cbTonnel = new CyclicBarrier(2);
    public static final Semaphore smpTonnel = new Semaphore(2);
    public static final Lock lockWin = new ReentrantLock();
    public static volatile boolean WINNER = false;


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60, false), new Tunnel(false), new Road(40, true));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 30));
        }
        for (int i = 0; i < cars.length; i++) {
            arrThrds[i] = new Thread(cars[i]);
            arrThrds[i].start();
        }
        try {
            cdlReady.await();
            cdlStart.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            cdlReadyAll.countDown();
        } catch (InterruptedException e) {}
        /*try {
            for (int i = 0; i < cars.length; i++) {
                if (arrThrds[i] != null) arrThrds[i].join();
            }
        } catch (InterruptedException e) {}*/


        try {
            cdlFinish.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (InterruptedException e) {}


        System.out.println("Ok");
    }

    static class Car implements Runnable {
        private static int CARS_COUNT;
        static {
            CARS_COUNT = 0;
        }
        private Race race;
        private int speed;
        private String name;
        public String getName() {
            return name;
        }
        public int getSpeed() {
            return speed;
        }
        public Car(Race race, int speed) {
            this.race = race;
            this.speed = speed;
            CARS_COUNT++;
            this.name = "Участник #" + CARS_COUNT;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.name + " готовится");
                Thread.sleep(500 + (int) (Math.random() * 800));
                cdlReady.countDown();
                System.out.println(this.name + " готов");
                cdlStart.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                cdlReadyAll.await();
            } catch (InterruptedException e) {}
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            cdlFinish.countDown();
        }
    }

    static abstract class Stage {
        protected int length;
        protected String description;
        protected boolean isFinal;

        public String getDescription() {
            return description;
        }

        public abstract void go(Car c);
    }

    static class Road extends Stage {
        public Road(int length, boolean isFinal) {
            this.length = length;
            this.isFinal = isFinal;
            this.description = "Дорога " + length + " метров";
        }

        @Override
        public void go(Car c) {
            try {
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
                System.out.println(c.getName() + " закончил этап: " + description);
                if (isFinal) {
                    lockWin.lock();
                    if (!WINNER) {
                        WINNER = true;
                        System.out.println(c.getName() + " - WIN");
                    }
                    lockWin.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Tunnel extends Stage {
        public Tunnel(boolean isFinal) {
            this.length = 80;
            this.isFinal = isFinal;
            this.description = "Тоннель " + length + " метров";
        }

        @Override
        public void go(Car c) {
            try {
                try {
                    System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                    if (PAIRS_TRANZIT) cbTonnel.await();
                    smpTonnel.acquire();
                    System.out.println(c.getName() + " начал этап: " + description);
                    Thread.sleep(length / c.getSpeed() * 1000);
                    smpTonnel.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(c.getName() + " закончил этап: " + description);
                    if (isFinal) {
                        lockWin.lock();
                        if (!WINNER) {
                            WINNER = true;
                            System.out.println(c.getName() + " - WIN");
                        }
                        lockWin.unlock();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Race {
        private ArrayList<Stage> stages;

        public ArrayList<Stage> getStages() {
            return stages;
        }

        public Race(Stage... stages) {
            this.stages = new ArrayList<>(Arrays.asList(stages));
        }
    }
}

        /*Semaphore smp = new Semaphore(2);
        for (int i = 0; i < 5; i++) {
            final int w = i;
            arrThrds[i] = new Thread(() -> {
                try {
                    System.out.println("Поток " + w + " перед семафором");
                    smp.acquire();
                    System.out.println("Поток " + w + " получил доступ к ресурсу");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Поток " + w + " освободил ресурс");
                    smp.release();
                }
            });
            arrThrds[i].start();
        }
        try {
            for (int i = 0; i < 5; i++) {
                arrThrds[i].join();
            }
        } catch (InterruptedException e) {}
         */
        /*
        // задаем количество потоков
        final int THREADS_COUNT = 5;
        // задаем значение счетчика
        final CountDownLatch cdl = new CountDownLatch(THREADS_COUNT);
        System.out.println("Начинаем");
        for (int i = 0; i < THREADS_COUNT; i++) {
            final int w = i;
            arrThrds[i] = new Thread(() -> {
                try {
                    // считаем, что выполнение задачи занимает ~1 сек
                    Thread.sleep(500 + (int)(500 * Math.random()));
                    // как только задача выполнена, уменьшаем счетчик
                    System.out.println("Поток #" + w + " - готов");
                    cdl.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            arrThrds[i].start();
        }
        try {
            cdl.await();    // пока счетчик не приравняется нулю, будем стоять на этой строке
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // как только все потоки выполнили свои задачи - пишем сообщение
        System.out.println("Работа завершена");
         */
        /*CyclicBarrier cb = new CyclicBarrier(2);
        for (int i = 0; i < 4; i++) {
            final int w = i;
            arrThrds[i] = new Thread(() -> {
                try {
                    System.out.println("Поток " + w + " готовится");
                    Thread.sleep(100 + (int) (3000 * Math.random()));
                    System.out.println("Поток " + w + " готов");
                    cb.await();
                    System.out.println("Поток " + w + " запустился");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            arrThrds[i].start();
        }
        try {
            for (int i = 0; i < CARS_COUNT; i++) {
                if (arrThrds[i] != null) arrThrds[i].join();
            }
        } catch (InterruptedException e) {}
        */

