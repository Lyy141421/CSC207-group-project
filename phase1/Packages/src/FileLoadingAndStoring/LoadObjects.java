package FileLoadingAndStoring;

import java.util.ArrayList;

public abstract class LoadObjects<T> {

    /**
     * Load all objects in this list.
     * @param objects The list of objects to be loaded.
     */
    public void loadAll(ArrayList<T> objects) {
        for (T object : objects) {
            this.loadOne(object);
        }
    }

    abstract void loadOne(T object);
}
