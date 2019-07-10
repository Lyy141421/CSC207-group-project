package FileLoadingAndStoring;

import Main.JobApplicationSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LoaderManager {
    //The List of all Loaders
    private static HashMap<Class, LoaderManager> loader_map = new HashMap<>();
    //The HashMap of objects by Filename and then by ID
    private static HashMap<String, HashMap<String, Object>> obj_map = new HashMap<>();

    // === Instance Variables ===
    //The Loader for this manager
    private GenericLoader loader;
    //The class of object in this FileLoadingAndStoring.Loader
    private Class clazz;
    //Filename of objects in this loader
    private String filename;

    // === Constructor ===
    public LoaderManager(GenericLoader loader, Class clazz, String filename){
        this.loader = loader;
        this.clazz = clazz;
        this.filename = filename;
        loader_map.put(clazz, this);
    }

    // === Static Methods ===

    /**
     * Adds a object to the obj_map
     *
     * @param filename - The filename of the object
     * @param id - the Id of the object
     * @param obj - the object to be stored
     */
    static void mapPut(String filename, String id, Object obj){
        if(obj_map.containsKey(filename)){
            obj_map.get(filename).put(id, obj);
        }
        else{
            obj_map.put(filename, new HashMap<String, Object>(){{put(id, obj);}});
        }
    }

    /**
     * Retrieves an object from the obj_map
     *
     * @param filename - The filename of the object
     * @param id - the Id of the object
     * @return returns the object which was retrieved
     */
    static Object mapGet(String filename, String id){
        if(ObjInMap(filename, id)){
            return obj_map.get(filename).get(id);
        }
        return null;
    }

    /**
     * Checks to see if the object is in the obj_map
     *
     * @param filename - The filename of the object
     * @param id - the Id of the object
     * @return boolean weather or not it is in the obj_map
     */
    static boolean ObjInMap(String filename, String id){
        return (obj_map.containsKey(filename) && obj_map.get(filename).containsKey(id));
    }

    /**
     * Loads an object from within another object to be assigned by that object
     *
     * @param jobApplicationSystem The job application system that is used.
     * @param clazz - the class of the Object
     * @param filename - The filename of the object
     * @param id - the Id of the object
     * @return the subloaded object
     */
    static Object subLoad(JobApplicationSystem jobApplicationSystem, Class clazz, String filename, String id) {
        if(ObjInMap(filename, id)){
            return mapGet(filename, id);
        }
        else{
            Constructor con;
            try {
                con = clazz.getConstructor(String.class);
                Object obj = con.newInstance(id);
                mapPut(filename, id, obj);
                loader_map.get(mapGet(filename, id).getClass()).loader.loadOne(jobApplicationSystem,
                        mapGet(filename, id));
                return obj;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Loads all Loaders in loader_map
     * @param jobApplicationSystem The job application system being used.
     */
    public static void startLoad(JobApplicationSystem jobApplicationSystem) {
        for(Object x : loader_map.keySet()){
            loader_map.get(x).load(jobApplicationSystem);
        }
        new PreviousLoginDateLoaderAndStorer().loadPreviousLoginDate(jobApplicationSystem);
    }

    // === Instance Methods ===

    /**
     * Loads all objects in this LoaderManager
     * @param jobApplicationSystem The job application system being used.
     */
    private void load(JobApplicationSystem jobApplicationSystem) {
        this.fillMap();
        for(Object obj : this.getArray()){
            this.loader.loadOne(jobApplicationSystem, obj);
        }
    }

    /**
     * Fills the obj_map with objects from json Id's
     */
    private void fillMap(){
        Iterator i = FileSystem.getAllID(this.filename);
        while(i.hasNext()){
            String id = (String)i.next();
            Constructor con;
            try {
                con = clazz.getConstructor(String.class);
                Object obj = con.newInstance(id);
                mapPut(filename, id, obj);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets an array of the Objects in the map
     *
     * @return array of the Objects
     */
    public ArrayList<Object> getArray(){
        ArrayList<Object> out = new ArrayList<>();
        for(String key : this.getHashMap().keySet()){
            out.add(getHashMap().get(key));
        }
        return out;
    }

    /**
     * Gets an HashMap of the Id's to Objects in the map
     *
     * @return The HashMap
     */
    HashMap<String, Object> getHashMap(){
        return obj_map.get(this.filename);
    }

}