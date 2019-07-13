package FileLoadingAndStoring;

import Main.JobApplicationSystem;
import Managers.UserManager;
import UsersAndJobObjects.Company;
import UsersAndJobObjects.User;

import java.io.*;
import java.util.ArrayList;

public class DataLoaderAndStorer {
    private JobApplicationSystem JAS;
    private String userFilePath;
    private String companyFilePath;


    public DataLoaderAndStorer(JobApplicationSystem JAS, String userFilePath, String companyFilePath)
            throws ClassNotFoundException, IOException {
        this.JAS = JAS;
        this.userFilePath = userFilePath;
        this.companyFilePath = companyFilePath;

        File userFile = new File(userFilePath);
        if (userFile.exists()) {
            this.readUsersFromFile(userFilePath);
        } else {
            userFile.createNewFile();
        }

        File companyFile = new File(companyFilePath);
        if (companyFile.exists()) {
            this.readCompaniesFromFile(companyFilePath);
        } else {
            companyFile.createNewFile();
        }
    }

    /**
     * Reads all users from the file.
     *
     * @param path The file path where users are stored.
     * @throws ClassNotFoundException
     */
    private void readUsersFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.JAS.getUserManager().setAllUsers((ArrayList<User>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reads all companies from the file.
     *
     * @param path The file path where companies are stored.
     * @throws ClassNotFoundException
     */
    private void readCompaniesFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            this.JAS.setCompanies((ArrayList<Company>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves all the data to the appropriate files.
     *
     * @throws IOException
     */
    public void saveAllData() throws IOException {
        this.saveUsersToFile(this.userFilePath);
        this.saveCompaniesToFile(this.companyFilePath);
    }

    /**
     * Saves all users to the appropriate file.
     *
     * @param filePath The file path where the users are stored.
     * @throws IOException
     */
    private void saveUsersToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this.JAS.getUserManager().getAllUsers());
        output.close();
    }

    /**
     * Saves all companies to the appropriate file.
     *
     * @param filePath The file path where the companies are stored.
     * @throws IOException
     */
    private void saveCompaniesToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(this.JAS.getCompanies());
        output.close();
    }
}
