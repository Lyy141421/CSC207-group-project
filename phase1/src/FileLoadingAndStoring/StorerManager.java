package FileLoadingAndStoring;

import java.util.ArrayList;
import java.util.HashMap;

public class StorerManager {
    //The List of all Loaders
    private static HashMap<Class, StorerManager> storer_map = new HashMap<>();
    //Stored Objects
    private static ArrayList<Object> stored = new ArrayList<>();


    // === Instance Variables ===
    //The Loader for this manager
    private GenericStorer storer;
    //List of all objects to be saved
    private ArrayList obj_list;

    // === Constructor ===

    public StorerManager(GenericStorer storer, Class clazz, ArrayList list){
        this.storer = storer;
        this.obj_list = list;
        storer_map.put(clazz, this);
    }

    // === Static Methods ===

    /**
     * Saves all Storer classes in the storer maps
     */
    public static void endSave(){
        for(Object x : storer_map.keySet()){
            storer_map.get(x).store();
        }
        new PreviousLoginDateLoaderAndStorer().storePreviousLoginDate();
    }

    /**
     * Adds and object to stored
     *
     * @param obj the object to be added
     */
    private static void addStored(Object obj){
        stored.add(obj);
    }

    /**
     * Checks if the object has been stored
     *
     * @param obj - the object being checked against
     * @return boolean on weather or not it has been stored
     */
    private static boolean isStored(Object obj){
        return stored.contains(obj);
    }

    /**
     * Clears the stored map
     */
    public static void flushStored(){
        storer_map = new HashMap<>();
        stored = new ArrayList<>();
    }

    /**
     * Substores any items which have been called in the containing object being stored
     * @param obj the object to be substored
     */
    public static void subStore(Object obj){
        if(!isStored(obj)){
            addStored(obj);
            storer_map.get(obj.getClass()).storer.storeOne(obj);
        }

    }

    // === Instance Methods ===

    /**
     * Stores all Objects in obj_list
     */
    void store(){
        for(Object x : obj_list){
            storer.storeOne(x);
        }
    }

}
