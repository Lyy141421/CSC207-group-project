package ApplicantStuff;

import DocumentManagers.ApplicantDocumentManager;
import DocumentManagers.ReferenceLetterDocumentManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JobApplicationDocument implements Serializable {

    // === Class variables ===
    static final long serialVersionUID = 1L;

    // === Instance variables ===
    // The contents of this job application document
    private File file;


    // === Public methods ===
    // === Constructor for references ===
    /*
     * Precondition: the file must already be created.
     */
    public JobApplicationDocument(File file, File destinationFolder) {
        this.file = file;
        String filePath = destinationFolder.getPath() + "/" + file.getName();
        this.copyFile(file, filePath);
        this.file = new File(filePath);
        this.file.setReadOnly();
    }

    // === Constructors for applicants submitting a file ===
    /*
     * Precondition: the file must already be created.
     */
    public JobApplicationDocument(File file, String username) {
        File folder = new File(ApplicantDocumentManager.FOLDER_PATH + "/" + username);
        String filePath = folder.getPath() + "/" + file.getName();
        this.copyFile(file, filePath);
        this.file = new File(filePath);
        this.file.setReadOnly();
    }

    // === Constructor for applicants submitting cover letter or CV from text box ===
    public JobApplicationDocument(String contents, String fileType, String username) {
        File folder = new File(ApplicantDocumentManager.FOLDER_PATH + "/" + username);
        String filePath = folder.getPath() + "/" + fileType + ".txt";
        if (Paths.get(filePath).toFile().exists()) {
            filePath = this.getNewFilePath(filePath);
        }
        this.file = this.createNewFile(filePath, contents);
        this.file.setReadOnly();
    }

    // === Getters ===
    public File getFile() {
        return this.file;
    }

    public void copyFile(String destinationPath) {
        this.copyFile(this.file, destinationPath);
    }

    private void copyFile(File file, String destinationPath) {
        if (Paths.get(destinationPath).toFile().exists()) {
            destinationPath = this.getNewFilePath(destinationPath);
        }
        try {
            Files.copy(Paths.get(file.getPath()), Paths.get(destinationPath));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    // ============================================================================================================== //
    // === Package-private methods ===

    // === Private methods ===

    /**
     * Creates a new text file at this file path with these contents. To be called when applicant writes CV/cover letter
     * from a text box
     *
     * @param filePath The file path of this file.
     * @param contents The contents of this file.
     * @return the new file created.
     */
    private File createNewFile(String filePath, String contents) {
        File newFile = new File(filePath);
        try {
            newFile.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
            out.print(contents);
            out.close();
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
     * @param oldFilePath The file path to be replaced.
     */
    private String getNewFilePath(String oldFilePath) {
        String[] fileNameAndExtension = this.separateExtension(oldFilePath);
        int i = 1;
        String newFilePath = fileNameAndExtension[0] + "(" + i + ")" + fileNameAndExtension[1];
        while (Paths.get(newFilePath).toFile().exists()) {
            i++;
            newFilePath = fileNameAndExtension[0] + "(" + i + ")" + fileNameAndExtension[1];
        }
        return newFilePath;
    }
}
