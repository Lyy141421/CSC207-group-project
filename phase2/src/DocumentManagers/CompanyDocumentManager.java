package DocumentManagers;

import ApplicantStuff.JobApplication;
import CompanyStuff.Company;
import JobPostings.BranchJobPosting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CompanyDocumentManager extends DocumentManager<Company> implements Observer {

    // === Class variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/companies";

    // === Instance variables ===
    private Company company;

    CompanyDocumentManager(Company company) {
        super(company);
        this.company = company;
        this.setFolder(new File(FOLDER + "/" + this.getObject().getName()));
        this.makeSubDirectories();
    }

    /**
     * Make subdirectories for this applicant.
     */
    private void makeSubDirectories() {
        File branchFolder = new File(this.getFolder().getPath() + "/branches");
        try {
            branchFolder.mkdir();
            branchFolder.createNewFile();
            File jobPostingsFolder = new File(branchFolder.getPath() + "/jobPostings");
            try {
                jobPostingsFolder.mkdir();
                jobPostingsFolder.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a list of files submitted with this job application.
     *
     * @param jobApplication The job application submitted.
     * @return the list of files submitted with this job application.
     */
    public File[] getFilesForJobApplication(JobApplication jobApplication) {
        BranchJobPosting jobPosting = jobApplication.getJobPosting();
        String jobPostingFolderName = jobPosting.getId() + "_" + jobPosting.getTitle();
        File jobPostingFolder = new File(jobPostingFolderName);
        File[] userFolders = jobPostingFolder.listFiles();
        String username = jobApplication.getApplicant().getUsername();
        for (File userFolder : userFolders) {
            if (userFolder.getName().equals(username)) {
                return userFolder.listFiles();
            }
        }
        return null;
    }


    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this branch
    }
}
