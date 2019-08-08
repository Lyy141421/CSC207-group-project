package NotificationSystem;

import ApplicantStuff.JobApplication;
import CompanyStuff.InterviewManager;
import CompanyStuff.JobPostings.BranchJobPosting;

public class NotificationFactory {

    public static final String AUTO_HIRING_EXACT = "Auto-hiring exact";
    public static final String AUTO_HIRING_LESS = "Auto-hiring less";
    public static final String NO_APPS_IN_CONSIDERATION = "No applications in consideration";
    public static final String NO_INTERVIEWERS_IN_FIELD = "No interviewers in field";
    public static final String HIRED = "Hired";
    public static final String ADVANCE_ROUND = "Advance round";
    public static final String WELCOME = "Welcome";

    public NotificationFactory() {
    }

    public Notification createNotification(String key, Object object) {
        switch (key) {
            case AUTO_HIRING_EXACT:
                return new Notification("Auto-Hiring Applicants All Positions Filled",
                        "All applicants in consideration for " + ((BranchJobPosting) object).getTitle() + " have been auto-hired and" +
                                " all positions have been filled");
            case AUTO_HIRING_LESS:
                return new Notification("Auto-Hiring Applicants Some Positions Filled",
                        "All applicants in consideration for " + ((BranchJobPosting) object).getTitle() + " have been auto-hired" +
                                " but not all requested positions have been filled");
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
            case WELCOME:
                return new Notification("Welcome", "Welcome to The GetAJob(tm) Program");
                default:
                return null;
        }
    }
}
