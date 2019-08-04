package GUIClasses.HRInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class HRMain extends UserMain {

    static final int NUM_CARDS = 7;
    HRBackend hrBackend;
    JPanel cards = new JPanel(new CardLayout());

    public HRMain(HRCoordinator hrCoordinator, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        this.hrBackend = new HRBackend(jobAppSystem, hrCoordinator);
        this.setLayout(new GridBagLayout());
        this.setCards();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.15;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        this.add(new HRSideBarMenuPanel(cards, logoutActionListener), c);

        c.gridx++;
        this.add(cards, c);
    }

    //=====Add component methods=====

    public void setCards() {
        cards.add(new HRHome(this.hrBackend), UserMain.HOME, 0);
        cards.add(new UserProfilePanel(this.hrBackend.getHR()), UserMain.PROFILE, 1);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.HIGH_PRIORITY), HRPanel.HIGH_PRIORITY_POSTINGS, 2);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.ALL), HRPanel.BROWSE_POSTINGS, 3);
        cards.add(new HRSearchApplicant(this.hrBackend, cards), HRPanel.SEARCH_APPLICANT, 4);
        cards.add(new HRAddOrUpdatePostingForm(this.hrBackend, true, null), HRPanel.ADD_POSTING, 5);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.UPDATABLE), HRPanel.UPDATE_POSTING, 6);
        //cards.add(new HRViewApp(this, this.hrBackend
        //, this.today, new HashMap<>()), HRPanel.APPLICATION);
    }

    public void refresh() {
        cards.removeAll();
        this.setCards();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HR Main");
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        //new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 28));
        jobApplicationSystem.setPreviousLoginDate(LocalDate.of(2019, 7, 19));
        Reference reference = jobApplicationSystem.getUserManager().createReference("bob@gmail.com", LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "L4B4P8");
        Branch branch2 = company.createBranch("Branch2", "L4B4P8");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", LocalDate.now(), "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        HRCoordinator hrc2 = new HRCoordinator("HR2", "password", "HRC", "email@gmail.com", branch2, LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(Arrays.asList("Tag1", "Tag2")), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 25));
        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 19));
        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("username2", "password", "Legal Name2", "email@gmail.com", LocalDate.of(2019, 7, 19), "L4B4P8");
        BranchJobPosting jobPosting2 = hrc2.addJobPosting("title2", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 26));
        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, LocalDate.of(2019, 7, 19));
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
        branchJobPostingManager.updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 21));
        branchJobPostingManager.updateJobPostingsClosedForReferences(LocalDate.of(2019, 7, 27));
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        frame.add(new HRMain(hrc, jobApplicationSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Minimal steps to run this chunk of code.
        // 1. delete all the files (.ser and .txt in the files directory)
        // 2. run the reference main and clicking log out
        // 3. run interview main and log out
        // 4. Come back here to HRMain and run the main program
        // 5. At this point, you can schedule a group interview
        /*JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        Branch branch = jobApplicationSystem.getCompanies().get(0).getBranches().get(0);
        branch.getJobPostingManager().updateAllClosedUnfilledJobPostings(LocalDate.of(2019, 8, 25));
        HRCoordinator hrc = branch.getHrCoordinators().get(0);
        jobApplicationSystem.setToday(LocalDate.of(2019, 8, 25));
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        JFrame frame = new JFrame();
        frame.add(new HRMain(hrc, jobApplicationSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
    }
}
