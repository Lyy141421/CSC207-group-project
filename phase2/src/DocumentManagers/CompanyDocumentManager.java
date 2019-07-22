package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.JobPostings.BranchJobPosting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CompanyDocumentManager extends DocumentManager {

    // === Class variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/companies";

    // === Instance variables ===
    private Company company;

    CompanyDocumentManager(Company company) {
        this.company = company;
        this.setFolder(new File(FOLDER + "/" + this.company.getName()));
        for (Branch branch : company.getBranches()) {
            this.createBranchSubDirectories(branch);
        }
    }

    /**
     * Make subdirectories for this company.
     */
    private void createBranchSubDirectories(Branch branch) {
        File branchFolder = new File(this.getFolder().getPath() + "/" + branch.getName());
        try {
            branchFolder.mkdirs();
            branchFolder.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create a sub directory for this branch.
     * @param branch    The branch that has been added to this company.
     */
    public void createBranchFolder(Branch branch) {
        File branchFolder = new File(this.getFolder().getPath() + "/" + branch.getName());
        try {
            branchFolder.mkdir();
            branchFolder.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create a folder for this branch job posting.
     *
     * @param branchJobPosting The branch job posting for which a folder is to be created.
     * @return the folder that was created.
     */
    private File createBranchJobPostingFolder(BranchJobPosting branchJobPosting) {
        Branch branch = branchJobPosting.getBranch();
        File branchFolder = new File(this.getFolder().getPath() + "/" + branch.getName());
        File jobPostingFolder = new File(branchFolder.getPath() + "/" + branchJobPosting.getId() + "_" +
                branchJobPosting.getTitle());
        jobPostingFolder.mkdir();
        try {
            jobPostingFolder.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jobPostingFolder;
    }

    /**
     * Get the folder for this job posting.
     *
     * @param branchJobPosting The job posting in question.
     * @return the folder for this job posting.
     */
    private File getFolderForJobPosting(BranchJobPosting branchJobPosting) {
        Branch branch = branchJobPosting.getBranch();
        File branchFolder = new File(this.getFolder().getPath() + "/" + branch.getName());
        return new File(branchFolder.getPath() + "/" + branchJobPosting.getId() + "_" +
                branchJobPosting.getTitle());
    }

    /**
     * Create a folder for the applicant who submitted this application.
     *
     * @param jobPostingFolder The job posting folder where the application folders are to be created.
     * @param jobApp           The job application for which the folder is to be created.
     * @return the folder created.
     */
    private File createApplicationFolder(File jobPostingFolder, JobApplication jobApp) {
        Applicant applicant = jobApp.getApplicant();
        File applicationFolder = new File(jobPostingFolder.getPath() + "/" + applicant.getUsername());
        applicationFolder.mkdir();
        try {
            applicationFolder.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return applicationFolder;
    }

    /**
     * Get the folder that belongs to this job application.
     * @param jobApplication    The job application in question.
     * @return the folder that holds this job application's submitted documents.
     */
    private File getFolderForJobApplication(JobApplication jobApplication) {
        BranchJobPosting jobPosting = jobApplication.getJobPosting();
        File jobPostingFolder = this.getFolderForJobPosting(jobPosting);
        File[] userFolders = jobPostingFolder.listFiles();
        String username = jobApplication.getApplicant().getUsername();
        for (File userFolder : userFolders) {
            if (userFolder.getName().equals(username)) {
                return userFolder;
            }
        }
        return null;
    }

    /**
     * Get a list of files submitted with this job application.
     *
     * @param jobApplication The job application submitted.
     * @return the list of files submitted with this job application.
     */
    public File[] getFilesForJobApplication(JobApplication jobApplication) {
        return this.getFolderForJobApplication(jobApplication).listFiles();
    }

    /**
     * Add files for this job application in the respective branch's folder.
     *
     * @param jobApp         The job application that has been submitted.
     * @param filesSubmitted The list of files to be submitted along with the application.
     */
    public void addFilesForJobApplication(JobApplication jobApp, ArrayList<File> filesSubmitted) {
        File folder = this.getFolderForJobApplication(jobApp);
        for (File fileToSubmit : filesSubmitted) {
            JobApplicationDocument document = new JobApplicationDocument(fileToSubmit);
            document.copyFile(folder + "/" + fileToSubmit.getName());
        }
    }

    /**
     * Check whether documents have already been transferred for this job posting.
     *
     * @param jobPosting The job posting in question.
     * @return true iff documents have already been transferred for this job posting.
     */
    public boolean applicationDocumentsTransferred(BranchJobPosting jobPosting) {
        return this.getFolderForJobPosting(jobPosting).exists();
    }

    /**
     * Make documents submitted for applications to this job posting to be visible to the branch.
     *
     * @param branchJobPosting The job posting that has recently closed for further applications.
     */
    public void transferApplicationDocuments(BranchJobPosting branchJobPosting) {
        File jobPostingFolder = this.createBranchJobPostingFolder(branchJobPosting);
        ArrayList<JobApplication> jobApps = branchJobPosting.getJobApplications();
        for (JobApplication jobApp : jobApps) {
            File applicationFolder = this.createApplicationFolder(jobPostingFolder, jobApp);
            for (JobApplicationDocument jobAppDoc : jobApp.getFilesSubmitted()) {
                String destinationPath = applicationFolder.getPath() + "/" + jobAppDoc.getFile().getName();
                jobAppDoc.copyFile(destinationPath);
            }
        }
    }
}
