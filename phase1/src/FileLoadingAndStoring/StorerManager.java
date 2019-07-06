package FileLoadingAndStoring;

import javax.management.AttributeList;
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

    public static void endSave(){
        for(Object x : storer_map.keySet()){
            storer_map.get(x).store();
        }
    }

    private static void addStored(Object obj){
        stored.add(obj);
    }

    private static boolean isStored(Object obj){
        return stored.contains(obj);
    }

    public static void flushStored(){
        stored = new ArrayList<>();
    }

    public static void subStore(Object obj){
        if(!isStored(obj)){
            addStored(obj);
            storer_map.get(obj.getClass()).storer.storeOne(obj);
        }

    }

    // === Instance Methods ===

    void store(){
        for(Object x : obj_list){
            storer.storeOne(x);
        }
    }

}
