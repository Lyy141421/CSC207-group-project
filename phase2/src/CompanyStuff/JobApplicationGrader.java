package CompanyStuff;

import ApplicantStuff.JobApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class JobApplicationGrader {

    // === Instance variable ===
    private JobApplication jobApplication;
    private ArrayList<String> keyWordsAndPhrases;
    private int grade;

    // === Constructor ===
    JobApplicationGrader(JobApplication jobApplication, ArrayList<String> keyWordsAndPhrases) {
        this.jobApplication = jobApplication;
        this.keyWordsAndPhrases = keyWordsAndPhrases;
    }

    // === Getter ===

    int getGrade() {
        return this.grade;
    }

    // === Other methods ===

    /**
     * Grade this job application based on the number of occurrences of keyWordsAndPhrases in the files submitted with
     * this application.
     */
    private void gradeFiles() {
        Branch branch = this.jobApplication.getJobPosting().getBranch();
        File[] filesSubmitted = branch.getDocumentManager().getFilesForJobApplication(this.jobApplication);
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
    }

}
