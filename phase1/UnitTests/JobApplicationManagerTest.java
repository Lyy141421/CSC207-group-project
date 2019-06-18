import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;

class JobApplicationManagerTest {

    ArrayList<JobApplication> createJobApplications() {
        JobPosting job1 = new SinglePositionJobPosting("title1", "field1", "description",
                new ArrayList<>(), new Company("Company"), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 7));
        job1.setFilled();
        JobPosting job2 = new SinglePositionJobPosting("title2", "field1", "description",
                new ArrayList<>(), new Company("Company"), LocalDate.of(2019, 6, 7),
                LocalDate.of(2019, 7, 8));

        JobApplication app1 = new JobApplication(job1, LocalDate.now());
        JobApplication app2 = new JobApplication(job2, LocalDate.now());

        return new ArrayList<>(Arrays.asList(app1, app2));
    }

    JobApplicationManager createJobApplicationManager() {
        JobApplicationManager JAM = new JobApplicationManager(this.createJobApplications());
        JAM.setMostRecentCloseDate(LocalDate.of(2019, 7, 8));
        return JAM;
    }

    @Test
    void testGetJobApplicationsEmpty() {
        JobApplicationManager JAM = new JobApplicationManager();
        assertTrue(JAM.getJobApplications().isEmpty());
    }

    @Test
    void testGetJobApplicationsNonEmpty() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        assertEquals("title1", JAM.getJobApplications().get(0).getJobPosting().getTitle());
        assertEquals("title2", JAM.getJobApplications().get(1).getJobPosting().getTitle());
    }

    @Test
    void testGetMostRecentCloseDateNull() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        assertNull(JAM.getMostRecentCloseDate());
    }

    @Test
    void testGetMostRecentCloseDateNotNull() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        assertEquals(LocalDate.of(2019, 7, 8), JAM.getMostRecentCloseDate());
    }

    @Test
    void testAddJobApplication() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        Applicant applicant = new Applicant();
        JobPosting jobPosting = new SinglePositionJobPosting();
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        JAM.addJobApplication(applicant, jobPosting, CV, coverLetter, LocalDate.now());
        assertEquals(3, JAM.getJobApplications().size());
    }

    @Test
    void testRemoveJobApplication() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        JobPosting job2 = JAM.getJobApplications().get(1).getJobPosting();
        JAM.removeJobApplication(job2);
        assertEquals(1, JAM.getJobApplications().size());
    }

    @Test
    void testGetPreviousJobApplications() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        JobApplication app = JAM.getJobApplications().get(0);
        assertEquals(app, JAM.getPreviousJobApplications().get(0));
    }

    @Test
    void testGetCurrentJobApplications() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        JobApplication app = JAM.getJobApplications().get(1);
        assertEquals(app, JAM.getCurrentJobApplications().get(0));
    }

    @Test
    void testFindMostRecentCloseDate() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        assertEquals(LocalDate.of(2019, 7, 7), JAM.getMostRecentCloseDate());
    }

    @Test
    void testUpdateMostRecentCloseDate() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        Applicant applicant = new Applicant();
        JobPosting jobPosting = new SinglePositionJobPosting();
        jobPosting.setCloseDate(LocalDate.of(2019, 7, 20));
        File CV = new File("README.txt");
        File coverLetter = new File("README.txt");
        JAM.addJobApplication(applicant, jobPosting, CV, coverLetter, LocalDate.now());
        JAM.updateMostRecentCloseDate();
        assertEquals(LocalDate.of(2019, 7, 20), JAM.getMostRecentCloseDate());
    }

    @Test
    void testGetNumDaysSinceMostRecentClosing() {
        JobApplicationManager JAM = this.createJobApplicationManager();
        LocalDate today = LocalDate.of(2019, 7, 9);
        assertEquals(1, JAM.getNumDaysSinceMostRecentClosing(today));

        LocalDate today1 = LocalDate.of(2019, 6, 30);
        assertEquals(0, JAM.getNumDaysSinceMostRecentClosing(today1));
    }
}