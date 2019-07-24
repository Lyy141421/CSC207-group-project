package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Panel which displays all of the required functionality for an Applicant user
 */
public class ApplicantMain extends JPanel {
    public ApplicantMain(Applicant applicant) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel cards = this.getApplicantCards(applicant);
        ApplicantSideBarMenuPanel sidebar = new ApplicantSideBarMenuPanel(cards, (CardLayout)cards.getLayout());

        c.weightx = 0.05; c.weighty = 0.5;
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
     */
    private JPanel getApplicantCards(Applicant applicant) {
        JPanel ret = new JPanel(new CardLayout());
        ret.add(new ApplicantBrowsePostings(applicant), "BROWSE");
        ret.add(new ApplicantViewProfile(applicant), "PROFILE");
        ret.add(new ApplicantDocuments(applicant), "DOCUMENTS");
        ret.add(new ApplicantSchedule(applicant), "SCHEDULE");
        ret.add(new ApplicantWithdraw(applicant), "WITHDRAW");
        return ret;
    }

    /**
     * using this to test shit
     */
    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.add(new ApplicantMain(new Applicant("a", "a", "a", "a",
                LocalDate.now(), "a")));
        test.setSize(854, 480); test.setVisible(true);
    }
}
