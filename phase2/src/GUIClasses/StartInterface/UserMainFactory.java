package GUIClasses.StartInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.Reference;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ApplicantInterface.ApplicantPanel;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.HRInterface.HRMain;
import GUIClasses.InterviewerInterface.InterviewerMain;
import GUIClasses.ReferenceInterface.ReferenceMain;
import Main.JobApplicationSystem;
import Main.User;

class UserMainFactory {

    private User user;
    private JobApplicationSystem jobApplicationSystem;
    private LogoutActionListener logoutActionListener;

    UserMainFactory(User user, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        this.user = user;
        this.jobApplicationSystem = jobAppSystem;
        this.logoutActionListener = logoutActionListener;
    }

    UserMain createPanel() {
        if (user instanceof Applicant) {
            return new ApplicantPanel((Applicant) user, jobApplicationSystem, logoutActionListener);
        } else if (user instanceof HRCoordinator) {
            return new HRMain((HRCoordinator) user, jobApplicationSystem, logoutActionListener);
        } else if (user instanceof Interviewer) {
            return new InterviewerMain((Interviewer) user, jobApplicationSystem, logoutActionListener);
        } else {
            return new ReferenceMain((Reference) user, jobApplicationSystem, logoutActionListener);
        }
    }

}
