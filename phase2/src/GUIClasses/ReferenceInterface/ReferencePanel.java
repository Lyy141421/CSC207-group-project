package GUIClasses.ReferenceInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserPanel;
import Main.JobApplicationSystem;
import GUIClasses.CommonUserGUI.UserProfilePanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ReferencePanel extends UserPanel {
    /**
     * The main reference panel.
     */

    // === Class variables ===
    // The keys for the cards
    static final String SUBMIT_REFERENCE_LETTER = "Submit";
    static final String VIEW_REFEREE_JOB_POSTINGS = "View Referee Job Postings";

    // === Instance variables ===
    private ReferenceBackEnd referenceBackEnd;  // The back end of the reference GUI
    private JPanel cards = new JPanel(new CardLayout());    // The cards that are being displayed

    // === Constructor ===
    public ReferencePanel(Reference reference, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        this.referenceBackEnd = new ReferenceBackEnd(reference, jobAppSystem);
        this.setLayout(new GridBagLayout());
        this.setCards();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        this.add(new ReferenceSideBarMenuPanel(cards, logoutActionListener), c);

        c.weightx = 0.01;
        c.gridx++;
        this.add(cards, c);
    }

    /**
     * Add the cards to the card layout panel
     */
    public void setCards() {
        cards.add(new ReferenceHomePanel(this.referenceBackEnd), UserPanel.HOME);
        cards.add(new UserProfilePanel(this.referenceBackEnd.getReference()), UserPanel.PROFILE);
        cards.add(new ReferenceSubmitLetterPanel(this.referenceBackEnd), SUBMIT_REFERENCE_LETTER);
        cards.add(new ReferenceViewRefereeJobPostingsPanel(this.referenceBackEnd), VIEW_REFEREE_JOB_POSTINGS);
    }

    /**
     * Update the cards after a reference letter submission.
     */
    public void refresh() {
        cards.removeAll();
        this.setCards();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reference Home Page");
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 19));
        jobApplicationSystem.setPreviousLoginDate(LocalDate.of(2019, 7, 19));
        Reference reference = jobApplicationSystem.getUserManager().createReference("bob@gmail.com", LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "Toronto");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", LocalDate.now(), "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19), LocalDate.now());
        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.now());
        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("username2", "password", "Legal Name2", "email@gmail.com", LocalDate.of(2019, 7, 19), "L4B4P8");
        BranchJobPosting jobPosting2 = hrc.addJobPosting("title2", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 20), LocalDate.now());
        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, LocalDate.of(2019, 7, 19));
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
        branchJobPostingManager.updateJobPostingsClosedForApplications(LocalDate.now());
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        frame.add(new ReferencePanel(reference, jobApplicationSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
