package GUIClasses.HRInterface;

import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.CommonUserGUI.GUIElementsCreator;
import GUIClasses.CommonUserGUI.NotificationsGUI;
import GUIClasses.CommonUserGUI.UserMain;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class HRHome extends JLayeredPane {

    private HRBackend hrBackend;
    private HRMain main;
    private JPanel cards;

    HRHome(HRBackend hrBackend, JPanel parent) {
        this.hrBackend = hrBackend;
        this.cards = parent;
        this.setLayout(null);
        JPanel welcomePanel = this.createWelcomePanel();
        welcomePanel.setBounds(170, 10, 300, 30);
        this.add(welcomePanel, new Integer(0));
        int y = 40;
        for (Object[] titleAndArray : this.createTitleToJobPostingsArray()) {
            JPanel tablePanel = this.createJobPostingsTablePanel((String) titleAndArray[0], (ArrayList<BranchJobPosting>) titleAndArray[1]);
            tablePanel.setBounds(30, y, 610, 125);
            this.add(tablePanel, new Integer(0));
            y += 135;
        }

        NotificationsGUI notifications = new NotificationsGUI(hrBackend.getHR());
        this.add(notifications.getNotificationsButton(), new Integer(1));
        this.add(notifications.getNotificationsPanel(), new Integer(1));

    }

    private ArrayList<Object[]> createTitleToJobPostingsArray() {
        this.main = (HRMain) this.cards.getParent();
        ArrayList<Object[]> titleToJobPostings = new ArrayList<>();
        //TODO: main is null.
        titleToJobPostings.add(new Object[]{"Job Postings To Review", new ArrayList<>(main.getUnreviewedJP().values())});
        titleToJobPostings.add(new Object[]{"Job Postings That Need Group Interviews Scheduled", new ArrayList<>(main.getScheduleJP().values())});
        titleToJobPostings.add(new Object[]{"Job Postings That Need Final Hiring Decision", new ArrayList<>(main.getHiringJP().values())});
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

}
