package GUIClasses.ReferenceInterface;

import ApplicantStuff.Reference;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.FrequentlyUsedMethods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class ReferenceViewRefereeJobPostingsPanel extends JPanel {

    // === Class variables ===
    // The key for the prompt card
    private static String PROMPT = "PROMPT";

    // === Instance variables ===
    // The reference who logged in
    private Reference reference;
    // The split pane
    private JSplitPane splitPane;
    // The list that displays the job applications that need reference letters
    private JList jobPostingList;
    // The panel that displays the job posting information
    private JPanel jobPostingCards = new JPanel(new CardLayout());


    // === Constructor ===
    ReferenceViewRefereeJobPostingsPanel(Reference reference) {
        this.reference = reference;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(new FrequentlyUsedMethods().createTitlePanel(
                "View the Job Postings That Your Referees Have Applied To", 20), BorderLayout.PAGE_START);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setRightComponent(jobPostingCards);
        this.setJobPostingList(splitPane);
        this.setJobPostingCards(splitPane);
        this.setListSelectionListener();
        this.add(splitPane);
    }


    // === Private methods ===

    /**
     * Set the list of referees to choose from
     * @param splitPane The split pane on which this list is going to be shown
     */
    private void setJobPostingList(JSplitPane splitPane) {
        this.jobPostingList = new JList<>();
        this.jobPostingList.setListData(reference.getJobPostingNameList());
        this.jobPostingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobPostingList.setLayoutOrientation(JList.VERTICAL);
        splitPane.setLeftComponent(new JScrollPane(this.jobPostingList));
    }

    /**
     * Set the list selection listener for choosing the referee.
     */
    private void setListSelectionListener() {
        this.jobPostingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jobPostingList.getSelectedIndex() != -1) {
                    int selectedIndex = jobPostingList.getSelectedIndex();
                    ((CardLayout) jobPostingCards.getLayout()).show(jobPostingCards, String.valueOf(selectedIndex));
                } else {
                    ((CardLayout) jobPostingCards.getLayout()).show(jobPostingCards, PROMPT);
                }
            }
        });
    }

    /**
     * Create the prompt card that is defaulted to show on the right side of the split pane.
     *
     * @return the prompt panel created.
     */
    private JPanel createPromptCard() {
        JPanel promptCard = new JPanel();
        promptCard.setLayout(new BorderLayout());
        JLabel promptText = new JLabel("Select a job posting to view");
        promptText.setFont(new Font("Century Gothic", Font.BOLD, 15));
        promptText.setHorizontalAlignment(SwingConstants.CENTER);
        promptCard.add(promptText, BorderLayout.CENTER);
        return promptCard;
    }

    /**
     * Set the job posting cards that are displayed when a referee is chosen from the list.
     *
     * @param splitPane The split pane on which this list is going to be shown.
     */
    private void setJobPostingCards(JSplitPane splitPane) {
        jobPostingCards.add(this.createPromptCard(), PROMPT);
        int index = 0;
        for (BranchJobPosting jobPosting : this.reference.getJobPostings()) {
            jobPostingCards.add(this.createJobPostingPanel(jobPosting), String.valueOf(index));
            index++;
        }
        splitPane.setRightComponent(jobPostingCards);
    }

    /**
     * Creates the panel where the job posting information is displayed.
     *
     * @param jobPosting The job posting being displayed.
     * @return the panel where the job posting information is displayed.
     */
    private JPanel createJobPostingPanel(BranchJobPosting jobPosting) {
        JPanel jobPostingSummaryPanel = new JPanel();
        jobPostingSummaryPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.addJobPostingCategoryLabels(c, jobPostingSummaryPanel);
        this.addJobPostingCategoryValues(c, jobPostingSummaryPanel, jobPosting);
        return jobPostingSummaryPanel;
    }

    /**
     * Adds the job posting category labels to the main job posting panel.
     *
     * @param c         The grid bag constraints for layout purposes.
     * @param mainPanel The panel where these labels are going to end up.
     */
    private void addJobPostingCategoryLabels(GridBagConstraints c, JPanel mainPanel) {
        c.insets = new Insets(0, 0, 20, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(new JLabel("Job Title: "), c);
        c.gridy++;
        mainPanel.add(new JLabel("Job Description:"), c);
        c.gridy++;
        mainPanel.add(new JLabel("Company Branch:"), c);
        c.gridy++;
        mainPanel.add(new JLabel("Close Date:"), c);
    }

    /**
     * Adds the job posting category values to the main job posting panel.
     *
     * @param c          The grid bag constraints for layout purposes.
     * @param mainPanel  The main panel where the job posting information will be displayed.
     * @param jobPosting The job posting being displayed.
     */
    private void addJobPostingCategoryValues(GridBagConstraints c, JPanel mainPanel, BranchJobPosting jobPosting) {
        c.insets = new Insets(0, 30, 20, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        JTextArea titleName = new JTextArea(jobPosting.getTitle());
        titleName.setEditable(false);
        mainPanel.add(titleName, c);
        c.gridy++;
        JTextArea jobDescriptionContent = new JTextArea(jobPosting.getDescription());
        jobDescriptionContent.setEditable(false);
        mainPanel.add(jobDescriptionContent, c);
        c.gridy++;
        JTextArea branchName = new JTextArea(jobPosting.getBranch().getName());
        branchName.setEditable(false);
        mainPanel.add(branchName, c);
        c.gridy++;
        JTextArea referenceCloseDate = new JTextArea(jobPosting.getReferenceCloseDate().toString());
        referenceCloseDate.setEditable(false);
        mainPanel.add(referenceCloseDate, c);
    }

}
