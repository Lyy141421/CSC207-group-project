package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import GUIClasses.CommonUserGUI.UserProfilePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which displays all of the required functionality for an Applicant user
 */
class ApplicantMain extends JPanel {
    private Applicant applicant;
    private ApplicantPanel masterPanel;

    ApplicantMain(Applicant applicant, ApplicantPanel masterPanel) {
        this.applicant = applicant;
        this.masterPanel = masterPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel cards = this.getApplicantCards();
        ApplicantSideBarMenuPanel sidebar = new ApplicantSideBarMenuPanel(cards, (CardLayout)cards.getLayout());

        c.weightx = 0.01; c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 0;
        this.add(sidebar, c);

        c.weightx = 1; c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1; c.gridwidth = 3; c.gridy = 0;
        this.add(cards, c);
    }

    /**
     * Builds the cards that are switched between by the sidebar
     * Shows notifications by default
     */
    private JPanel getApplicantCards() {
        JPanel ret = new JPanel(new CardLayout());
        ret.add(new ApplicantHome(applicant), "HOME");
        ret.add(new ApplicantBrowsePostings(applicant, masterPanel), "POSTINGS");
        ret.add(new UserProfilePanel(applicant), "PROFILE");
//        ret.add(new TODO, "DOCUMENTS"); fixing rn
//        ret.add(new TODO ApplicantSchedule(), "SCHEDULE"); MAKE
        ret.add(new ApplicantViewApps(applicant), "MANAGE");
        return ret;
    }
}
