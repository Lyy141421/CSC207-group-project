package GUIClasses.HRInterface;

import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import GUIClasses.ActionListeners.LogoutActionListener;
import GUIClasses.CommonUserGUI.UserMain;
import GUIClasses.CommonUserGUI.UserProfilePanel;
import Main.JobApplicationSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

        this.add(new HRSideBarMenuPanel(cards, hrBackend, logoutActionListener), c);
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
        cards.removeAll();
        this.setJPLists();
        this.setCards();
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
     *
     * @param JPList a list of job postings.
     * @return the hash map of titles to branch job postings.
     */
    private HashMap<String, BranchJobPosting> getTitleToJPMap(ArrayList<BranchJobPosting> JPList) {
        HashMap<String, BranchJobPosting> titleToJPMap = new HashMap<>();
        for (BranchJobPosting JP : JPList) {
            titleToJPMap.put(this.toJPTitle(JP), JP);
        }

        return titleToJPMap;
    }

    /**
     * Gets a string representation of the title of this branch job posting.
     *
     * @param branchJobPosting a branch job posting.
     * @return the title to be displayed of this branch job posting.
     */
    private String toJPTitle(BranchJobPosting branchJobPosting) {
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


    /*public static void main(String[] args) {
        //Run this!
        //This instantiates 2 applicants who apply to a single job posting
        //Also instantiates an hr and interviewer in that company
        JobApplicationSystem jobApplicationSystem = new JobApplicationSystem();
        new DataLoaderAndStorer(jobApplicationSystem).loadAllData();
        jobApplicationSystem.setToday(LocalDate.of(2019, 7, 19));
        Company company = jobApplicationSystem.createCompany("Company");
        Branch branch = company.createBranch("Branch", "Toronto");
        Applicant applicant = jobApplicationSystem.getUserManager().createApplicant("username", "password", "Legal Name", "email@gmail.com", "L4B4P8", jobApplicationSystem);
        HRCoordinator hrc = new HRCoordinator("HR", "password", "HRC", "email@gmail.com", branch, LocalDate.of(2019, 7, 19));
        BranchJobPosting jobPosting = hrc.addJobPosting("title", "field", "descriptionhujedkfnvsgrhjegskamkagjrwuiladkvmkajgirwouskvmzkjgiskdzvn,mkngs\niznvjgsirklzngjslitw4gsijlkznjsirtwtrsigjlzknvmJDEI0   IPUOwrahektdznmv\nlpox-98uy7gufhvnb tmwkeafoisCXU*yygchbvn    4mk2RWFsvzx\nwgudkngrhadkjn\nwhaegkjsc\ngwaeihfkncMZ<ghaecsknm,z\n",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, LocalDate.of(2019, 7, 19), LocalDate.of(2019, 7, 19));
        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.now());
        JobApplicationDocument doc = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        JobApplicationDocument doc2 = new JobApplicationDocument(new File("./sample.txt"), applicant.getUsername());
        jobApp.addFiles(new ArrayList<>(Arrays.asList(doc, doc2)));
        Applicant applicant2 = jobApplicationSystem.getUserManager().createApplicant("username2", "password", "Legal Name2", "email@gmail.com", "L4B4P8", jobApplicationSystem);
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
    }*/
}
