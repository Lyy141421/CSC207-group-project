package UIClasses;

import java.util.ArrayList;

class PrintItems<T> {
    /**
     * Prints a list of items.
     */

    /**
     * Print an unnumbered list of objects.
     *
     * @param objects The objects to be printed
     */
    void printList(ArrayList<T> objects) {
        if (objects.isEmpty()) {
            System.out.println("\nNo items to view.");
        }
        for (T object : objects) {
            System.out.println("\n" + object);
        }
    }

    /**
     * Prints a numbered list of objects.
     *
     * @param objects The objects to be printed.
     */
    void printListToSelectFrom(ArrayList<T> objects) {
        if (objects.isEmpty()) {
            System.out.println("\nNo items to view.");
        }
        for (int i = 1; i <= objects.size(); i++) {
            System.out.println("\n" + i + ".");
            System.out.println(objects.get(i - 1));
        }
    }

}
