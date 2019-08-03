package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.Reference;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interview;
import org.junit.jupiter.api.Test;

import java.sql.Ref;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BranchJobPostingManagerTest {

    private LocalDate today = LocalDate.of(2019, 7, 29);
    private Company company = new Company("company_a");
    private Branch branch = company.createBranch("BranchName", "E6H1P9");
    private HRCoordinator hrcoordinator = new HRCoordinator("Stacy", "ABC123",
            "Stacy Anderson" + " LeagalName", "Stacy@gmail.com", branch, today);

    BranchJobPosting createJobPostingHR() {
        return hrcoordinator.addJobPosting("Title", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                today, today.plusDays(5), today.plusDays(5));
    }

    BranchJobPosting createJobPostingHRGroup() {
        return hrcoordinator.addJobPosting("Title", "field", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 2,
                today, today.plusDays(5), today.plusDays(5));
    }

    CompanyJobPosting createCompanyJobPosting() {
        CompanyJobPosting posting = new CompanyJobPosting("Title", "field", "description",
        new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<String>(),
                company, branch);
        company.addCompanyJobPosting(posting);
        return posting;
    }

    Reference createReference() {
        return new Reference("Ref@gmail.com", today);
    }

    BranchJobPosting createPosting(String name){
        return new BranchJobPosting(name, "Sales", "none", new ArrayList<>(),
                new ArrayList<>(), 1, branch, today, today.plusDays(5), today.plusDays(5));
    }

    Applicant createApplicant(String name) {
        return new Applicant(name, "ABC123", "Legal Name",
                name + "@gmail.com", today, "Toronto");
    }

    JobApplication createJobApplication(Applicant app, BranchJobPosting posting){
        return new JobApplication(app, posting, today);
    }

    @Test
    void getBranchJobPosting() {
        BranchJobPosting posting = createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getBranchJobPosting(posting.getId()), posting);
    }

    @Test
    void getBranchJobPostings() {
        BranchJobPosting posting = createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getBranchJobPostings().get(0), posting);
    }

    @Test
    void addJobPosting() {
        BranchJobPosting posting = createPosting("Test Posting");
        branch.getJobPostingManager().addJobPosting(posting);
        assertEquals(branch.getJobPostingManager().getBranchJobPostings().get(0), posting);
    }

    @Test
    void getJobPostingsNotAppliedToByApplicant() {
        BranchJobPosting posting = createJobPostingHR();
        Applicant app = createApplicant("Phillip");
        assertEquals(branch.getJobPostingManager().getJobPostingsNotAppliedToByApplicant(app).size(), 1);
        posting.addJobApplication(createJobApplication(app, posting));
        assertEquals(branch.getJobPostingManager().getJobPostingsNotAppliedToByApplicant(app).size(), 0);
    }

    @Test
    void getOpenJobPostings() {
        createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getOpenJobPostings(today).size(), 1);
        assertEquals(branch.getJobPostingManager().getOpenJobPostings(today.plusDays(7)).size(), 0);

    }

    @Test
    void getClosedJobPostingsNotFilled() {
        createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getClosedJobPostingsNotFilled(today).size(), 0);
        assertEquals(branch.getJobPostingManager().getClosedJobPostingsNotFilled(today.plusDays(7)).size(), 1);
    }

    @Test
    void getJobPostingsRecentlyClosedForApplications() {
        createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getJobPostingsRecentlyClosedForApplications(today.plusDays(7)).size(), 1);
    }

    @Test
    void getJobPostingsRecentlyClosedForReferences() {
        createJobApplication(createApplicant("Phil"), createJobPostingHR());
        branch.getJobPostingManager().getBranchJobPostings().get(0).createInterviewManager();
        assertEquals(branch.getJobPostingManager().getJobPostingsRecentlyClosedForReferences(today.plusDays(7)).size(), 1);
    }

    @Test
    void getJobPostingsThatNeedDeadlineExtensions() {
        createJobPostingHR();
        branch.getJobPostingManager().getBranchJobPostings().get(0).createInterviewManager();
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedDeadlineExtensions(today).size(), 0);
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedDeadlineExtensions(today.plusDays(7)).size(), 1);
    }

    @Test
    void getJobPostingsThatNeedGroupInterviewsScheduled() {
        BranchJobPosting posting_single = createJobPostingHR();
        BranchJobPosting posting_group = createJobPostingHRGroup();
        branch.getJobPostingManager().getBranchJobPostings().get(0).createInterviewManager();
        branch.getJobPostingManager().getBranchJobPostings().get(1).createInterviewManager();
        Applicant app1 = createApplicant("Phil");
        Applicant app2 = createApplicant("Will");
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedGroupInterviewsScheduled(today.plusDays(7)).size(), 0);
        posting_group.addJobApplication(createJobApplication(app1, posting_single));
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedGroupInterviewsScheduled(today.plusDays(7)).size(), 0);
        posting_group.addJobApplication(createJobApplication(app1, posting_group));
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedGroupInterviewsScheduled(today.plusDays(7)).size(), 0);
        posting_group.addJobApplication(createJobApplication(app2, posting_group));
        assertEquals(branch.getJobPostingManager().getJobPostingsThatNeedGroupInterviewsScheduled(today.plusDays(7)).size(), 1);

    }

    @Test
    void getJobPostingsThatNeedHRSelectionForHiring() {
    }

    @Test
    void updateAllClosedUnfilledJobPostings() {
        createJobPostingHR();
        branch.getJobPostingManager().getBranchJobPostings().get(0).createInterviewManager();
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().
                setInterviewConfiguration(interviewConfiguration);
        branch.getJobPostingManager().updateAllClosedUnfilledJobPostings(today);
        assertEquals(branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().getCurrentRound(), -1);
        branch.getJobPostingManager().updateAllClosedUnfilledJobPostings(today.plusDays(7));
        assertEquals(branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().getCurrentRound(), 0);
    }

    @Test
    void getAllApplicantsToBranch() {
        BranchJobPosting posting_single = createJobPostingHR();
        BranchJobPosting posting_group = createJobPostingHRGroup();
        Applicant app1 = createApplicant("Phil");
        Applicant app2 = createApplicant("Will");
        assertEquals(branch.getJobPostingManager().getAllApplicantsToBranch(today).size(), 0);
        createJobApplication(app1, posting_group);
        assertEquals(branch.getJobPostingManager().getAllApplicantsToBranch(today).size(), 0);
        assertEquals(branch.getJobPostingManager().getAllApplicantsToBranch(today.plusDays(7)).size(), 1);
        createJobApplication(app1, posting_single);
        assertEquals(branch.getJobPostingManager().getAllApplicantsToBranch(today.plusDays(7)).size(), 1);
        createJobApplication(app2, posting_group);
        assertEquals(branch.getJobPostingManager().getAllApplicantsToBranch(today.plusDays(7)).size(), 2);
    }

    @Test
    void getExtendableCompanyJobPostings() {
        assertEquals(branch.getJobPostingManager().getExtendableCompanyJobPostings().size(), 0);
        createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getExtendableCompanyJobPostings().size(), 0);
        CompanyJobPosting posting = createCompanyJobPosting();
        assertEquals(branch.getJobPostingManager().getExtendableCompanyJobPostings().size(), 0);
    }

    @Test
    void getUpdatableJobPostings(){
        createJobPostingHR();
        assertEquals(branch.getJobPostingManager().getUpdatableJobPostings(today).size(), 1);
    }

    @Test
    void getBranch() {
        assertEquals(branch.getJobPostingManager().getBranch(), branch);
    }

    @Test
    void setBranch() {
        Branch branch2 = new Branch("Test", "Toronto", company);
        branch.getJobPostingManager().setBranch(branch2);
        assertEquals(branch.getJobPostingManager().getBranch(), branch2);
    }

    @Test
    void updateJobPostingsClosedForApplications() {
        BranchJobPosting posting = createJobPostingHR();
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(today);
        assertNull(branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager());
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(today.plusDays(7));
        assertNull(branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager());

        Applicant app1 = createApplicant("Phil");
        JobApplication application = createJobApplication(app1, posting);
//        assertEquals(app1.getJobApplicationManager().getJobApplications().size(), 1);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(today.plusDays(7));
        assertNotNull(branch.getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager());

    }

    @Test
    void updateJobPostingsClosedForReferences() {
        BranchJobPosting posting = createJobPostingHR();
        Applicant app1 = createApplicant("Phil");
        JobApplication application = createJobApplication(app1, posting);
        ArrayList<Reference> ref_list = new ArrayList<>();
        Reference ref = createReference();
        ref_list.add(ref);
        assertEquals(ref.getJobPostings().size(), 0);
        application.addReferences(ref_list);
        assertEquals(ref.getJobPostings().size(), 1);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(today.plusDays(7));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(today.plusDays(7));
        assertEquals(ref.getJobPostings().size(), 0);

    }
}