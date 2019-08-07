package FileLoadingAndStoring;

import ApplicantStuff.JobApplication;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.JobPostings.CompanyJobPosting;
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

    // File names of storage files
    private static final String FILES_FOLDER_PATH = "./files";
    private static final String ALL_DATA_FILE_PATH = FILES_FOLDER_PATH + "/data.ser";
    //    private static final String COMPANY_FILE_PATH = FILES_FOLDER_PATH + "/companies.ser";
    private static final String MISCELLANEOUS_FILE_PATH = FILES_FOLDER_PATH + "/date_and_static_variables.txt";
    private static final String FSA_TO_CMA_PATH = FILES_FOLDER_PATH + "/CMA_per_FSA_Centroid.json";

    // === Instance variable ===
    private JobApplicationSystem jobApplicationSystem;

    // === Constructor ===
    public DataLoaderAndStorer(JobApplicationSystem jobApplicationSystem) {
        this.jobApplicationSystem = jobApplicationSystem;
    }

    /**
     * Loads all the data into memory
     */
    public void loadAllData() {
        try {
            this.createFilesFolder();
//            String[] filePaths = new String[]{ALL_DATA_FILE_PATH, COMPANY_FILE_PATH, MISCELLANEOUS_FILE_PATH};
            String[] filePaths = new String[]{ALL_DATA_FILE_PATH, MISCELLANEOUS_FILE_PATH};
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (file.exists()) {
                    this.loadRespectiveData(filePath);
                } else {
                    file.createNewFile();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Load all data is called");
    }

    /**
     * Create the files folder.
     *
     * @throws IOException
     */
    private void createFilesFolder() throws IOException {
        File filesFolder = new File(FILES_FOLDER_PATH);
        if (!filesFolder.exists()) {
            filesFolder.mkdir();
            filesFolder.createNewFile();
        }
    }

    /**
     * Load the respective data based on the file path.
     *
     * @param filePath The file path where the data is stored.
     */
    private void loadRespectiveData(String filePath) {
        switch (filePath) {
            case ALL_DATA_FILE_PATH:
                this.loadUsers();
                break;
            case MISCELLANEOUS_FILE_PATH:
                this.loadPreviousLoginDateAndStaticVariables();
        }
    }

    /**
     * Load the FSA hash map (for location purposes).
     *
     * @return the hash map loaded.
     *
     */
    public static HashMap<String, String> loadFSAHashMap() {
        HashMap map = new HashMap();
        Object obj = null;
        JSONObject jobj;
        try {
            obj = new JSONParser().parse(new FileReader(FSA_TO_CMA_PATH));
        } catch (IOException | ParseException e) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Reads all users from the file.
     *
     */
    private void loadUsers() {
        try {
            InputStream file = new FileInputStream(ALL_DATA_FILE_PATH);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.jobApplicationSystem.getUserManager().setAllUsers((ArrayList<User>) input.readObject());
            input.close();
        } catch (EOFException eof) {    // empty file
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
//
//    /**
//     * Reads all companies from the file.
//     *
//     */
//    private void loadCompanies() {
//        try {
//            InputStream file = new FileInputStream(COMPANY_FILE_PATH);
//            InputStream buffer = new BufferedInputStream(file);
//            ObjectInput input = new ObjectInputStream(buffer);
//
//            this.jobApplicationSystem.setCompanies((ArrayList<Company>) input.readObject());
//            input.close();
//        } catch (EOFException eof) {    // empty file
//        } catch (IOException | ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//    }

    /**
     * Loads the previous login date saved in "PreviousLoginDate.txt' in memory.
     */
    private void loadPreviousLoginDateAndStaticVariables() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(MISCELLANEOUS_FILE_PATH))) {
            String dateString = fileReader.readLine().trim();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, dtf);
            this.jobApplicationSystem.setPreviousLoginDate(date);
            int totalNumJobPostings = Integer.parseInt(fileReader.readLine().trim());
            CompanyJobPosting.setTotalNumOfJobPostings(totalNumJobPostings);
            int totalNumInterviews = Integer.parseInt(fileReader.readLine().trim());
            Interview.setTotalNumOfInterviews(totalNumInterviews);
            int totalNumApplications = Integer.parseInt(fileReader.readLine().trim());
            JobApplication.setTotalNumOfApplications(totalNumApplications);
        } catch (NullPointerException npe) {    // empty file
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void refreshAllData() {
        this.storeAllData();
        this.loadAllData();
    }

    /**
     * Saves all the data to the appropriate files.
     *
     */
    public void storeAllData() {
        System.out.println("Store all data is called");
//        this.storeCompanies();
        this.storeUsers();
        this.storePreviousLoginDateAndStaticVariables();
    }

    /**
     * Saves all users to the appropriate file.
     *
     */
    private void storeUsers() {
        try {
            OutputStream file = new FileOutputStream(ALL_DATA_FILE_PATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            output.writeObject(this.jobApplicationSystem.getUserManager().getAllUsers());
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    /**
//     * Saves all companies to the appropriate file.
//     *
//     */
//    private void storeCompanies() {
//        try {
//            OutputStream file = new FileOutputStream(COMPANY_FILE_PATH);
//            OutputStream buffer = new BufferedOutputStream(file);
//            ObjectOutput output = new ObjectOutputStream(buffer);
//
//            output.writeObject(this.jobApplicationSystem.getCompanies());
//            output.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    /**
     * Stores the previous login date in 'PreviousLoginDate.txt'.
     *
     */
    private void storePreviousLoginDateAndStaticVariables() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(MISCELLANEOUS_FILE_PATH)))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = this.jobApplicationSystem.getToday().format(dtf);
            out.println(dateString);
            int totalNumJobPostings = CompanyJobPosting.getTotalNumOfJobPostings();
            out.println(totalNumJobPostings);
            int totalNumInterviews = Interview.getTotalNumOfInterviews();
            out.println(totalNumInterviews);
            int totalNumApplications = JobApplication.getTotalNumOfApplications();
            out.println(totalNumApplications);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}