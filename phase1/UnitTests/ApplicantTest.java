import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantTest {

    void createJAS() {
        Company company = JobApplicationSystem.addCompany("Company");
        JobPosting job1 = new SinglePositionJobPosting();
        job1.setCloseDate(LocalDate.of(2019, 7, 7));
        JobPosting job2 = new SinglePositionJobPosting();
        job1.setCloseDate(LocalDate.of(2019, 6, 7));
        JobPosting job3 = new SinglePositionJobPosting();
        job1.setCloseDate(LocalDate.of(2019, 5, 7));
        ArrayList<JobPosting> jobPostings = new ArrayList<>(Arrays.asList(job1, job2, job3));
        company.setJobPostings(jobPostings);
    }

    Applicant createApplicant() {
        return new Applicant("username", "password", "legalName",
                "email@gmail.com", LocalDate.of(2019, 6, 17));
    }

    @Test
    void testViewJobPostings() {
        this.createJAS();
        assertEquals(3, new Applicant().viewJobPostings().size());
    }

    @Test
    void testApplyForJob() {
        this.createJAS();
        JobPosting job = JobApplicationSystem.getAllJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        assertFalse(a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 7, 8)));
        assertTrue(a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 7, 6)));
        assertFalse(a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 7, 6)));
        assertEquals(1, a.getAllJobApplications().size());
    }

    @Test
    void testWithdrawApplication() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        job1.setFilled();
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        assertFalse(a.withdrawApplication(job1));
        assertTrue(a.withdrawApplication(job2));
        assertEquals(1, a.getAllJobApplications().size());
    }

    @Test
    void testGetAllJobApplications() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        assertEquals(2, a.getAllJobApplications().size());
    }

    @Test
    void testGetPreviousJobApplications() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        assertEquals(0, a.getPreviousJobApplications().size());

        job1.setFilled();
        assertEquals(1, a.getPreviousJobApplications().size());
    }

    @Test
    void testGetCurrentJobApplications() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        assertEquals(2, a.getCurrentJobApplications().size());

        job1.setFilled();
        assertEquals(1, a.getCurrentJobApplications().size());
    }

    @Test
    void testGetNumDaysSinceMostRecentClosing() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        LocalDate today1 = LocalDate.of(2019, 7, 8);
        LocalDate today2 = LocalDate.of(2019, 7, 7);
        LocalDate today3 = LocalDate.of(2019, 6, 8);
        assertEquals(1, a.getNumDaysSinceMostRecentClosing(today1));
        assertEquals(0, a.getNumDaysSinceMostRecentClosing(today2));
        assertEquals(0, a.getNumDaysSinceMostRecentClosing(today3));
    }

    @Test
    void testIsInactive() {
        this.createJAS();
        JobPosting job1 = JobApplicationSystem.getAllJobPostings().get(0);
        JobPosting job2 = JobApplicationSystem.getAllJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 7, 6));
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 7, 6));
        LocalDate today1 = LocalDate.of(2019, 8, 5);
        LocalDate today2 = LocalDate.of(2019, 8, 6);
        LocalDate today3 = LocalDate.of(2019, 8, 7);
        assertFalse(a.isInactive(today1));
        assertTrue(a.isInactive(today2));
        assertTrue(a.isInactive(today3));
    }
}