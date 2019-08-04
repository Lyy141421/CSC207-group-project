package GUIClasses.ApplicantInterface;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.DocumentViewer;
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
class ApplicantMain extends JPanel {
    private Applicant applicant;
    private ApplicantPanel masterPanel;
    private JobApplicationSystem jobAppSystem;

    ApplicantMain(Applicant applicant, ApplicantPanel masterPanel,
                  JobApplicationSystem jobAppSystem, LogoutActionListener logout) {
        this.applicant = applicant;
        this.masterPanel = masterPanel;
        this.jobAppSystem = jobAppSystem;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel cards = this.getApplicantCards();
        ApplicantSideBarMenuPanel sidebar = new ApplicantSideBarMenuPanel(cards,
                (CardLayout)cards.getLayout(), logout);

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
    private JPanel getApplicantCards() {
        JPanel ret = new JPanel(new CardLayout());
        ret.add(new ApplicantHome(applicant), "HOME");
        ret.add(new ApplicantBrowsePostings(applicant, masterPanel, jobAppSystem), "POSTINGS");
        ret.add(new UserProfilePanel(applicant), "PROFILE");
        ret.add(new DocumentViewer(applicant.getDocumentManager().getFolder()), "DOCUMENTS");
        ret.add(new ApplicantSchedule(applicant, jobAppSystem), "SCHEDULE");
        ret.add(new ApplicantViewApps(applicant), "MANAGE");
        return ret;
    }

    public static void main(String[] args) {
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "L4B4P8");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", "L4B4P8", LocalDate.now());
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 25));
        JFrame frame = new JFrame();
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        frame.add(new ApplicantPanel(applicant, jobApplicationSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
