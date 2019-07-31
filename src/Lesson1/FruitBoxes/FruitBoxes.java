package Lesson1.FruitBoxes;

import Lesson1.FruitBoxes.fruits.*;

public class FruitBoxes {
    private static final int CNT_BOXES = 3;
    public static void main(String[] args) {
        // п. 3 коробки с фруктами
        Fruit apple = new Apple();
        Fruit orange = new Orange();

        BoxWithFruits<Fruit>[] boxes = new BoxWithFruits[CNT_BOXES];
        System.out.println("Коробка яблок");
        for (int i = 0; i < boxes.length; i++) boxes[i] = new BoxWithFruits<>();
        boxes[0].boxInfo();
        boxes[0].addFruit(apple);
        boxes[0].addFruit(apple);
        boxes[0].addFruit(orange);
        boxes[0].boxInfo();

        System.out.println("Коробка апельсинов");
        boxes[1].boxInfo();
        boxes[1].addFruit(orange);
        boxes[1].addFruit(orange);
        boxes[1].addFruit(orange);
        boxes[1].addFruit(apple);
        boxes[1].addFruit(apple);
        boxes[1].boxInfo();
        boxes[1].replaceTo(boxes[0], orange);

        System.out.println("Коробка апельсинов 2 пересыпаем из 1-й");
        boxes[2].addFruit(orange);
        boxes[2].addFruit(orange);
        boxes[2].boxInfo();
        boxes[1].replaceTo(boxes[2], orange);
        boxes[1].boxInfo();
        boxes[2].boxInfo();

        //2-е решение
        BoxWithFruits<Apple> boxes0 = new BoxWithFruits(apple);
        BoxWithFruits<Orange> boxes1 = new BoxWithFruits(orange);
        BoxWithFruits<Orange> boxes2 = new BoxWithFruits(orange);

        System.out.println("Коробка яблок");
        boxes0.boxInfo();
        boxes0.addFruit(apple);
        boxes0.addFruit(apple);
        boxes0.addFruit(orange);
        boxes0.boxInfo();

        System.out.println("Коробка апельсинов");
        boxes1.boxInfo();
        boxes1.addFruit(orange);
        boxes1.addFruit(orange);
        boxes1.addFruit(orange);
        boxes1.addFruit(apple);
        boxes1.addFruit(apple);
        boxes1.boxInfo();
        //не работает на уровне компиляции
//        boxes1.replaceTo(boxes0, orange);

        System.out.println("Коробка апельсинов 2 пересыпаем из 1-й");
        boxes2.addFruit(orange);
        boxes2.addFruit(orange);
        boxes2.boxInfo();
        boxes1.replaceTo(boxes2, orange);
        boxes1.boxInfo();
        boxes2.boxInfo();
        System.out.println("Ok");
    }
}
