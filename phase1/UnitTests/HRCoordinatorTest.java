import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HRCoordinatorTest {

    HRCoordinator createHRCoordinator() {
        Company company = new Company("Company");
        return new HRCoordinator("username", "password", "legalName",
                "email", company, LocalDate.of(2019, 6, 7));
    }

    @Test
    void testGetCompany() {
        HRCoordinator HRC = createHRCoordinator();
        assertEquals("Company", HRC.getCompany().getName());
    }

    @Test
    void testAddJobPosting() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        assertEquals("title", HRC.getCompany().getJobPostings().get(0).getTitle());
    }

    @Test
    void testUpdateJobPostingStatus() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        HRC.updateJobPostingStatus(HRC.getCompany().getJobPostings().get(0));
        assertTrue(HRC.getCompany().getJobPostings().get(0).isFilled());
    }

    @Test
    void testViewAllJobApplications() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        assertTrue(HRC.viewAllApplications(job).isEmpty());

        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(1, HRC.viewAllApplications(job).size());
    }

    @Test
    void testViewApplication() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(HRC.viewAllApplications(job).get(0), HRC.viewApplication(job, a));
    }

    @Test
    void testViewCoverLetter() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(coverLetter, HRC.viewCoverLetter(job, a));
    }

    @Test
    void testViewCV() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(CV, HRC.viewCV(job, a));
    }

    @Test
    void testViewAllApplicants() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        assertTrue(HRC.viewAllApplicants(job).isEmpty());

        Applicant a = new Applicant();
        Applicant b = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        b.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(2, HRC.viewAllApplicants(job).size());
    }

    @Test
    void testViewPreviousApplicationsToCompany() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title1", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        HRC.addJobPosting("title2", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 12),
                LocalDate.of(2019, 7, 7));
        JobPosting job1 = HRC.getCompany().getJobPostings().get(0);
        JobPosting job2 = HRC.getCompany().getJobPostings().get(1);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        assertTrue(HRC.viewAllApplicationsToCompany(a).isEmpty());
        a.applyForJob(job1, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(1, HRC.viewAllApplicationsToCompany(a).size());
        a.applyForJob(job2, CV, coverLetter, LocalDate.of(2019, 6, 19));
        assertEquals(2, HRC.viewAllApplicationsToCompany(a).size());
    }

    @Test
    void testReviewApplications() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title1", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        Applicant b = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        b.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        HRC.reviewApplications(job);
        for (JobApplication app : HRC.viewAllApplications(job)) {
            assertEquals(-1, app.getStatus());
        }
    }

    @Test
    void testAdvanceApplication() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title1", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        HRC.reviewApplications(job);
        JobApplication app = HRC.viewApplication(job, a);

        HRC.advanceApplication(app);
        assertEquals(0, app.getStatus());
        HRC.advanceApplication(app);
        assertEquals(1, app.getStatus());
        HRC.advanceApplication(app);
        assertEquals(2, app.getStatus());
        HRC.advanceApplication(app);
        assertEquals(3, app.getStatus());
        HRC.advanceApplication(app);
        assertEquals(4, app.getStatus());
    }

    @Test
    void addInterviewer() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addInterviewer("email", "field", LocalDate.now());
        assertEquals(1, HRC.getCompany().getFieldToInterviewers().size());
    }

    @Test
    void testSetUpInterview() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title1", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        HRC.reviewApplications(job);
        JobApplication app = HRC.viewApplication(job, a);
        HRC.addInterviewer("email", "field", LocalDate.now());
        Interviewer interviewer = HRC.getCompany().getFieldToInterviewers().get("field").get(0);
        HRC.setUpInterview(app, 0);
        assertEquals(1, interviewer.getInterviews().size());
    }

    @Test
    void testHireApplicant() {
        HRCoordinator HRC = createHRCoordinator();
        HRC.addJobPosting("title1", "field", "description",
                new ArrayList<>(), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        JobPosting job = HRC.getCompany().getJobPostings().get(0);
        Applicant a = new Applicant();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        a.applyForJob(job, CV, coverLetter, LocalDate.of(2019, 6, 19));
        HRC.reviewApplications(job);
        HRC.hireApplicant(job);
        assertTrue(job.isFilled());
    }
}