package Main;

import GUIClasses.ActionListeners.LogoutActionListener;

public class UserInterfaceFactory {

    private User user;
    private JobApplicationSystem jobApplicationSystem;
    private LogoutActionListener logoutActionListener;

    UserInterfaceFactory(User user, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        this.user = user;
        this.jobApplicationSystem = jobAppSystem;
        this.logoutActionListener = logoutActionListener;
    }

//    UserPanel createPanel() {
//        if (user instanceof Applicant) {
//            return new ApplicantPanel();
//        } else if (user instanceof HRCoordinator) {
//            return new HRCoordinatorPanel();
//        } else if (user instanceof Interviewer) {
//            return new InterviewerMain();
//        } else {
//            return new ReferencePanel();
//        }
//    }

}
