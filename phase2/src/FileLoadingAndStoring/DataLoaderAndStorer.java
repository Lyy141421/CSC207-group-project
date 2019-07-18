package FileLoadingAndStoring;

import CompanyStuff.Company;
import Main.JobApplicationSystem;
import Main.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DataLoaderAndStorer {
    private JobApplicationSystem jobApplicationSystem;
    private String userFilePath;
    private String companyFilePath;
    private String dateFilePath;

    private static final String FSA_TO_CMA_FILENAME = "phase2/files/CMA_per_FSA_Centroid.json";

    public DataLoaderAndStorer(JobApplicationSystem jobApplicationSystem, String userFilePath, String companyFilePath, String dateFilePath) {
        this.jobApplicationSystem = jobApplicationSystem;
        this.userFilePath = userFilePath;
        this.companyFilePath = companyFilePath;
        this.dateFilePath = dateFilePath;
    }

    public void loadAllData() throws ClassNotFoundException, IOException {
        File userFile = new File(this.userFilePath);
        if (userFile.exists()) {
            this.loadUsers();
        } else {
            userFile.createNewFile();
        }

        File companyFile = new File(this.companyFilePath);
        if (companyFile.exists()) {
            this.loadCompanies();
        } else {
            companyFile.createNewFile();
        }

        File dateFile = new File(this.dateFilePath);
        if (dateFile.exists()) {
            this.loadPreviousLoginDate();
        } else {
            dateFile.createNewFile();
        }
    }

    public static HashMap<String, String> loadFSAHashMap() {
        HashMap map = new HashMap();
        Object obj = null;
        JSONObject jobj = null;
        try {
            obj = new JSONParser().parse(new FileReader(FSA_TO_CMA_FILENAME));
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        try {
            jobj = new JSONObject(obj.toString());
            Iterator iterator = jobj.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = jobj.getString(key);
                map.put(key, value);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Reads all users from the file.
     *
     * @throws ClassNotFoundException
     */
    private void loadUsers() throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(this.userFilePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.jobApplicationSystem.getUserManager().setAllUsers((ArrayList<User>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reads all companies from the file.
     *
     * @throws ClassNotFoundException
     */
    private void loadCompanies() throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(this.companyFilePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.jobApplicationSystem.setCompanies((ArrayList<Company>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the previous login date saved in "PreviousLoginDate.txt' in memory.
     */
    private void loadPreviousLoginDate() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(this.dateFilePath))) {
            String dateString = fileReader.readLine().trim();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, dtf);
            this.jobApplicationSystem.setPreviousLoginDate(date);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Saves all the data to the appropriate files.
     *
     * @throws IOException
     */
    public void storeAllData() throws IOException {
        this.storeUsers();
        this.storeCompanies();
        this.storePreviousLoginDate();
    }

    /**
     * Saves all users to the appropriate file.
     *
     * @throws IOException
     */
    private void storeUsers() throws IOException {
        OutputStream file = new FileOutputStream(this.userFilePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this.jobApplicationSystem.getUserManager().getAllUsers());
        output.close();
    }

    /**
     * Saves all companies to the appropriate file.
     *
     * @throws IOException
     */
    private void storeCompanies() throws IOException {
        OutputStream file = new FileOutputStream(this.companyFilePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this.jobApplicationSystem.getCompanies());
        output.close();
    }

    /**
     * Stores the previous login date in 'PreviousLoginDate.txt'.
     *
     */
    private void storePreviousLoginDate() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dateFilePath)))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = this.jobApplicationSystem.getToday().format(dtf);
            out.println(dateString);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
