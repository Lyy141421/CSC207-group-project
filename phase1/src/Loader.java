import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

class Loader {

    // === Class Variables ===
    //The List of all Loaders
    private static ArrayList<Loader> loader_list = new ArrayList<>();

    // === Instance Variables ===
    //A List of all objects to load and save
    private  ArrayList<Storable> obj_list = new ArrayList<>();
    //The class of object in this Loader
    private Class clazz;
    //Filename of objects in this loader
    private String filename;

    // === Constructors ===

    /**
     * Creates an instance of Loader
     *
     * @param clazz - The class of this loaders objects
     * @param FILENAME - The filename of the Objects in this loader
     */
    Loader(Class clazz, String FILENAME){
        this.clazz = clazz;
        this.filename = FILENAME;
        loader_list.add(this);
    }

    /**
     * Creates an instance of Loader
     *
     * @param clazz - The class of this loaders objects
     * @param FILENAME - The filename of the Objects in this loader
     * @param obj_list - A pre-made list of objects to draw from
     */
    Loader(Class clazz, String FILENAME, ArrayList<Storable> obj_list){
        this.clazz = clazz;
        this.filename = FILENAME;
        this.obj_list = obj_list;
        loader_list.add(this);
    }

    // === Static Methods ===

    /**
     * Loads all objects in the loader at the start of the program
     */
    static void startLoad(){
        for(Loader loader : loader_list){
            loader.fillList();
            loader.loadALl();
        }
    }

    /**
     * Saves all objects in the loader at the end of the program
     */
    static void endSave(){
        for(Loader loader : loader_list){
            loader.saveAll(); //TODO use FileSystem Map for this
        }
    }

    // === Loader Methods ===

    /**
     * Fills the obj_list from json memory by Instantiating via Id
     */
    void fillList(){
        Iterator i = FileSystem.getAllID(this.filename);
        while(i.hasNext()){
            try {
                Constructor con = this.clazz.getConstructor(String.class);
                Object obj = con.newInstance((String)i.next());
                this.obj_list.add((Storable) obj);
                ((Storable) obj).loadSelf();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads Object in this loader
     */
    void loadALl(){
        for(Storable x : this.obj_list){
            x.loadSelf();
        }
    }

    /**
     * Saves all objects in the loader
     */
    void saveAll(){
        for(Storable x : this.obj_list){
            x.saveSelf();
        }
    }

    // === Getters ===

    /**
     * Gets the list of objects in this loader
     *
     * @return - the list of Objects
     */
    ArrayList<Storable> getObj_list(){
        return this.obj_list;
    }



}
