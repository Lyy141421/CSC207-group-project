package ApplicantStuff;

import CompanyStuff.Company;

import java.io.*;


public class JobApplicationDocument implements Serializable {

    // === Instance variables ===
    // The folder under which this file is stored
    private File folder;
    // The contents of this job application document
    private File file;

    // === Public methods ===
    // TODO
    public void submit(Company company) {

    }


    // ============================================================================================================== //
    // === Package-private methods ===
    // === Constructor ===
    JobApplicationDocument(File folder, File file) {
        this.folder = folder;
        this.file = file;
        String filePath = this.folder.getPath() + File.pathSeparator + this.file.getName();
        File newFileDest = new File(filePath);
        if (newFileDest.exists()) {
            this.changeFileDestination(newFileDest);
        }
        this.file.renameTo(newFileDest);
    }

    JobApplicationDocument(File folder, String fileType, String contents) {
        this.folder = folder;
        String filePath = this.folder.getPath() + File.pathSeparator + fileType + ".txt";
        File newFile = this.createNewFile(filePath, contents);
        if (newFile.exists()) {
            this.changeFileDestination(newFile);
        }
        this.file = newFile;
    }

    // === Getters ===

    public File getFile() {
        return this.file;
    }

    // === Setters ===
    void setFile(File newFile) {
        this.file = newFile;
    }

    // === Other methods ===
    private File createNewFile(String filePath, String contents) {
        File newFile = new File(filePath);
        try {
            newFile.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
            out.println(contents);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return newFile;
    }

    /**
     * Separate the extension of this file path.
     *
     * @param filePath The file path being stripped.
     * @return an array of the filename (without extension) and the extension,
     */
    private String[] separateExtension(String filePath) {
        int indexOfLastDot = filePath.lastIndexOf(".");
        return new String[]{filePath.substring(0, indexOfLastDot), filePath.substring(indexOfLastDot)};
    }

    /**
     * Change the file destination for this file.
     *
     * @param file The file in question.
     */
    private void changeFileDestination(File file) {
        String filePath = file.getPath();
        String[] fileNameAndExtension = this.separateExtension(filePath);
        int i = 1;
        File dest = new File(fileNameAndExtension[0] + "(" + i + ")" + fileNameAndExtension[1]);
        while (dest.exists()) {
            i++;
            dest = new File(fileNameAndExtension[0] + "(" + i + ")" + fileNameAndExtension[1]);
        }
        file.renameTo(dest);
    }
}
