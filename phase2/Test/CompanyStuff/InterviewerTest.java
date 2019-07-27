package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import NotificationSystem.Notification;
import NotificationSystem.NotificationManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InterviewerTest {

    Company company_a = new Company("company_a");
    Branch branch_a = company_a.createBranch("BranchName", "E6H1P9");


    Branch createBranch (String name){
        return company_a.createBranch(name, "Skiff Lake");
    }

    Interviewer createInterviewer (String username){
        return new Interviewer(username, "ABC123", username + "'s Real Name", username + "@gmail.com", branch_a,
                "Sales", LocalDate.of(2019, 7, 29));
    }

    Applicant createApplicant(String name) {
        return new Applicant(name, "ABC123", name + " Lastname",
                name + "@gmail.com", LocalDate.of(2019, 7, 29), "Toronto");
    }

    @Test
    void testConstructor() {
        Interviewer interviewer = this.createInterviewer("Phillip");

        assertEquals(interviewer.getUsername(), "Phillip");
        assertEquals(interviewer.getPassword(), "ABC123");
        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
        assertEquals(interviewer.getDateCreated(), LocalDate.of(2019, 7, 29));

        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
        assertEquals(interviewer.getBranch().getName(), "BranchName");
        assertEquals(interviewer.getBranch().getCma(), "Fredericton");
        assertEquals(interviewer.getField(), "Sales");
    }

    @Test
    void getUsername() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getUsername(), "Phillip");
    }

    @Test
    void getPassword() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getPassword(), "ABC123");
    }

    @Test
    void getLegalName() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
    }

    @Test
    void getEmail() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
    }

    @Test
    void getDateCreated() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getDateCreated(), LocalDate.of(2019, 7, 29));
    }

    @Test
    void getNotificationManager() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
    }

    @Test
    void setUsername() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setUsername("Not Phillip");
        assertEquals(interviewer.getUsername(), "Not Phillip");
    }

    @Test
    void setEmail() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setEmail("Phillip.Soetebeer@gmail.com");
        assertEquals(interviewer.getEmail(), "Phillip.Soetebeer@gmail.com");
    }

    @Test
    void update() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.update(new Notification("TestN", "TestN text"));
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
        interviewer.update(5);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
    }

    @Test
    void getAllNotifications() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.update(new Notification("TestN", "TestN text"));
        interviewer.update(new Notification("TestN2", "TestN text2"));
        assertTrue(interviewer.getAllNotifications().size() == 2);
    }

    @Test
    void addNotification() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.addNotification(new Notification("TestN", "TestN text"));
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
    }

    @Test
    void removeNotification() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        Notification notification = new Notification("TestN", "TestN text");
        interviewer.addNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
        interviewer.removeNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
    }

    @Test
    void equals1() {
    }

    @Test
    void hashCode1() {
    }

    @Test
    void getBranch() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getBranch() instanceof Branch);
        assertEquals(interviewer.getBranch().getName(), "BranchName");
    }

    @Test
    void getField() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getField(), "Sales");
    }

    @Test
    void getInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getInterviews() instanceof ArrayList);
    }

    @Test
    void setField() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setField("Quality Assurance");
        assertEquals(interviewer.getField(), "Quality Assurance");
    }

    @Test
    void setBranch() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setBranch(createBranch("Branch 2.0"));
        assertEquals(interviewer.getBranch().getName(), "Branch 2.0");
    }

    @Test
    void setInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        BranchJobPosting jobPosting = new BranchJobPosting("", "", "", new ArrayList<>(),
                new ArrayList<>(), 1, branch_a, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));
        JobApplication jobApp = new JobApplication(new Applicant("", "", "", "", LocalDate.now(), ""), jobPosting, LocalDate.of(2020, 1, 3));
        lst.add(new Interview(jobApp, interviewer, jobPosting.getInterviewManager()));
        interviewer.setInterviews(lst);
        assertTrue(interviewer.getInterviews().size() == 1);
    }

    @Test
    void getEarliestTimeAvailableForNewInterview() { //todo test this
    }

    @Test
    void findJobAppById() {
    }

    @Test
    void isAvailable() {
    }

    @Test
    void isAvailable1() {
    }

    @Test
    void getFirstDateAvailableOnOrAfterDate() {
    }

    @Test
    void getTimeSlotsFilledOnDate() {
    }

    @Test
    void getTimeSlotsAvailableOnDate() {
    }

    @Test
    void removeInterview() {
    }

    @Test
    void scheduleInterview() {
    }

    @Test
    void getUnscheduledInterviews() {
    }

    @Test
    void getScheduledUpcomingInterviews() {
    }

    @Test
    void getAllIncompleteInterviews() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccurred() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccuredAsCoordinator() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccurredNotAsCoordinator() {
    }

    @Test
    void getListOfIntervieweeJobApplications() {
    }

    @Test
    void getDisplayedProfileCategories() {
    }

    @Test
    void getDisplayedProfileInformation() {
    }

    @Test
    void addInterview() {
    }
}