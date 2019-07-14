package UsersAndJobObjects;

import java.io.*;

public class JobApplicationDocument implements Serializable {

    // === Instance variables ===
    // The user to which this document belongs
    private User user;
    // The unique file name of this document
    private String fileName;
    // The contents of this job application document
    private File file;

    // === Constructor ===
    public JobApplicationDocument(User user, File file) {
        this.user = user;
        this.fileName = file.getName();
        this.file = file;
    }

    public JobApplicationDocument(User user, String contents) throws IOException {
        this.user = user;
        this.fileName = user.getUsername() + "_" + user.getUsername() + ".txt";
        this.file = this.createNewFile(this.fileName, contents);
    }

    // === Getters ===
    public String getFileName() {
        return this.fileName;
    }

    public File getFile() {
        return this.file;
    }

    // === Other methods ===
    private File createNewFile(String fileName, String contents) throws IOException {
        File newFile = new File(this.fileName);
        newFile.createNewFile();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        out.println(contents);
        return newFile;
    }
}
