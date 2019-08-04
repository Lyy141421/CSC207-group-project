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

class JobApplicationTest {

    BranchJobPosting createJobPosting() {
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        return new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
    }

    JobApplication createJobApp() {
        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", "Toronto", LocalDate.of(2019, 7, 20));
        BranchJobPosting jobPosting = this.createJobPosting();
        return new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
    }

    @Test
    void testConstructor() {
        JobApplication jobApp = this.createJobApp();
        assertEquals("jsmith", jobApp.getApplicant().getUsername());
        assertEquals("Title", jobApp.getJobPosting().getTitle());
        assertTrue(jobApp.getFilesSubmitted().isEmpty());
        assertEquals(jobApp.getApplicationDate(), LocalDate.of(2019, 7, 21));
        assertTrue(jobApp.getReferences().isEmpty());
        assertTrue(jobApp.getInterviews().isEmpty());
        assertNotNull(jobApp.getStatus());
    }

    @Test
    void testAddFiles() {
        JobApplication jobApp = this.createJobApp();
        JobApplicationDocument jobAppDoc1 = new JobApplicationDocument(new File("./sample.txt"));
        JobApplicationDocument jobAppDoc2 = new JobApplicationDocument(new File("./sample.txt"));
        JobApplicationDocument jobAppDoc3 = new JobApplicationDocument(new File("./sample.txt"));
        jobApp.addFiles(new ArrayList<>(Arrays.asList(jobAppDoc1, jobAppDoc2, jobAppDoc3)));

        assertEquals(3, jobApp.getFilesSubmitted().size());
    }

    @Test
    void testAddReferences() {
        JobApplication jobApp = this.createJobApp();
        Reference reference1 = new Reference("bob@gmail.com", LocalDate.of(2019, 8, 1));
        Reference reference2 = new Reference("bob2@gmail.com", LocalDate.of(2019, 8, 1));
        jobApp.addReferences(new ArrayList<>(Arrays.asList(reference1, reference2)));

        assertEquals(2, jobApp.getReferences().size());
        assertEquals("bob@gmail.com", jobApp.getReferences().get(0).getEmail());
        assertEquals("bob2@gmail.com", jobApp.getReferences().get(1).getEmail());
        assertEquals(1, reference1.getJobAppsForReference().size());
        assertEquals(1, reference2.getJobAppsForReference().size());
        assertEquals("jsmith", reference1.getJobAppsForReference().get(0).getApplicant().getUsername());
        assertEquals("jsmith", reference2.getJobAppsForReference().get(0).getApplicant().getUsername());
    }

    @Test
    void testRemoveAppFromAllReferencesMultipleReferences() {
        JobApplication jobApp = this.createJobApp();
        Reference reference1 = new Reference("bob@gmail.com", LocalDate.of(2019, 8, 1));
        Reference reference2 = new Reference("bob2@gmail.com", LocalDate.of(2019, 8, 1));
        jobApp.addReferences(new ArrayList<>(Arrays.asList(reference1, reference2)));
        jobApp.removeAppFromAllReferences();

        assertTrue(reference1.getJobAppsForReference().isEmpty());
        assertTrue(reference2.getJobAppsForReference().isEmpty());
        assertEquals(2, jobApp.getReferences().size());
    }

    @Test
    void testIsArchived() {
        JobApplication jobApp = this.createJobApp();
        assertFalse(jobApp.isArchived());

        jobApp.getStatus().setArchived();
        assertTrue(jobApp.isArchived());
    }

    @Test
    void testGetLastInterview() {
        JobApplication jobApp = this.createJobApp();
        assertNull(jobApp.getLastInterview());

        BranchJobPosting jobPosting = jobApp.getJobPosting();
        jobPosting.createInterviewManager();
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 11), InterviewTime.ELEVEN_AM_TO_NOON));
        interview.setResult(jobApp, true);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        Interview interview2 = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 20), InterviewTime.ELEVEN_AM_TO_NOON));
        interview2.setResult(jobApp, true);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        Interview interview3 = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview3.setTime(new InterviewTime(LocalDate.of(2019, 9, 13), InterviewTime.ELEVEN_AM_TO_NOON));
        interview3.setResult(jobApp, true);

        assertNotNull(jobApp.getLastInterview());
        assertEquals(LocalDate.of(2019, 9, 13), jobApp.getLastInterview().getTime().getDate());
    }

    @Test
    void testAddInterview() {
        JobApplication jobApp = this.createJobApp();
        BranchJobPosting jobPosting = jobApp.getJobPosting();
        jobPosting.createInterviewManager();
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();

        assertEquals(1, jobApp.getInterviews().size());
        assertEquals("Interviewer", jobApp.getInterviews().get(0).getInterviewCoordinator().getUsername());
        jobApp.getLastInterview().setResult(jobApp, true);

        new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        assertEquals(2, jobApp.getInterviews().size());
        assertEquals("Interviewer", jobApp.getInterviews().get(0).getInterviewCoordinator().getUsername());
        assertEquals("Interviewer2", jobApp.getInterviews().get(1).getInterviewCoordinator().getUsername());

    }
}