package GUIClasses.InterviewerInterface;

import CompanyStuff.Branch;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewerMain extends UserPanel {

    // === Class variables ===
    static String ADD_NOTES = "ADD_NOTES";
    static String INCOMPLETE = "INCOMPLETE";
    static String COORDINATOR = "COORDINATOR";

    MethodsTheGUICallsInInterviewer interviewerInterface;

    CardLayout cardLayout = new CardLayout();
    JPanel cards = new JPanel();

    private InterviewerMain(MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today,
                            LogoutActionListener logoutActionListener) {
        this.interviewerInterface = interviewerInterface;

        this.setLayout(new BorderLayout());
        this.add(new InterviewerSideBarMenuPanel(logoutActionListener), BorderLayout.WEST);
        this.cards.setLayout(this.cardLayout);

        this.addCards();
    }

    private void addCards() {
        cards.add(new InterviewerHomePanel(this.interviewerInterface), InterviewerMain.HOME);
        cards.add(new UserProfilePanel(this.interviewerInterface.getInterviewer()), InterviewerMain.PROFILE);
        cards.add(new InterviewerView(this.interviewerInterface), InterviewerMain.INCOMPLETE);
        cards.add(new InterviewerViewComplete(this.interviewerInterface), InterviewerMain.ADD_NOTES);
        cards.add(new InterviewerCoordinatorView(this.interviewerInterface), InterviewerMain.COORDINATOR);
        this.add(cards, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Must run the main method in reference panel first to instantiate the job posting, applicant and application

        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        Branch branch = jobApplicationSystem.getCompanies().get(0).getBranches().get(0);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Bobby", "email", branch, "field", LocalDate.of(2019, 7, 10));
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews(LocalDate.of(2019, 8, 11));
        jobApplicationSystem.setToday(LocalDate.of(2019, 8, 20));
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        JFrame frame = new JFrame();
        frame.add(new InterviewerMain(new MethodsTheGUICallsInInterviewer(jobApplicationSystem, interviewer), LocalDate.of(2019, 8, 13), logoutActionListener));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
