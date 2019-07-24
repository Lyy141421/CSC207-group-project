package GUIClasses.InterviewerInterface.BackUpCopies;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.InterviewerInterface.*;
import GUIClasses.MethodsTheGUICallsInInterviewer;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class InterviewerMainCopy extends JPanel {

    Container contentPane;
    MethodsTheGUICallsInInterviewer interviewerInterface;
    LocalDate today;

    Container mainPanel = this;
    CardLayout cardLayout = new CardLayout();

    InterviewerHomePanel homePanel;
    InterviewerView viewIncompletePanel;
    InterviewerViewComplete viewCompletePanel;
    InterviewerCoordinatorView coordinatorViewPanel;
//    InterviewerSchedule schedulePanel;

    private InterviewerMainCopy(Container contentPane, MethodsTheGUICallsInInterviewer interviewerInterface, LocalDate today) {
        this.setLayout(this.cardLayout);

        this.contentPane = contentPane;
        this.interviewerInterface = interviewerInterface;
        this.today = today;

        this.addPanels();
        this.setActions();
    }

    private void addPanels() {
        this.add(this.homePanel = new InterviewerHomePanel(this, this.interviewerInterface, this.today), InterviewerPanel.HOME);
        this.add(this.viewIncompletePanel = new InterviewerView(this, this.interviewerInterface, this.today), InterviewerPanel.INCOMPLETE);
        //TODO: might want to combine the following two since they differ in one function only.
        this.add(this.viewCompletePanel = new InterviewerViewComplete(this, this.interviewerInterface, this.today), InterviewerPanel.COMPLETE);
        this.add(this.coordinatorViewPanel = new InterviewerCoordinatorView(this, this.interviewerInterface, this.today), InterviewerPanel.COORDINATOR);
//        this.add(this.schedulePanel = new InterviewerSchedule(this, this.interviewerInterface, this.today), InterviewerPanel.SCHEDULE);
    }

    private void setActions() {
        this.setViewCompleteAction();
        this.setViewIncompleteAction();
        this.setViewCoordinatorAction();
//        this.setScheduleAction();
        this.setLogoutAction();
    }

    private void setViewCompleteAction() {
        this.homePanel.getViewCompleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCompletePanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.COMPLETE);
            }
        });
    }

    private void setViewIncompleteAction() {
        this.homePanel.getViewIncompleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewIncompletePanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.INCOMPLETE);
            }
        });
    }

    private void setViewCoordinatorAction() {
        this.homePanel.getViewCoordinatorButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coordinatorViewPanel.reload();
                cardLayout.show(mainPanel, InterviewerPanel.COORDINATOR);
            }
        });
    }

//    private void setScheduleAction() {
//        this.homePanel.getScheduleButton().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                schedulePanel.reload();
//                cardLayout.show(mainPanel, InterviewerPanel.SCHEDULE);
//            }
//        });
//    }

//    private void setLogoutAction() {
//        this.homePanel.getLogoutButton().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //TODO: save data
//                // TODO menu bar -- action listener external
//                ((CardLayout) contentPane.getLayout()).show(contentPane, "LOGIN");
//            }
//        });
//    }

    public static void main(String[] args) {
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "L4B3Z9");
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer);
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();

        JFrame frame = new JFrame();
        frame.add(new InterviewerMainCopy(frame.getContentPane(), new MethodsTheGUICallsInInterviewer(interviewer), LocalDate.now()));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
