package GUIClasses.InterviewerInterface;

import CompanyStuff.Branch;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewerMain extends UserMain {
    /**
     * The main panel for Interviewer GUI.
     */

    // === Class variables ===
    // Keys for the cards in the card layout
    static String SCHEDULE = "SCHEDULE";
    static String INCOMPLETE = "INCOMPLETE";
    static String COMPLETE = "COMPLETE";

    // === Instance variables ===
    private InterviewerBackEnd interviewerBackEnd;   // The backend class for the Interviewer GUI
    private JPanel cards = new JPanel(new CardLayout());        // The panel that contains all the Interviewer cards

    // === Constructor ===
    public InterviewerMain(Interviewer interviewer, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        super(jobAppSystem);
        this.interviewerBackEnd = new InterviewerBackEnd(jobAppSystem, interviewer);
        this.setLayout(new GridBagLayout());
        this.setCards();

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.75;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new InterviewerSideBarMenuPanel(cards, logoutActionListener), c);

        c.weightx = 1;
        c.gridx = 1;
        c.gridwidth = 3;
        this.add(cards, c);
    }

    /**
     * Sets the cards for the Interviewer GUI.
     */
    public void setCards() {
        cards.add(new InterviewerHomePanel(this.interviewerBackEnd), InterviewerMain.HOME);
        cards.add(new UserProfilePanel(this.interviewerBackEnd.getInterviewer()), InterviewerMain.PROFILE);
        cards.add(new InterviewerSchedule(this.interviewerBackEnd), InterviewerMain.SCHEDULE);
        cards.add(new InterviewerViewOnly(this.interviewerBackEnd), InterviewerMain.INCOMPLETE);
        cards.add(new InterviewerCompleteInterviewFull(this.interviewerBackEnd), InterviewerMain.COMPLETE);
    }

    public void refresh() {
        super.refresh();
        cards.removeAll();
        this.setCards();
    }

//    public static void main(String[] args) {
        // Must run the main method in reference panel first to instantiate the job posting, applicant and application
//        // For completing the interview
//        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        Branch branch = jobApplicationSystem.getCompanies().get(0).getBranches().get(0);
//        Interviewer interviewer = branch.getFieldToInterviewers().get("field").get(0);
//        jobApplicationSystem.setToday(LocalDate.of(2019, 8, 24));
//        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
//        JFrame frame = new JFrame();
//        frame.add(new InterviewerMain(interviewer, jobApplicationSystem, logoutActionListener));
//        frame.setSize(854, 480);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // For scheduling the interview
//        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        Branch branch = jobApplicationSystem.getCompanies().get(0).getBranches().get(0);
//        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
//        Interviewer interviewer = new Interviewer("Interviewer", "password", "Bobby", "email", branch, "field", LocalDate.of(2019, 7, 10));
//        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
//        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
//        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
//        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
//        jobPosting.getInterviewManager().rejectApplicationsForFirstRound(new ArrayList<>());
//        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
//        jobPosting2.getInterviewManager().rejectApplicationsForFirstRound(new ArrayList<>());
//        ArrayList<String[]> interviewConfiguration2 = new ArrayList<>();
//        interviewConfiguration2.add(new String[]{Interview.GROUP, "In-person"});
//        jobPosting2.getInterviewManager().setInterviewConfiguration(interviewConfiguration2);
//        branch.getJobPostingManager().updateAllClosedUnfilledJobPostings(LocalDate.of(2019, 8, 12));
//        jobApplicationSystem.setToday(LocalDate.of(2019, 8, 20));
//        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
//        JFrame frame = new JFrame();
//        frame.add(new InterviewerMain(interviewer, jobApplicationSystem, logoutActionListener));
//        frame.setSize(854, 480);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}