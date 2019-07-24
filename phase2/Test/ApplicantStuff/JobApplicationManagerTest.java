package ApplicantStuff;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationManagerTest {

    private Applicant applicant;
    private Company company1;
    private Branch branch1A;
    private HRCoordinator hrc;
    private BranchJobPosting jobPosting;
    private BranchJobPosting jobPosting2;

    JobApplicationManagerTest() {
        applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        company1 = new Company("Company");
        branch1A = company1.createBranch("Branch", "Toronto");
        hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, LocalDate.of(2019, 7, 15));
        jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        jobPosting2 = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
    }

    JobApplication createJobApplication(BranchJobPosting jobPosting) {
        return new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
    }

    @Test
    void testConstructor() {
        JobApplicationManager jobAppManager = new JobApplicationManager();
        assertTrue(jobAppManager.getJobApplications().isEmpty());
    }

    @Test
    void addJobApplication() {
        JobApplicationManager jobAppManager = new JobApplicationManager();
        JobApplication jobApp = this.createJobApplication(jobPosting);
        jobAppManager.addJobApplication(jobApp);
        assertEquals(1, jobAppManager.getJobApplications().size());
        jobAppManager.addJobApplication(jobApp);
        assertEquals(2, jobAppManager.getJobApplications().size());
        assertEquals("jsmith", jobAppManager.getJobApplications().get(1).getApplicant().getUsername());
    }

    @Test
    void removeJobApplicationFromBranchJobPostingNotAppliedTo() {
        JobApplicationManager jobAppManager = new JobApplicationManager();

        jobAppManager.removeJobApplication(jobPosting2);
        assertTrue(jobAppManager.getJobApplications().isEmpty());

        JobApplication jobApp = this.createJobApplication(jobPosting);
        jobAppManager.addJobApplication(jobApp);
        jobAppManager.removeJobApplication(jobPosting2);
        assertEquals(1, jobAppManager.getJobApplications().size());
    }

    @Test
    void removeJobApplicationFromBranchJobPostingAppliedTo() {
        JobApplicationManager jobAppManager = new JobApplicationManager();
        JobApplication jobApp = this.createJobApplication(jobPosting);
        JobApplication jobApp2 = this.createJobApplication(jobPosting2);
        jobAppManager.addJobApplication(jobApp);
        jobAppManager.addJobApplication(jobApp2);
        assertEquals(2, jobAppManager.getJobApplications().size());
        jobAppManager.removeJobApplication(jobPosting);
        assertEquals(1, jobAppManager.getJobApplications().size());
        jobAppManager.removeJobApplication(jobPosting2);
        assertTrue(jobAppManager.getJobApplications().isEmpty());
    }

    @Test
    void getUpcomingInterviews() {

    }

    @Test
    void getNumDaysSinceMostRecentCloseDate() {
    }

    @Test
    void getPreviousJobApplications() {
    }

    @Test
    void getCurrentJobApplications() {
    }

    @Test
    void getFilesSubmittedForApplication() {
    }

    @Test
    void getLastClosedJobApp() {
    }
}