package NewGUI;

import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import JobPostings.BranchJobPosting;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class ReferenceViewRefereeJobPostingsPanel extends JPanel implements ListSelectionListener {

    private Reference reference;
    //  The list that displays the job applications that need reference letters
    private JList jobAppList;
    // The panel that displays the job posting information
    private JPanel jobPostingCards = new JPanel(new CardLayout());

    ReferenceViewRefereeJobPostingsPanel(Reference reference) {
        this.reference = reference;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(this.createJobApplicationListPanel(), BorderLayout.WEST);
        for (BranchJobPosting jobPosting : this.reference.getJobPostingsForReference()) {
            String key = jobPosting.getTitle() + jobPosting.getId();
            jobPostingCards.add(this.createJobPostingPanel(jobPosting), key);
        }
        this.add(this.jobPostingCards, BorderLayout.CENTER);
    }

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

    private JPanel createJobPostingPanel(BranchJobPosting jobPosting) {
        JPanel jobPostingSummaryPanel = new JPanel();
        jobPostingSummaryPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.createJobTitlePanel(c, jobPostingSummaryPanel, jobPosting.getTitle());
        this.createJobDescriptionPanel(c, jobPostingSummaryPanel, jobPosting.getDescription());
        this.createCompanyPanel(c, jobPostingSummaryPanel, jobPosting.getBranch().getName());
        this.createCloseDatePanel(c, jobPostingSummaryPanel, jobPosting.getCloseDate().toString());
        return jobPostingSummaryPanel;
    }

    private void createJobTitlePanel(GridBagConstraints c, JPanel mainPanel, String title) {
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        JLabel jobTitleLabel = new JLabel("Job Title: ");
        mainPanel.add(jobTitleLabel, c);
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel jobTitleName = new JLabel(title);
        jobTitleName.setBackground(Color.WHITE);
        jobTitleName.setOpaque(true);
        mainPanel.add(jobTitleName, c);
    }

    private void createJobDescriptionPanel(GridBagConstraints c, JPanel mainPanel, String description) {
        c.insets = new Insets(20, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        JLabel jobDescriptionLabel = new JLabel("Job Description:");
        mainPanel.add(jobDescriptionLabel, c);
        JTextArea jobDescriptionContent = new JTextArea(description);
        c.insets = new Insets(20, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = jobDescriptionContent.getWidth();
        c.ipady = jobDescriptionContent.getHeight();
        mainPanel.add(jobDescriptionContent, c);
    }

    private void createCompanyPanel(GridBagConstraints c, JPanel mainPanel, String branchName) {
        c.insets = new Insets(20, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 2;
        JLabel companyLabel = new JLabel("Company Branch:");
        mainPanel.add(companyLabel, c);
        c.insets = new Insets(20, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 2;
        JLabel companyNameLabel = new JLabel(branchName);
        companyNameLabel.setBackground(Color.WHITE);
        companyNameLabel.setOpaque(true);
        mainPanel.add(companyNameLabel, c);
    }

    private void createCloseDatePanel(GridBagConstraints c, JPanel mainPanel, String date) {
        c.insets = new Insets(20, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 3;
        JLabel closeDateLabel = new JLabel("Close Date:");
        mainPanel.add(closeDateLabel, c);
        c.insets = new Insets(20, 10, 0, 0);
        c.gridx = 1;
        c.gridy = 3;
        JLabel closeDate = new JLabel(date);
        closeDate.setBackground(Color.WHITE);
        closeDate.setOpaque(true);
        mainPanel.add(closeDate, c);
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
