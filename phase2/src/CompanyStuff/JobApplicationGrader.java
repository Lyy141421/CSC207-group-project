package CompanyStuff;

import ApplicantStuff.JobApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class JobApplicationGrader {

    // TODO make this better

    // === Instance variable ===
    private ArrayList<String> keyWordsAndPhrases;
    private HashMap<JobApplication, Integer> jobAppToGrade;
    private int grade;

    // === Constructor ===
    JobApplicationGrader(ArrayList<JobApplication> jobApps, ArrayList<String> keyWordsAndPhrases) {
        this.keyWordsAndPhrases = keyWordsAndPhrases;

        for (JobApplication jobApp : jobApps) {
            jobAppToGrade.put(jobApp, 0);
        }
    }

    // === Getter ===

    int getGrade(JobApplication jobApp) {
        return this.jobAppToGrade.get(jobApp);
    }


    // === Other methods ===

    public void gradeAllJobApps() {
        for (JobApplication jobApp : this.jobAppToGrade.keySet()) {
            this.jobAppToGrade.replace(jobApp, this.gradeJobApplication(jobApp));
        }
    }

    /**
     * Grade this job application based on the number of occurrences of items in keyWordsAndPhrases in the files
     * submitted with this application.
     * @param jobApplication    The job application to be graded
     * @return the grade received
     */
    private int gradeJobApplication(JobApplication jobApplication) {
        Company company = jobApplication.getJobPosting().getCompany();
        File[] filesSubmitted = company.getDocumentManager().getFilesForJobApplication(jobApplication);
        for (File file : filesSubmitted) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().toLowerCase();
                    for (String keyWordOrPhrase : this.keyWordsAndPhrases) {
                        for (int i = 0; (i = line.indexOf(keyWordOrPhrase, i)) != -1; i += keyWordOrPhrase.length()) {
                            grade++;
                        }
                    }
                }
                scanner.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return grade;
    }


}
