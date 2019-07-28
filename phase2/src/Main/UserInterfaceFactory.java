package Main;

import ApplicantStuff.Applicant;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.InterviewerInterface.InterviewerMain;
import GUIClasses.ReferenceInterface.ReferencePanel;

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
