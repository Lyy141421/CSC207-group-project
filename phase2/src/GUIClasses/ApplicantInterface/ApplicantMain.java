package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.DocumentViewer;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Panel which displays all of the required functionality for an Applicant user
 */
public class ApplicantMain extends JPanel {
    private Applicant applicant;
    private ApplicantPanel masterPanel;
    private ApplicantBackend applicantBackend;
    private JPanel cards;
    private GridBagConstraints c;

    ApplicantMain(Applicant applicant, ApplicantPanel masterPanel,
                  JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        applicantBackend = new ApplicantBackend(applicant, jobAppSystem);
        this.applicant = applicant;
        this.masterPanel = masterPanel;

        setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        this.setApplicantCards();
        ApplicantSideBarMenuPanel sidebar = new ApplicantSideBarMenuPanel(cards, applicantBackend, jobAppSystem, (CardLayout) cards.getLayout(), logout);

        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 0;
        this.add(sidebar, c);

        c.weightx = 0.7;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1; c.gridwidth = 3; c.gridy = 0;
        this.add(cards, c);
    }

    /**
     * Builds the cards that are switched between by the sidebar
     * Shows notifications by default
     */
    private void setApplicantCards() {
        cards = new JPanel(new CardLayout());
        cards.add(new ApplicantHome(applicant), UserMain.HOME);
        cards.add(new ApplicantBrowsePostings(applicantBackend, masterPanel), "POSTINGS");
        cards.add(new UserProfilePanel(applicant), UserMain.PROFILE);
        cards.add(this.createApplicantDocumentViewer(), "DOCUMENTS");
        cards.add(new ApplicantSchedule(applicantBackend), "SCHEDULE");
        cards.add(new ApplicantViewApps(applicantBackend, masterPanel), "MANAGE");
    }

    public void refresh() {
        masterPanel.refresh();
    }

    private JPanel createApplicantDocumentViewer() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new DocumentViewer(applicantBackend.getApplicantFolder()), new GridBagConstraints());
        return panel;
    }

//    public static void main(String[] args) {
//        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 19));
//        Company company = jobApplicationSystem.createCompany("Company");
//        Branch branch = company.createBranch("Branch", "L4B4P8");
//        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", "L4B4P8", LocalDate.now());
//        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
//        hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
//                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19));
//        JFrame frame = new JFrame();
//        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
//        frame.add(new ApplicantPanel(applicant, jobApplicationSystem, logoutActionListener));
//        frame.setSize(854, 480);
//        frame.setResizable(false);
//        frame.setVisible(true);
//    }
}
