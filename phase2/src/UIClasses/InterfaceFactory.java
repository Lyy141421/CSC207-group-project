package UIClasses;

import CompanyStuff.HRCoordinator;
import Main.JobApplicationSystem;
import Main.User;

class InterfaceFactory {
    /**
     * Factory that creates the appropriate interface for the user.
     */

    /**
     * Create the appropriate interface for this user.
     *
     * @param JAS The job application system being used
     * @param user  The user who logged in
     * @return the appropriate interface for this user.
     */
    UserInterface createInterface(JobApplicationSystem JAS, User user) {
        if (user instanceof Applicant) {
            return new ApplicantInterface(JAS, user);
        } else if (user instanceof Interviewer) {
            return new InterviewerInterface(JAS, user);
        } else if (user instanceof HRCoordinator) {
            return new HRCoordinatorInterface(JAS, user);
        }
        return null;
    }
}
