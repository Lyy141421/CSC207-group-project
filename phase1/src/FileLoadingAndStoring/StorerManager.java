package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import UsersAndJobObjects.JobApplication;

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
     * @param jobApplicationSystem The job application system being used.
     */
    public static void endSave(JobApplicationSystem jobApplicationSystem) {
        for(Object x : storer_map.keySet()){
            storer_map.get(x).store(jobApplicationSystem);
        }
        new PreviousLoginDateLoaderAndStorer().storePreviousLoginDate(jobApplicationSystem);
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
     * @param jobApplicationSystem The job application system being used.
     * @param obj the object to be substored
     */
    static void subStore(JobApplicationSystem jobApplicationSystem, Object obj) {
        if(!isStored(obj)){
            addStored(obj);
            storer_map.get(obj.getClass()).storer.storeOne(jobApplicationSystem, obj);
        }

    }

    // === Instance Methods ===

    /**
     * Stores all Objects in obj_list
     * @param jobApplicationSystem The job application system being used.
     */
    void store(JobApplicationSystem jobApplicationSystem) {
        for(Object x : obj_list){
            storer.storeOne(jobApplicationSystem, x);
        }
    }

}
