package ApplicantStuff;

import CompanyStuff.*;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationManagerTest {

    Applicant createApplicant() {
        return new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
    }

    JobApplication createJobApplication(Applicant applicant, BranchJobPosting jobPosting) {
        return new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
    }

    Branch createCompanyBranchEmployeesAndJobPostings() {
        Company company1 = new Company("Company");
        Branch branch1A = company1.createBranch("Branch", "Toronto");
        HRCoordinator hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, LocalDate.of(2019, 7, 15));
        new Interviewer("joe", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, "field", LocalDate.of(2019, 7, 15));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        return branch1A;
    }

    @Test
    void testConstructor() {
        JobApplicationManager jobAppManager = new JobApplicationManager();
        assertTrue(jobAppManager.getJobApplications().isEmpty());
    }

    @Test
    void testAddJobApplication() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        JobApplicationManager jobAppManager = applicant.getJobApplicationManager();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        jobAppManager.addJobApplication(jobApp);
        assertEquals(1, jobAppManager.getJobApplications().size());
        jobAppManager.addJobApplication(jobApp);
        assertEquals(2, jobAppManager.getJobApplications().size());
        assertEquals("jsmith", jobAppManager.getJobApplications().get(1).getApplicant().getUsername());
    }

    @Test
    void testRemoveJobApplicationFromBranchJobPostingNotAppliedTo() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        JobApplicationManager jobAppManager = applicant.getJobApplicationManager();

        jobAppManager.removeJobApplication(jobPosting2);
        assertTrue(jobAppManager.getJobApplications().isEmpty());

        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        jobAppManager.addJobApplication(jobApp);
        jobAppManager.removeJobApplication(jobPosting2);
        assertEquals(1, jobAppManager.getJobApplications().size());
    }

    @Test
    void testRemoveJobApplicationFromBranchJobPostingAppliedTo() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        JobApplicationManager jobAppManager = applicant.getJobApplicationManager();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        jobAppManager.addJobApplication(jobApp);
        jobAppManager.addJobApplication(jobApp2);
        assertEquals(2, jobAppManager.getJobApplications().size());
        jobAppManager.removeJobApplication(jobPosting);
        assertEquals(1, jobAppManager.getJobApplications().size());
        jobAppManager.removeJobApplication(jobPosting2);
        assertTrue(jobAppManager.getJobApplications().isEmpty());
    }

    @Test
    void testGetUpcomingInterviewsNoInterviews() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        JobApplicationManager jobAppManager = applicant.getJobApplicationManager();
        assertTrue(jobAppManager.getUpcomingInterviews(
                LocalDate.of(2019, 7, 21)).isEmpty());
        this.createJobApplication(applicant, jobPosting);
        assertTrue(applicant.getJobApplicationManager().getUpcomingInterviews(
                LocalDate.of(2019, 7, 24)).isEmpty());
    }

    @Test
    void testGetUpcomingInterviewsOneInterviewWithin7Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 17), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)));
    }

    @Test
    void testGetUpcomingInterviewsMultipleInterviewsWithin7Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 17), InterviewTime.ELEVEN_AM_TO_NOON));
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 14), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(2, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)));
    }

    @Test
    void testGetUpcomingInterviewsOneInterviewInExactly7Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)));
    }

    @Test
    void getUpcomingInterviewsOneInterviewInExactly8Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 11)));
    }

    @Test
    void getUpcomingInterviewsMixedTiming() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 20), InterviewTime.ELEVEN_AM_TO_NOON));
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)));
    }


    @Test
    void getNumDaysSinceMostRecentCloseDate() {
        Applicant applicant = this.createApplicant();

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