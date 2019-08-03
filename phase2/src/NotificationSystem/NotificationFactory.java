package NotificationSystem;

import ApplicantStuff.JobApplication;
import CompanyStuff.InterviewManager;
import CompanyStuff.JobPostings.BranchJobPosting;

public class NotificationFactory {

    public static final String AUTO_HIRING = "Auto-hiring";
    public static final String NO_APPS_IN_CONSIDERATION = "No applications in consideration";
    public static final String NO_INTERVIEWERS_IN_FIELD = "No interviewers in field";
    public static final String HIRED = "Hired";
    public static final String ADVANCE_ROUND = "Advance round";

    public NotificationFactory() {
    }

    public Notification createNotification(String key, Object object) {
        switch (key) {
            case AUTO_HIRING:
                return new Notification("Auto-Hiring All Applicants",
                        "All applicants in " + ((BranchJobPosting) object).getTitle() + " have been auto-hired");
            case NO_APPS_IN_CONSIDERATION:
                BranchJobPosting jobPosting = (BranchJobPosting) object;
                return new Notification("Warning: No Applications in Consideration",
                        "There are no applications in consideration for the " + jobPosting.getTitle()
                                + " job posting (id " + jobPosting.getId() + "). It has been automatically" +
                                " set to filled with zero positions.");
            case NO_INTERVIEWERS_IN_FIELD:
                InterviewManager interviewManager = (InterviewManager) object;
                return new Notification("No Interviewers For Field", "There are no interviewers" +
                        "at your branch for this field. Round " + interviewManager.getCurrentRound() +
                        " of interviews cannot be set for " + interviewManager.getBranchJobPosting().getTitle() + ".");
            case HIRED:
                return new Notification("You've Been Hired!", "You have been hired at " +
                        ((JobApplication) object).getJobPosting().getBranch().getName() + ", make sure to check your email.");
            case ADVANCE_ROUND:
                return new Notification("Advance to Next Round",
                        "You have advanced to the next round in " + ((BranchJobPosting) object).getTitle());
            default:
                return null;
        }
    }
}
