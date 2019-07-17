package GUIClasses;

public class DataLoaderAndSaver {
    /*private static String userFilePath;
    private static String companyFilePath;

    public DataLoaderAndSaver(String userFilePath, String companyFilePath) throws ClassNotFoundException, IOException {
        DataLoaderAndSaver.userFilePath = userFilePath;
        DataLoaderAndSaver.companyFilePath = companyFilePath;

        File userFile = new File(userFilePath);
        if (userFile.exists()) {
            DataLoaderAndSaver.readUsersFromFile(userFilePath);
        } else {
            userFile.createNewFile();
        }

        File companyFile = new File(companyFilePath);
        if (companyFile.exists()) {
            DataLoaderAndSaver.readCompaniesFromFile(companyFilePath);
        } else {
            companyFile.createNewFile();
        }
    }

    private static void readUsersFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            UserManager.setAllUsers((ArrayList<User>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void readCompaniesFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            JobApplicationSystem.setCompanies((ArrayList<Branch>) input.readObject());
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveAllData() throws IOException {
        DataLoaderAndSaver.saveUsersToFile(DataLoaderAndSaver.userFilePath);
        DataLoaderAndSaver.saveCompaniesToFile(DataLoaderAndSaver.companyFilePath);
    }

    private static void saveUsersToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(UserManager.getAllUsers());
        output.close();
    }

    private static void saveCompaniesToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(JobApplicationSystem.getCompanies());
        output.close();
    }*/
}
