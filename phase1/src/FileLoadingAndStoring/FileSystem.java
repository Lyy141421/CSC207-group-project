package FileLoadingAndStoring;

import org.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.*;
import java.util.Iterator;

public class FileSystem {

    /**
     * Converts a JsonArray to a list
     *
     * @param jarry - the JSONArray to be converted
     * @return Returns the list of the JsonArray
     * @throws JSONException
     */
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
    public static HashMap read(String filename, String id) {
        JSONObject jobj = null;
        try {
            jobj = FileToJson(filename).getJSONObject("data").getJSONObject(id);
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
    public static void write(String filename, String id, HashMap data){
        JSONObject jobj = FileToJson(filename);
        try {
            ((JSONObject)jobj.get("data")).put(id, data);
            PrintWriter pw = new PrintWriter("phase1/files/" + filename + ".json");
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
            obj = new JSONParser().parse(new FileReader("phase1/files/" + filename + ".json"));
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
            PrintWriter pw = new PrintWriter("phase1/files/" + filename + ".json");
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
        try {
            return FileToJson(filename).getJSONObject("data").keys();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
}
