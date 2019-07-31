package Lesson1.FruitBoxes;

import Lesson1.FruitBoxes.fruits.*;

public class FruitBoxes {
    private static final int CNT_BOXES = 3;
    public static void main(String[] args) {
        // п. 3 коробки с фруктами
        Fruit apple = new Apple();
        Fruit orange = new Orange();

        System.out.println("Коробка яблок");
        BoxWithFruits<Fruit>[] boxes = new BoxWithFruits[CNT_BOXES];
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

        System.out.println("Ok");
    }
}
