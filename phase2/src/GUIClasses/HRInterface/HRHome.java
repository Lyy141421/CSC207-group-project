package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.CommonUserGUI.UserPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class HRHome extends HRPanel {

    // TODO add a scroll bar on the right side

    HRHome(HRBackend hrBackend) {
        super(hrBackend);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.createWelcomePanel(), c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridy = 1;
        c.gridheight = 75;
        for (Object[] titleAndArray : this.createTitleToJobPostingsArray()) {
            this.add(this.createJobPostingsTablePanel((String) titleAndArray[0], (ArrayList<BranchJobPosting>) titleAndArray[1]), c);
            c.gridy += c.gridheight;
        }
    }

    private ArrayList<Object[]> createTitleToJobPostingsArray() {
        ArrayList<Object[]> titleToJobPostings = new ArrayList<>();
        titleToJobPostings.add(new Object[]{"Job Postings To Review", this.hrBackend.getJPToReview()});
        titleToJobPostings.add(new Object[]{"Job Postings That Need Group Interviews Scheduled", this.hrBackend.getJPToSchedule()});
        titleToJobPostings.add(new Object[]{"Job Postings That Need Final Hiring Decision", this.hrBackend.getJPToHire()});
        return titleToJobPostings;
    }

    /**
     * Create a welcome panel to be displayed.
     *
     * @return the welcome panel created.
     */
    private JPanel createWelcomePanel() {
        JPanel welcomeMessage = new GUIElementsCreator().createLabelPanel("Welcome " +
                this.hrBackend.getHR().getLegalName(), 20, true);
        return welcomeMessage;
    }

    /**
     * Create the full panel that contains information for jobPostings to schedule.
     *
     * @return the panel created.
     */
    private JPanel createJobPostingsTablePanel(String tableTitle, ArrayList<BranchJobPosting> jobPostings) {
        JPanel applicantSelection = new JPanel();
        applicantSelection.setLayout(new BorderLayout());
        applicantSelection.add(new GUIElementsCreator().createLabelPanel(
                tableTitle, 15, true), BorderLayout.BEFORE_FIRST_LINE);
        applicantSelection.add(this.createJobPostingsTable(jobPostings), BorderLayout.CENTER);
        return applicantSelection;
    }

    private JPanel createJobPostingsTable(ArrayList<BranchJobPosting> jobPostings) {
        Object[][] data = new Object[jobPostings.size()][];

        for (int i = 0; i < jobPostings.size(); i++) {
            data[i] = jobPostings.get(i).getCategoryValuesForHR();
        }

        return new GUIElementsCreator().createTablePanel(BranchJobPosting.CATEGORY_LABELS_FOR_HR, data);
    }

    /**
     * Refreshes all the cards in InterviewerMain when any changes are made.
     */
    void refresh() {
        ((UserPanel) this.getParent().getParent()).refresh();
    }

}
