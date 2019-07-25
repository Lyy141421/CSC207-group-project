package CompanyStuff;

import NotificationSystem.Notification;
import NotificationSystem.NotificationManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InterviewerTest {

    Company company_a = new Company("company_a");
    Branch branch_a = company_a.createBranch("BranchName", "Skiff Lake");


    Interviewer createInterviewer (String username){
        return new Interviewer(username, "ABC123", username + "'s Real Name", username + "@gmail.com", branch_a,
                "Sales", LocalDate.of(2019, 7, 29));
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
        assertEquals(interviewer.getBranch().getCma(), "Skiff Lake");
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
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
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
        interviewer.removeNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
    }

    @Test
    void equals1() {
    }

    @Test
    void hashCode1() {
    }

    @Test
    void getBranch() {
    }

    @Test
    void getField() {
    }

    @Test
    void getInterviews() {
    }

    @Test
    void setField() {
    }

    @Test
    void setBranch() {
    }

    @Test
    void setInterviews() {
    }

    @Test
    void getEarliestTimeAvailableForNewInterview() {
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