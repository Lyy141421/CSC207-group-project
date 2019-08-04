package ApplicantStuff;

import CompanyStuff.*;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationManagerTest {

    Applicant createApplicant() {
        return new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", "Toronto", LocalDate.of(2019, 7, 20));
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
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 8, 3),
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
        this.createJobApplication(applicant, jobPosting);
        assertEquals(1, jobAppManager.getJobApplications().size());
        this.createJobApplication(applicant, jobPosting);
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

        this.createJobApplication(applicant, jobPosting);
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
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
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
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 4));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 17), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)).size());
    }

    @Test
    void testGetUpcomingInterviewsMultipleInterviewsWithin7Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 4));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        jobPosting2.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 17), InterviewTime.ELEVEN_AM_TO_NOON));
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(1).setTime(new InterviewTime(LocalDate.of(2019, 8, 14), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(2, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)).size());
    }

    @Test
    void testGetUpcomingInterviewsOneInterviewInExactly7Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 4));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)).size());
    }

    @Test
    void getUpcomingInterviewsOneInterviewInExactly8Days() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 4));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertTrue(applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 11)).isEmpty());
    }

    @Test
    void getUpcomingInterviewsMixedTiming() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Applicant applicant = this.createApplicant();
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 4));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        jobPosting2.getInterviewManager().setUpOneOnOneInterviews();
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(0).setTime(new InterviewTime(LocalDate.of(2019, 8, 20), InterviewTime.ELEVEN_AM_TO_NOON));
        branch.getFieldToInterviewers().get("field").get(0).getInterviews().get(1).setTime(new InterviewTime(LocalDate.of(2019, 8, 19), InterviewTime.ELEVEN_AM_TO_NOON));
        assertEquals(1, applicant.getJobApplicationManager().getUpcomingInterviews(LocalDate.of(2019, 8, 12)).size());
    }

    @Test
    void testGetNumDaysSinceMostRecentCloseDateNoJobApps() {
        Applicant applicant = this.createApplicant();
        assertEquals(0, applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(LocalDate.now()));
    }

    @Test
    void testGetNumDaysSinceMostRecentCloseDateOneOpenJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        this.createJobApplication(applicant, jobPosting);
        assertEquals(0, applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(jobPosting.getApplicantCloseDate().minusDays(3)));
    }

    @Test
    void testGetNumDaysSinceMostRecentCloseDateOneClosedJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        this.createJobApplication(applicant, jobPosting);
        assertEquals(3, applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(jobPosting.getApplicantCloseDate().plusDays(3)));
    }

    @Test
    void testGetNumDaysSinceMostRecentCloseDateOneClosedOneOpen() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        this.createJobApplication(applicant, jobPosting);
        this.createJobApplication(applicant, jobPosting2);
        assertEquals(0, applicant.getJobApplicationManager().getNumDaysSinceMostRecentCloseDate(jobPosting.getApplicantCloseDate().plusDays(2)));
    }

    @Test
    void testGetPreviousJobApplicationsNoJobApps() {
        Applicant applicant = this.createApplicant();
        assertTrue(applicant.getJobApplicationManager().getPreviousJobApplications().isEmpty());
    }

    @Test
    void testGetPreviousJobApplicationsOneJobAppOpen() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        this.createJobApplication(applicant, jobPosting);
        assertTrue(applicant.getJobApplicationManager().getPreviousJobApplications().isEmpty());
    }

    @Test
    void testGetPreviousJobApplicationsOneJobAppArchived() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        jobApp.getStatus().setArchived();
        assertEquals(1, applicant.getJobApplicationManager().getPreviousJobApplications().size());
    }

    @Test
    void testGetPreviousJobApplicationsMixed() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        JobApplication jobApp1 = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        assertTrue(applicant.getJobApplicationManager().getPreviousJobApplications().isEmpty());
        jobApp1.getStatus().setArchived();
        assertEquals(1, applicant.getJobApplicationManager().getPreviousJobApplications().size());
        jobApp2.getStatus().setArchived();
        assertEquals(2, applicant.getJobApplicationManager().getPreviousJobApplications().size());
    }

    @Test
    void testGetCurrentJobApplicationsNoJobApps() {
        Applicant applicant = this.createApplicant();
        assertTrue(applicant.getJobApplicationManager().getCurrentJobApplications().isEmpty());
    }

    @Test
    void testGetCurrentJobApplicationsOneFilledJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApp1 = this.createJobApplication(applicant, jobPosting);
        jobApp1.getStatus().setArchived();
        assertTrue(applicant.getJobApplicationManager().getCurrentJobApplications().isEmpty());
    }

    @Test
    void testGetCurrentJobApplicationsOneOpenJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        this.createJobApplication(applicant, jobPosting);
        assertEquals(1, applicant.getJobApplicationManager().getCurrentJobApplications().size());
    }

    @Test
    void testGetCurrentJobApplicationsMixed() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        JobApplication jobApp1 = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        assertEquals(2, applicant.getJobApplicationManager().getCurrentJobApplications().size());
        jobApp1.getStatus().setArchived();
        assertEquals(1, applicant.getJobApplicationManager().getCurrentJobApplications().size());
        jobApp2.getStatus().setArchived();
        assertTrue(applicant.getJobApplicationManager().getCurrentJobApplications().isEmpty());
    }

    @Test
    void testGetCurrentJobApplicationsOneJobAppThenWithdrawn() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApp1 = this.createJobApplication(applicant, jobPosting);
        assertEquals(1, applicant.getJobApplicationManager().getCurrentJobApplications().size());
        branch.getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 8, 5));
        branch.getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        applicant.withdrawJobApplication(LocalDate.of(2019, 8, 13), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getCurrentJobApplications().isEmpty());
    }

    @Test
    void testGetLastClosedJobAppNoJobApp() {
        Applicant applicant = this.createApplicant();
        assertNull(applicant.getJobApplicationManager().getLastClosedJobApp());
    }

    @Test
    void testGetLastClosedJobAppOneOpenJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApp1 = this.createJobApplication(applicant, jobPosting);
        assertEquals(jobApp1, applicant.getJobApplicationManager().getLastClosedJobApp());
    }

    @Test
    void testGetLastClosedJobAppTwoOpenJobApp() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        assertEquals(jobApp2, applicant.getJobApplicationManager().getLastClosedJobApp());
    }

    @Test
    void testFindJobApplicationNoJobAppToPosting() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        assertNull(applicant.getJobApplicationManager().findJobApplication(jobPosting));
    }

    @Test
    void testFindJobApplicationJobAppToPosting() {
        Applicant applicant = this.createApplicant();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        assertEquals(jobApp, applicant.getJobApplicationManager().findJobApplication(jobPosting));
    }
}