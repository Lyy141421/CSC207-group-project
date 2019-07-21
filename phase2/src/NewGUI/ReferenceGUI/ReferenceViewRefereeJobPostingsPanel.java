package NewGUI.ReferenceGUI;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.JobPostings.BranchJobPosting;
import NewGUI.FrequentlyUsedMethods;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class ReferenceViewRefereeJobPostingsPanel extends JPanel implements ListSelectionListener {

    // === Instance variables ===
    // The reference who logged in
    private Reference reference;
    // The list that displays the job applications that need reference letters
    private JList jobAppList;
    // The panel that displays the job posting information
    private JPanel jobPostingCards = new JPanel(new CardLayout());

    // === Constructor ===
    ReferenceViewRefereeJobPostingsPanel(Reference reference) {
        this.reference = reference;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(new FrequentlyUsedMethods().createTitlePanel(
                "View the Job Postings That Your Referees Have Applied To", 20), BorderLayout.PAGE_START);
        this.add(this.createJobApplicationListPanel(), BorderLayout.WEST);
        for (BranchJobPosting jobPosting : this.reference.getJobPostingsForReference()) {
            String key = jobPosting.getTitle() + jobPosting.getId();
            jobPostingCards.add(this.createJobPostingPanel(jobPosting), key);
        }
        this.add(this.jobPostingCards, BorderLayout.CENTER);
    }


    // === Private methods ===

    /**
     * Create a list panel with all the job applications that this reference needs to write a reference for.
     *
     * @return the panel with this list.
     */
    private JPanel createJobApplicationListPanel() {
        JPanel jobAppListPanel = new JPanel();
        jobAppListPanel.setLayout(new BorderLayout());
        JLabel listTitle = new JLabel("Select a referee:");
        jobAppListPanel.add(listTitle, BorderLayout.PAGE_START);
        DefaultListModel listModel = new DefaultListModel();
        for (JobApplication jobApp : reference.getJobAppsForReference()) {
            listModel.addElement(jobApp.getApplicant().getLegalName());
        }
        jobAppList = new JList(listModel);
        jobAppList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobAppList.setSelectedIndex(0);
        jobAppList.addListSelectionListener(this);
        jobAppList.setVisibleRowCount(5);
        jobAppList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane listScrollPane = new JScrollPane(jobAppList);
        jobAppListPanel.add(listScrollPane, BorderLayout.CENTER);
        return jobAppListPanel;
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
        mainPanel.add(new JTextArea(jobPosting.getTitle()), c);
        c.gridy++;
        JTextArea jobDescriptionContent = new JTextArea(jobPosting.getDescription());
        mainPanel.add(jobDescriptionContent, c);
        c.gridy++;
        mainPanel.add(new JTextArea(jobPosting.getBranch().getName()), c);
        c.gridy++;
        mainPanel.add(new JTextArea(jobPosting.getCloseDate().toString()), c);
    }

    // For the list selection listener
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (jobAppList.getSelectedIndex() != -1) {
                int selectedIndex = jobAppList.getSelectedIndex();
                ArrayList<BranchJobPosting> refereeJobPostings = this.reference.getJobPostingsForReference();
                BranchJobPosting jobPostingToDisplay = refereeJobPostings.get(selectedIndex);
                String key = jobPostingToDisplay.getTitle() + jobPostingToDisplay.getId();
                ((CardLayout) jobPostingCards.getLayout()).show(jobPostingCards, key);
            }
        }
    }

}
