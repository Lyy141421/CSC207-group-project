package GUIClasses;

import java.util.ArrayList;

class PrintItems<T> {

    /**
     * Print a list if objects.
     *
     * @param objects The objects to be printed
     */
    void printList(ArrayList<T> objects) {
        if (objects.isEmpty()) {
            System.out.println("No items to view.");
        }
        for (T object : objects) {
            System.out.println(object);
            System.out.println();
        }
    }

    void printListToSelectFrom(ArrayList<T> objects) {
        if (objects.isEmpty()) {
            System.out.println("No items to view.");
        }
        for (int i = 1; i <= objects.size(); i++) {
            System.out.println(i + ".");
            System.out.println(objects.get(i - 1));
            System.out.println();
        }
    }

}
