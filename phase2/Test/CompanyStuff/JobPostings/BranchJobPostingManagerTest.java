package CompanyStuff.JobPostings;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import org.junit.jupiter.api.Test;

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
        return hrcoordinator.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                today, today.plusDays(5), today.plusDays(5));
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
        
    }

    @Test
    void getJobPostingsRecentlyClosedForApplications() {
    }

    @Test
    void getJobPostingsRecentlyClosedForReferences() {
    }

    @Test
    void getJobPostingsThatNeedInterviewProcessConfigured() {
    }

    @Test
    void getFilledJobPostings() {
    }

    @Test
    void getJobPostingsThatNeedGroupInterviewsScheduled() {
    }

    @Test
    void getJobPostingsThatNeedHRSelectionForHiring() {
    }

    @Test
    void updateAllClosedUnfilledJobPostings() {
    }

    @Test
    void getBranch() {
    }

    @Test
    void setBranch() {
    }

    @Test
    void updateJobPostingsClosedForApplications() {
    }

    @Test
    void updateJobPostingsClosedForReferences() {
    }
}