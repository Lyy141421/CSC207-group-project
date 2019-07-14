package UIClasses;

import Main.JobApplicationSystem;
import Main.User;

class InterfaceFactory {
    /**
     * Factory that creates the appropriate interface for the user.
     */

    // === Class variables ===
    // The name of the applicant class
    private static final String APPLICANT_CLASS_NAME = "Users.Applicant";
    // The name of the interviewer class
    private static final String INTERVIEWER_CLASS_NAME = "UsersAndJobObjects.Interviewer";
    // The name of the HR coordinator class
    private static final String HRCOORDINATOR_CLASS_NAME = "Users.HRCoordinator";

    /**
     * Create the appropriate interface for this user.
     *
     * @param JAS The job application system being used
     * @param user  The user who logged in
     * @return the appropriate interface for this user.
     */
    UserInterface createInterface(JobApplicationSystem JAS, User user) {
        switch (user.getClass().getName()) {
            case InterfaceFactory.APPLICANT_CLASS_NAME:
                return new ApplicantInterface(JAS, user);
            case InterfaceFactory.INTERVIEWER_CLASS_NAME:
                return new InterviewerInterface(JAS, user);
            case InterfaceFactory.HRCOORDINATOR_CLASS_NAME:
                return new HRCoordinatorInterface(JAS, user);
        }
        return null;
    }
}
