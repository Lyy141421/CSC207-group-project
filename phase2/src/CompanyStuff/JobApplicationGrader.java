package CompanyStuff;

import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class JobApplicationGrader {
    /**
     * Class that grades a job application based on the number of occurrences of key words and phrases in the files submitted.
     */

    // === Instance variable ===
    private ArrayList<String> keyWordsAndPhrases;   // The key words and phrases the HR Coordinator inputs.
    private ArrayList<JobApplication> jobApps;  // The job applications submitted for a job posting.
    private ArrayList<GradedJobApplication> gradedJobApps = new ArrayList<>();  // The graded job applications

    // === Constructor ===
    public JobApplicationGrader(BranchJobPosting jobPosting, ArrayList<String> keyWordsAndPhrases) {
        this.jobApps = jobPosting.getJobApplications();
        this.keyWordsAndPhrases = keyWordsAndPhrases;
    }

    // === Other methods ===

    /**
     * Get the list of job applications sorted in non-decreasing order based on the grade given.
     *
     * @return the list of job applications sorted in non-decreasing order.
     */
    public ArrayList<JobApplication> getSortedJobApps() {
        this.gradeAllJobApps();

        ArrayList<JobApplication> sortedJobApps = new ArrayList<>();
        this.gradedJobApps.sort(new JobAppComparator());
        for (GradedJobApplication gradedJobApplication : this.gradedJobApps) {
            sortedJobApps.add(gradedJobApplication.jobApp);
        }
        return sortedJobApps;
    }

    /**
     * Grade all job appplications.
     */
    private void gradeAllJobApps() {
        for (JobApplication jobApp : this.jobApps) {
            this.gradedJobApps.add(new GradedJobApplication(jobApp, this.gradeJobApplication(jobApp)));
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
        int grade = 0;
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
                continue;
            }

        }
        return grade;
    }

    private class GradedJobApplication {
        /**
         * A graded job application.
         */

        // === Instance variables ===
        private JobApplication jobApp;  // The job application graded.
        private int grade;  // The grade received.

        // === Constructor ===
        GradedJobApplication(JobApplication jobApp, int grade) {
            this.jobApp = jobApp;
            this.grade = grade;
        }
    }

    private class JobAppComparator implements Comparator<GradedJobApplication> {

        // Note this is used to sort in non-decreasing order.
        @Override
        public int compare(GradedJobApplication jobApp1, GradedJobApplication jobApp2) {
            return jobApp2.grade - jobApp1.grade;
        }
    }


}
