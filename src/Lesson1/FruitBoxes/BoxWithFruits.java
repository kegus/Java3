package Lesson1.FruitBoxes;

import Lesson1.FruitBoxes.fruits.Fruit;

public class BoxWithFruits<T extends Fruit> {
    final private int maxWeight = 100;
    private int weight;

    public Class<? extends Fruit> getClassFruit() {
        return classFruit;
    }

    private Class<? extends Fruit> classFruit;

    public int getWeight() {
        return weight;
    }

    public BoxWithFruits() {
        weight = 0;
        classFruit = null;
    }

    public void boxInfo() {
        if (weight > 0)
            System.out.println("вес " + weight + " " + classFruit.getSimpleName());
        else
            System.out.println("Коробка пуста");
    }

    public boolean addFruit(T fruit) {
        if (classFruit == null)
            classFruit = fruit.getClass();
        if (classFruit == fruit.getClass()) {
            if (weight + fruit.getWeight() < maxWeight) {
                weight += fruit.getWeight();
                return true;
            } else
                System.out.println("Коробка полна");
        } else
            System.out.println("Нельзя смешивать фрукты");
        return false;
    }

    public boolean removeFruit(T fruit) {
        if (classFruit == fruit.getClass()) {
            if (weight - fruit.getWeight() >= 0) {
                weight -= fruit.getWeight();
                return true;
            } else
                System.out.println("Коробка пуста");
        } else
            System.out.println("Этих фруктов тут нет");
        return false;
    }

    public void replaceTo(BoxWithFruits<T> b2, T fruit) {
        if (compareByType(b2))
            while (b2.addFruit(fruit))
                if (!removeFruit(fruit)) {
                    b2.removeFruit(fruit);
                    break;
                }
        else
            System.out.println("Разные фрукты");
    }

    public boolean compareByWeight(BoxWithFruits<?> b2) {
        if(getWeight() == b2.getWeight()) return true;
        return false;
    }

    public boolean compareByType(BoxWithFruits<?> b2) {
        if(this.getClassFruit() == b2.getClassFruit()) return true;
        return false;
    }

    public boolean compareByTypeAndWeight(BoxWithFruits<?> b2) { // типы должны совпадать
        if(compareByType(b2) && compareByWeight(b2)) return true;
        return false;
    }
}
