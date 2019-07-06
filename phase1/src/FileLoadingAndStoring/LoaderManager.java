package FileLoadingAndStoring;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LoaderManager {
    //The List of all Loaders
    private static HashMap<Class, LoaderManager> loader_map = new HashMap<>();
    //The Hashmap of objects by Filename and then by ID
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

    static void mapPut(String filename, String id, Object obj){
        if(obj_map.containsKey(filename)){
            obj_map.get(filename).put(id, obj);
        }
        else{
            obj_map.put(filename, new HashMap<String, Object>(){{put(id, obj);}});
        }
    }

    static Object mapGet(String filename, String id){
        if(ObjInMap(filename, id)){
            return obj_map.get(filename).get(id);
        }
        return null;
    }

    static boolean ObjInMap(String filename, String id){
        return (obj_map.containsKey(filename) && obj_map.get(filename).containsKey(id));
    }

    static Object subLoad(Class clazz, String filename, String id){
        if(ObjInMap(filename, id)){
            return mapGet(filename, id);
        }
        else{
            Constructor con = null;
            try {
                con = clazz.getConstructor(String.class);
                Object obj = con.newInstance(id);
                mapPut(filename, id, obj);
                loader_map.get(mapGet(filename, id).getClass()).loader.loadOne(mapGet(filename, id));
                return obj;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void startLoad(){
        for(Object x : loader_map.keySet()){
            loader_map.get(x).load();
        }
    }

    // === Instance Methods ===

    private void load(){
        this.fillMap();
        this.loadAll();
    }

    private void fillMap(){
        Iterator i = FileSystem.getAllID(this.filename);
        while(i.hasNext()){
            String id = (String)i.next();
            Constructor con = null;
            try {
                con = clazz.getConstructor(String.class);
                Object obj = con.newInstance(id);
                mapPut(filename, id, obj);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    void loadAll(){
        for(Object obj : this.getArray()){
            this.loader.loadOne(obj);
        }
    }

    public ArrayList<Object> getArray(){
        ArrayList<Object> out = new ArrayList<>();
        for(String key : this.getHashMap().keySet()){
            out.add(getHashMap().get(key));
        }
        return out;
    }

    HashMap<String, Object> getHashMap(){
        return obj_map.get(this.filename);
    }

}
















//    // === Class Variables ===
//    //The List of all Loaders
//    private static ArrayList<Loader> loader_list = new ArrayList<>();
//
//    // === Instance Variables ===
//    //A List of all objects to load and save
//    private  ArrayList<Storable> obj_list = new ArrayList<>();
//    //The class of object in this FileLoadingAndStoring.Loader
//    private Class clazz;
//    //Filename of objects in this loader
//    private String filename;
//
//
//











//    // === Constructors ===
//
//    /**
//     * Creates an instance of FileLoadingAndStoring.Loader
//     *
//     * @param clazz - The class of this loaders objects
//     * @param FILENAME - The filename of the Objects in this loader
//     */
//    public Loader(Class clazz, String FILENAME){
//        this.clazz = clazz;
//        this.filename = FILENAME;
//        loader_list.add(this);
//    }
//
//    /**
//     * Creates an instance of FileLoadingAndStoring.Loader
//     *
//     * @param clazz - The class of this loaders objects
//     * @param FILENAME - The filename of the Objects in this loader
//     * @param obj_list - A pre-made list of objects to draw from
//     */
//    Loader(Class clazz, String FILENAME, ArrayList<Storable> obj_list){
//        this.clazz = clazz;
//        this.filename = FILENAME;
//        this.obj_list = obj_list;
//        loader_list.add(this);
//    }
//
//    // === Static Methods ===
//
//    /**
//     * A method to save all storable elements in an ArrayList
//     *
//     * @param list - an ArrayList of FileLoadingAndStoring.Storable Elements
//     */
//    static void saveList(ArrayList<Storable> list){
//        for(Storable x : list){
//            x.saveSelf();
//        }
//    }
//
//    /**
//     * Loads all objects in the loader at the start of the program
//     */
//    public static void startLoad(){
//        for(Loader loader : loader_list){
//            loader.fillList();
//            loader.loadALl();
//        }
//    }
//
//    /**
//     * Saves all objects in the loader at the end of the program
//     */
//    public static void endSave(){
//        for(Loader loader : loader_list){
//            loader.saveAll();
//        }
//        for(Object x : FileSystem.load_map.keySet()){
//            for(Object y : FileSystem.load_map.get(x).keySet()){
//                ((Storable)FileSystem.load_map.get(x).get(y)).saveSelf();
//            }
//        }
//    }
//
//    // === FileLoadingAndStoring.Loader Methods ===
//
//    /**
//     * Fills the obj_list from json memory by Instantiating via Id
//     */
//    void fillList(){
//        Iterator i = FileSystem.getAllID(this.filename);
//        while(i.hasNext()){
//            try {
//                String s = (String) i.next();
//                if(!FileSystem.isLoaded(this.filename, s)) {
//                    Constructor con = this.clazz.getConstructor(String.class);
//                    Object obj = con.newInstance((String) s);
//                    this.obj_list.add((Storable) obj);
//                    ((Storable) obj).loadSelf();
//                    FileSystem.mapPut(this.filename, s, obj);
//                }
//            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Loads Object in this loader
//     */
//    void loadALl(){
//        for(Storable x : this.obj_list){
//            x.loadSelf();
//        }
//    }
//
//    /**
//     * Saves all objects in the loader
//     */
//    void saveAll(){
//        for(Storable x : this.obj_list){
//            x.saveSelf();
//        }
//    }
//
//    // === Getters ===
//
//    /**
//     * Gets the list of objects in this loader
//     *
//     * @return - the list of Objects
//     */
//    ArrayList<Storable> getObj_list(){
//        return this.obj_list;
//    }
//
//
//
//}
