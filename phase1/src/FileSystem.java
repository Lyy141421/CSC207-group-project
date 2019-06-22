import org.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.json.simple.parser.*;
import java.util.Iterator;

public class FileSystem {

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
}
