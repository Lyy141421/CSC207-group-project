package CompanyStuff;

import NotificationSystem.NotificationManager;
import UsersAndJobObjects.JobPosting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HRCoordinatorTest {

    private LocalDate today = LocalDate.of(2019, 7, 29);
    private Company company = new Company("company_a");
    private Branch branch = company.createBranch("BranchName", "E6H1P9");

    JobPosting createJobPosting(String id) {
        return new JobPosting(id);
    }

    HRCoordinator createHR(String name) {
        return new HRCoordinator(name, "ABC123", name + " LeagalName",
                 name + "@gmail.com", branch, today);
    }

    @Test
    void constructor() {
        HRCoordinator hrcord = createHR("Phillip");
        assertEquals(hrcord.getUsername(), "Phillip");
        assertEquals(hrcord.getPassword(), "ABC123");
        assertEquals(hrcord.getLegalName(), "Phillip LeagalName");
        assertEquals(hrcord.getEmail(), "Phillip@gmail.com");
        assertEquals(hrcord.getDateCreated(), today);
        assertEquals(hrcord.getBranch(), branch);
        assertNotNull(hrcord.getNotificationManager());

    }

    @Test
    void notificationTest() {
        //todo test the whole system here for HR
    }

    @Test
    void getBranch() {
    }

    @Test
    void setBranch() {
    }

    @Test
    void addJobPosting() {
    }

    @Test
    void implementJobPosting() {
    }

    @Test
    void chooseInterviewConfiguration() {
    }
}