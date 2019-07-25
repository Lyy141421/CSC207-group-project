package CompanyStuff;

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
    void getUsername() {
    }

    @Test
    void getPassword() {
    }

    @Test
    void getLegalName() {
    }

    @Test
    void getEmail() {
    }

    @Test
    void getDateCreated() {
    }

    @Test
    void getNotificationManager() {
    }

    @Test
    void setUsername() {
    }

    @Test
    void setEmail() {
    }

    @Test
    void update() {
    }

    @Test
    void addNotification() {
    }

    @Test
    void removeNotification() {
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
//    void testConstructor() {
//        Interviewer interviewer = this.createInterviewer("Phillip");
//
//        assertEquals(interviewer.getUsername(), "Phillip");
//        assertEquals(interviewer.getPassword(), "ABC123");
//        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
//        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
//        assertEquals(interviewer.getDateCreated(), LocalDate.of(2019, 7, 29));
//
//        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
//        assertEquals(interviewer.getBranch().getName(), "BranchName");
//        assertEquals(interviewer.getBranch().getCma(), "Skiff Lake");
//        assertEquals(interviewer.getField().getCma(), "Sales");
//    }
//
//
//}