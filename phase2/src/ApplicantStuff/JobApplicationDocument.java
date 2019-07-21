package ApplicantStuff;

import DocumentManagers.ApplicantDocumentManager;
import JobPostings.BranchJobPosting;
import DocumentManagers.CompanyDocumentManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JobApplicationDocument implements Serializable {

    // === Instance variables ===
    // The contents of this job application document
    private File file;


    // === Public methods ===

    // === Constructor for references ===
    public JobApplicationDocument(File file) {
        this.file = file;
    }

    // === Constructors for applicants ===
    public JobApplicationDocument(File file, String username) {
        File folder = new File(ApplicantDocumentManager.FOLDER + "/" + username);
        String filePath = folder.getPath() + "/" + file.getName();
        this.copyFile(file, filePath);
        this.file = new File(filePath);
    }

    /**
     * Submit this job application document to the company.
     *
     * @param jobPosting The job posting for which this application is being submitted.
     * @param applicant  The applicant for which this document is being submitted.
     */
    // TODO fix
    public void submit(BranchJobPosting jobPosting, Applicant applicant) {
        String applicantFolderInJobPostingPath = CompanyDocumentManager.FOLDER + "/" + jobPosting.getBranch().getName()
                + "/" + jobPosting.getId() + "_" + jobPosting.getTitle() + "/" + applicant.getUsername();
        String companyDestinationPath = applicantFolderInJobPostingPath + "/" + this.file.getName();
        File applicantFolder = new File(applicantFolderInJobPostingPath);
        try {
            if (!applicantFolder.exists()) {
                applicantFolder.mkdirs();
                applicantFolder.createNewFile();
            }
            this.copyFile(companyDestinationPath);
        } catch (IOException io) {
            io.printStackTrace();
        }
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

    // === Constructor for applicants submitting cover letter or CV from text box ===
    JobApplicationDocument(String contents, String fileType, Applicant applicant) {
        File folder = new File(ApplicantDocumentManager.FOLDER + "/" + applicant.getUsername());
        String filePath = folder.getPath() + "/" + fileType + ".txt";
        if (Paths.get(filePath).toFile().exists()) {
            filePath = this.getNewFilePath(filePath);
        }
        this.file = this.createNewFile(filePath, contents);
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
     * @param oldFilePath   The file path to be replaced.
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

    public static void main(String[] args) {
        String folderPath = "./uploadedDocuments/something/else";
        File file = new File(folderPath);
        file.mkdirs();
        try {
            File newFile = new File(folderPath + "/text.txt");
            newFile.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }


    }

}
