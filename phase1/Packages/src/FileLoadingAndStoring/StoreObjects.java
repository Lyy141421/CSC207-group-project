package FileLoadingAndStoring;

import java.util.ArrayList;

public abstract class StoreObjects<T> {

    /**
     * Store all the objects in this list.
     * @param objects    The objects to be stored.
     */
    public void storeAll(ArrayList<T> objects) {
        for (T object : objects) {
            this.storeOne(object);
        }
    }

    abstract void storeOne(T object);
}
