package GUIClasses.HRInterface;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import FileLoadingAndStoring.DataLoaderAndStorer;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.ApplicantInterface.ApplicantMain;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HRMain extends UserMain {

    static final int NUM_CARDS = 7;
    HRBackend hrBackend;
    private JPanel cards;

    private HashMap<String, BranchJobPosting> unreviewedJP;
    private HashMap<String, BranchJobPosting> scheduleJP;
    private HashMap<String, BranchJobPosting> hiringJP;
    private HashMap<String, BranchJobPosting> archivedJP;
    private HashMap<String, BranchJobPosting> importantJP = new HashMap<>();
    private HashMap<String, BranchJobPosting> updatableJPs;
    private HashMap<String, BranchJobPosting> allJP;

    public HRMain(HRCoordinator hrCoordinator, JobApplicationSystem jobAppSystem, LogoutActionListener logoutActionListener) {
        super(jobAppSystem);
        this.hrBackend = new HRBackend(jobAppSystem, hrCoordinator);
        this.setLayout(new GridBagLayout());
        this.cards = new JPanel(new CardLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.weightx = 1;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        this.add(cards, c);

        this.setJPLists();
        this.setCards();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.7;

        this.add(new HRSideBarMenuPanel(cards, logoutActionListener), c);
    }

    //=====Add component methods=====

    public void setCards() {
        cards.add(new HRHome(this.hrBackend, cards), UserMain.HOME, 0);
        cards.add(new UserProfilePanel(this.hrBackend.getHR()), UserMain.PROFILE, 1);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.HIGH_PRIORITY), HRPanel.HIGH_PRIORITY_POSTINGS, 2);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.ALL), HRPanel.BROWSE_POSTINGS, 3);
        cards.add(new HRSearchApplicant(this.hrBackend, cards), HRPanel.SEARCH_APPLICANT, 4);
        cards.add(new HRAddOrUpdatePostingForm(this.hrBackend, true, null), HRPanel.ADD_POSTING, 5);
        cards.add(new HRViewPosting(this.hrBackend, cards, HRViewPosting.UPDATABLE), HRPanel.UPDATE_POSTING, 6);
    }

    public void refresh() {
        super.refresh();
        cards.removeAll();
        this.setJPLists();
        setCards();
        this.revalidate();
        this.repaint();
    }

    private void setJPLists() {
        this.unreviewedJP = this.getTitleToJPMap(hrBackend.getJPToReview());
        this.scheduleJP = this.getTitleToJPMap(hrBackend.getJPToSchedule());
        this.hiringJP = this.getTitleToJPMap(hrBackend.getJPToHire());
        this.archivedJP = this.getTitleToJPMap(hrBackend.getAllFilledJP());
        this.updatableJPs = this.getTitleToJPMap(hrBackend.getJPThatCanBeUpdated());
        this.allJP = this.getTitleToJPMap(hrBackend.getAllJP());

        this.importantJP.putAll(this.unreviewedJP);
        this.importantJP.putAll(this.scheduleJP);
        this.importantJP.putAll(this.hiringJP);
    }

    /**
     * Gets a hash map of titles to branch job postings from a list of job postings.
     * @param JPList a list of job postings.
     * @return the hash map of titles to branch job postings.
     */
    HashMap<String, BranchJobPosting> getTitleToJPMap(ArrayList<BranchJobPosting> JPList) {
        HashMap<String, BranchJobPosting> titleToJPMap = new HashMap<>();
        for (BranchJobPosting JP : JPList) {
            titleToJPMap.put(this.toJPTitle(JP), JP);
        }

        return titleToJPMap;
    }

    /**
     * Gets a string representation of the title of this branch job posting.
     * @param branchJobPosting a branch job posting.
     * @return the title to be displayed of this branch job posting.
     */
    String toJPTitle(BranchJobPosting branchJobPosting) {
        return branchJobPosting.getId() + "-" + branchJobPosting.getTitle();
    }

    void removeFromJPLists(BranchJobPosting branchJobPosting) {
        String title = this.toJPTitle(branchJobPosting);
        this.unreviewedJP.remove(title);
        this.scheduleJP.remove(title);
        this.hiringJP.remove(title);
        this.importantJP.remove(title);
    }

    HashMap<String, BranchJobPosting> getImportantJP() {
        return this.importantJP;
    }

    HashMap<String, BranchJobPosting> getUnreviewedJP() {
        return this.unreviewedJP;
    }

    HashMap<String, BranchJobPosting> getScheduleJP() {
        return this.scheduleJP;
    }

    HashMap<String, BranchJobPosting> getHiringJP() {
        return this.hiringJP;
    }

    HashMap<String, BranchJobPosting> getArchivedJP() {
        return this.archivedJP;
    }

    HashMap<String, BranchJobPosting> getUpdatableJPs() {
        return this.updatableJPs;
    }

    HashMap<String, BranchJobPosting> getAllJP() {
        return this.allJP;
    }

//    public static void main(String[] args) {//Phillips Test For Lif Cycle 2
//        LocalDate refdate = LocalDate.now();
//        JFrame frame = new JFrame("HR Main");
//        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
//        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        jobApplicationSystem.setToday(refdate.plusDays(7));
//        jobApplicationSystem.setPreviousLoginDate(refdate.plusDays(-1));
//        Reference reference = jobApplicationSystem.getUserManager().createReference("ref@gmail.com", refdate.plusDays(-1));
//        Company company = jobApplicationSystem.createCompany("Segufix");
//        Branch branch = company.createBranch("Segufix Canada", "E6H1P9");
//        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("Fran", "1", "Hannah J", "Fran@mail.com", "E6H1P9", refdate.plusDays(-1));
//        Applicant applicant1 = jobApplicationSystem.getUserManager().createApplicant("Will", "1", "Will W", "Will@mail.com", "E6H1P9", refdate.plusDays(-1));
//        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("Jon", "1", "Jon J", "Jon@mail.com", "E6H1P9", refdate.plusDays(-1));
//        Interviewer interviewer = jobApplicationSystem.getUserManager().createInterviewer("Stacy", "1", "Stacy O", "Stacy@mail.com", branch, "Sales", refdate.plusDays(-1));
//        HRCoordinator hrc = jobApplicationSystem.getUserManager().createHRCoordinator("Phil", "1", "Phillip Soetebeer", "email@gmail.com", branch, refdate.plusDays(-1));
//        BranchJobPosting jobPosting = hrc.addJobPosting("Posting 1", "Sales", "A Poor Choice",
//                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(Arrays.asList("Tags", "Tag2")), 2, refdate, refdate.plusDays(5));
//        JobApplication jobApp = new JobApplication(applicant, jobPosting, refdate);
//        JobApplication jobApp1 = new JobApplication(applicant1, jobPosting, refdate);
//        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting, refdate);
//        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
//        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
//        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
//        jobApp1.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
//        jobApp2.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
//        reference.addJobApplication(jobApp);
//        reference.addJobApplication(jobApp1);
//        reference.addJobApplication(jobApp2);
//        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
//        branchJobPostingManager.updateJobPostingsClosedForApplications(refdate.plusDays(6));
//        System.out.println("AAAAAAAAAAAAAAAA:"+branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().getHrTask());
//        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
//        frame.add(new HRMain(hrc, jobApplicationSystem, logoutActionListener));
//        frame.setSize(854, 480);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }


    public static void main(String[] args) {
//        JFrame frame = new JFrame("HR Main");
//        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
//        //new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
//        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 28));
//        jobApplicationSystem.setPreviousLoginDate(LocalDate.of(2019, 7, 19));
//        Reference reference = jobApplicationSystem.getUserManager().createReference("bob@gmail.com", LocalDate.of(2019, 7, 19));
//        Company company = jobApplicationSystem.createCompany("Company");
//        Branch branch = company.createBranch("Branch", "L4B4P8");
//        Branch branch2 = company.createBranch("Branch2", "L4B4P8");
//        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", LocalDate.now(), "L4B4P8");
//        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
//        HRCoordinator hrc2 = new HRCoordinator("HR2", "password", "HRC", "email@gmail.com", branch2, LocalDate.of(2019, 7, 19));
//        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
//                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(Arrays.asList("Tag1", "Tag2")), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 25));
//        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 19));
//        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
//        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
//        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
//        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("username2", "password", "Legal Name2", "email@gmail.com", LocalDate.of(2019, 7, 19), "L4B4P8");
//        BranchJobPosting jobPosting2 = hrc2.addJobPosting("title2", "field", "description",
//                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 26));
//        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, LocalDate.of(2019, 7, 19));
//        reference.addJobApplication(jobApp);
//        reference.addJobApplication(jobApp2);
//        BranchJobPostingManager branchJobPostingManager = branch.getJobPostingManager();
//        branchJobPostingManager.updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 21));
//        branchJobPostingManager.updateJobPostingsClosedForReferences(LocalDate.of(2019, 7, 27));
//        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
//        frame.add(new HRMain(hrc, jobApplicationSystem, logoutActionListener));
//        frame.setSize(854, 480);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Run this!
        // This instantiates 2 applicants who apply to a single job posting
        // Also instantiates an hr and interviewer in that company
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "Toronto");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", "L4B4P8", LocalDate.now());
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19));
        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.now());
        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("username2", "password", "Legal Name2", "email@gmail.com", "L4B4P8", LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting2 = hrc.addJobPosting("title2", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 20), LocalDate.of(2019, 7, 20));
        JobApplication jobApp2 = new JobApplication(applicant2, jobPosting2, LocalDate.of(2019, 7, 19));
        JobApplicationDocument doc3 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        jobApp2.addFiles(new ArrayList<>(Arrays.asList(doc3)));
        jobApplicationSystem.setToday(LocalDate.of(2019, 8, 25));
        new Interviewer("Interviewer", "password", "Bobby", "email", branch, "field", LocalDate.of(2019, 7, 10));
        jobApplicationSystem.updateAllJobPostings();
        LogoutActionListener logoutActionListener = new LogoutActionListener(new Container(), new CardLayout(), jobApplicationSystem);
        JFrame frame = new JFrame();
        frame.add(new HRMain(hrc, jobApplicationSystem, logoutActionListener));
        frame.setSize(854, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
