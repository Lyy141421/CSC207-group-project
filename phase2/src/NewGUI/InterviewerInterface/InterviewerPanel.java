package NewGUI.InterviewerInterface;

import CompanyStuff.Interviewer;
import GUIClasses.InterviewerInterface.InterviewerHomePanel;
import GUIClasses.InterviewerInterface.InterviewerSideBarMenuPanel;
import Main.JobApplicationSystem;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;
import GUIClasses.CommonUserGUI.UserProfilePanel;

import javax.swing.*;
import java.awt.*;

public class InterviewerPanel extends JPanel {

    // === Instance variables ===
    private Interviewer interviewer;
    private JobApplicationSystem jobApplicationSystem;
    private JPanel cards = new JPanel(new CardLayout());
    static final String HOME = "Home";
    static final String PROFILE = "Profile";
    static final String SCHEDULE_INTERVIEWS = "Schedule Interviews";
    static final String COMPLETE_INTERVIEWS = "Complete Interviews";
    static final String VIEW_INTERVIEWEES = "View Interviewees";

    // === Constructor ===
    InterviewerPanel(Interviewer interviewer, JobApplicationSystem jobApplicationSystem) {
        this.interviewer = interviewer;
        this.jobApplicationSystem = jobApplicationSystem;
        this.setLayout(new BorderLayout());
        this.add(new InterviewerSideBarMenuPanel(), BorderLayout.WEST);
        this.addCards();
    }

    /**
     * Add the cards to the card layout panel
     */
    private void addCards() {
        cards.add(new InterviewerHomePanel(this.interviewer, jobApplicationSystem), HOME);
        cards.add(new UserProfilePanel(this.interviewer), PROFILE);
//        cards.add(new InterviewerScheduleInterviewsPanel(this.interviewer), SCHEDULE_INTERVIEWS);
//        cards.add(new InterviewerCompleteInterviewsPanel(this.interviewer), COMPLETE_INTERVIEWS);
//        cards.add(new InterviewerViewIntervieweesPanel(this.interviewer), VIEW_INTERVIEWEES);
//        cards.add(this.successfulSubmissionPanel(), SUCCESSFUL_SUBMISSION);
        this.add(cards, BorderLayout.CENTER);
    }


    /**
     * Create a panel that shows a file has been successfully submitted.
     *
     * @return a panel with a "submission successful" message.
     */
    private JPanel successfulSubmissionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel message = new FrequentlyUsedMethods().createTitlePanel("Submission Successful!", 40);
        message.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
        panel.add(message, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Update the cards after a interviewer letter submission.
     */
    public void updateCards() {
        cards.removeAll();
        this.addCards();
    }
}
