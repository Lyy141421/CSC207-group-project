package NewGUI.ReferenceGUI;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import JobPostings.BranchJobPosting;
import JobPostings.BranchJobPostingManager;
import NewGUI.FrequentlyUsedMethods;
import NewGUI.UserProfilePanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ReferencePanel extends JPanel {

    // === Instance variables ===
    private Reference reference;
    private JPanel cards = new JPanel(new CardLayout());
    public static final String HOME = "Home";
    public static final String PROFILE = "Profile";
    static final String SUBMIT_REFERENCE_LETTER = "Submit";
    static final String VIEW_REFEREE_JOB_POSTINGS = "View Referee Job Postings";
    public static final String SUCCESSFUL_SUBMISSION = "Successful Submission";

    // === Constructor ===
    ReferencePanel(Reference reference) {
        this.setLayout(new BorderLayout());
        this.add(new ReferenceSideBarMenuPanel(reference), BorderLayout.WEST);
        this.reference = reference;
        cards.add(new ReferenceHomePanel(this.reference), HOME);
        cards.add(new UserProfilePanel(this.reference), PROFILE);
        cards.add(new ReferenceSubmitLetterPanel(this.reference), SUBMIT_REFERENCE_LETTER);
        cards.add(new ReferenceViewRefereeJobPostingsPanel(this.reference), VIEW_REFEREE_JOB_POSTINGS);
        cards.add(this.successfulSubmissionPanel(), SUCCESSFUL_SUBMISSION);
        this.add(cards, BorderLayout.CENTER);
    }

    private JPanel successfulSubmissionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel message = new FrequentlyUsedMethods().createTitlePanel("Submission Successful!", 40);
        message.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
        panel.add(message, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Update the cards after a reference letter submission.
     */
    public void updateCards() {
        cards.removeAll();
        cards.add(new ReferenceHomePanel(this.reference), HOME);
        cards.add(new UserProfilePanel(this.reference), PROFILE);
        cards.add(new ReferenceSubmitLetterPanel(this.reference), SUBMIT_REFERENCE_LETTER);
        cards.add(new ReferenceViewRefereeJobPostingsPanel(this.reference), VIEW_REFEREE_JOB_POSTINGS);
        cards.add(this.successfulSubmissionPanel(), SUCCESSFUL_SUBMISSION);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reference Home Page");
        Reference reference = new Reference("bob@gmail.com", LocalDate.now());
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9");
        branch.setCompany(company);
        company.addBranch(branch);
        Applicant applicant = new Applicant("username", "password", "Legal Name", "email@gmail.com", LocalDate.now(), "L4B4P8");
        BranchJobPosting jobPosting = new BranchJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19));
        JobApplication jobApp = new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 19));
        Applicant applicant2 = new Applicant("username2", "password", "Legal Name2", "email@gmail.com", LocalDate.now(), "L4B4P8");
        BranchJobPosting jobPosting2 = new BranchJobPosting("title2", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.now(), LocalDate.now());
        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 19));
        jobPosting.addJobApplication(jobApp);
        jobPosting2.addJobApplication(jobApp2);
        jobPosting.addObserver(company.getDocumentManager());
        jobPosting2.addObserver(company.getDocumentManager());
        reference.addJobApplicationForReference(jobApp);
        reference.addJobApplicationForReference(jobApp2);
        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
        branchJobPostingManager.notifyJobPostingClosed(LocalDate.now());
        frame.add(new ReferencePanel(reference));
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
