package Miscellaneous;

import UsersAndJobObjects.JobApplication;

import java.time.LocalDate;
import java.util.Comparator;

public class CloseDateComparator implements Comparator<JobApplication> {
    /**
     * Comparator that compares job applications in terms of job posting close date.
     */

    /**
     * Compare two interviews in terms of date.
     *
     * @param jobApp1 The first job application in question.
     * @param jobApp2 The second job application in question.
     * @return a negative number, zero or a positive number based on if the first interview is before, at the same time
     * or after the second interview, respectively.
     */
    public int compare(JobApplication jobApp1, JobApplication jobApp2) {
        LocalDate date1 = jobApp1.getJobPosting().getCloseDate();
        LocalDate date2 = jobApp2.getJobPosting().getCloseDate();
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isEqual(date2)) {
            return 0;
        } else {
            return 1;
        }
    }

}
