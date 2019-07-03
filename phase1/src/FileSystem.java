import org.json.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.*;
import java.util.Iterator;
import java.util.List;

public class FileSystem {

    static HashMap<String, HashMap<String, Object>> load_map = new HashMap<>();

    static ArrayList JArrayToList(JSONArray jarry) throws JSONException {
        ArrayList<Object> list = new ArrayList<Object>();
        for(int i = 0; i < jarry.length(); i++){
            if(jarry.get(i) instanceof JSONArray){
                list.add(JArrayToList((JSONArray) jarry.get(i)));
            }
            else{
                list.add(jarry.get(i));
            }
        }
        return list;
    }

    /**
     * Loads an object as a HashMap
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     * @return returns the Data of this Object as a HashMap
     */
    static HashMap read(String filename, String id) {
        JSONObject jobj = null;
        try {
            jobj = FileToJson(filename).getJSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap map = new HashMap();
        for (Iterator it = jobj.keys(); it.hasNext(); ) {
            String x = (String)it.next();
            try {
                if(jobj.get(x) instanceof JSONArray){
                    map.put(x, JArrayToList((JSONArray) jobj.get(x)));
                }
                else{ map.put(x, jobj.get(x)); }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     *Saves the object in a json library
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being saved
     * @param data The HashMap of data to be saved to the Id
     */
    static void write(String filename, String id, HashMap data){
        JSONObject jobj = FileToJson(filename);
        try {
            jobj.put(id, data);
            PrintWriter pw = new PrintWriter(filename + ".json");
            pw.write(jobj.toString());
            pw.close();
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *Used to create or load JSON Objects form a filename
     *
     * @param filename The name of the .json file to be accessed (Only the name not including .json)
     * @return The JSON object loaded from the .json file
     */
    static private JSONObject FileToJson(String filename){
        Object obj = null;
        JSONObject jobj = null;
        try {
            obj = new JSONParser().parse(new FileReader(filename + ".json"));
            jobj = new JSONObject(obj.toString());
        } catch (IOException | ParseException | JSONException e) {
            jobj = new JSONObject();
            e.printStackTrace();
        }
        return jobj;
    }

    /**
     * Deletes an Id and data from a json file
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     */
    static void delete(String filename, String id){
        JSONObject jobj = FileToJson(filename);
        jobj.remove(id);
        try {
            PrintWriter pw = new PrintWriter(filename + ".json");
            pw.write(jobj.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets an iterable for mass loading of Id's of a file
     *
     * @param filename the name of the json file (Excluding .json)
     * @return An Iterator of Id's
     */
    static Iterator getAllID(String filename){
        return FileToJson(filename).keys();
    }

    /**
     * Returns a boolean on weather or not an Id is in a file
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     * @return boolean if ID in File
     */
    static boolean IDinMemory(String filename, String id){
        Iterator keys = getAllID(filename);
        while(keys.hasNext()){
            if(keys.next().equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * Tests to see weather an object has been loaded
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     * @return True if in load_map
     */
    static boolean isLoaded(String filename, String id){
        if(load_map.containsKey(filename)){
            if(load_map.get(filename).containsKey(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * gets the object if it is already loaded for placement in another objects loading
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     * @return The Object or Null
     */
    static Object mapGet(String filename, String id){
        if(isLoaded(filename, id)){
            return load_map.get(filename).get(id);
        }
        return null;
    }

    /**
     * Puts a Object into the loaded HashMap (Used after said object is loaded)
     *
     * @param filename the name of the json file (Excluding .json)
     * @param id The unique Id of the Item being loaded
     * @param obj the object to put into the HashMap
     */
    static void mapPut(String filename, String id, Object obj){
        if(load_map.containsKey(filename)){
            load_map.get(filename).put(id, obj);
        }
        else{
            HashMap<String, Object> m = new HashMap<>();
            m.put(id, obj);
            load_map.put(filename, m);
        }
    }

    /**
     * A method to load subs of class c granted that they are loadable
     *
     * @param c - The class of the object to be loaded
     * @param filename - the Filename of the Object
     * @param id - The Id of the Object
     * @return The object being subLoaded or null if not possible
     */
    static Object subLoader(Class c, String filename, String id){
        if(FileSystem.isLoaded(filename, id)){
            return mapGet(filename, id);
        }
        else{
            try {
                return c.getConstructor(String.class).newInstance(id);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
