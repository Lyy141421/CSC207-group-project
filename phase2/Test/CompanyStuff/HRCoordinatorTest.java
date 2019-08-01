package CompanyStuff;

import CompanyStuff.JobPostings.CompanyJobPosting;
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

    CompanyJobPosting createCompanyJobPosting() {
        return new CompanyJobPosting("A Job Title", "Jobbing", "A Job", new ArrayList<>(),
                new ArrayList<>(), company, branch);
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
        HRCoordinator hrcord = createHR("Phillip");
        assertEquals(hrcord.getBranch(), branch);
    }

    @Test
    void setBranch() {
        HRCoordinator hrcord = createHR("Phillip");
        assertEquals(hrcord.getBranch(), branch);
        Branch branch_two = company.createBranch("BranchName2", "E6H1P9");
        hrcord.setBranch(branch_two);
        assertEquals(hrcord.getBranch(), branch_two);
        assertNotEquals(hrcord.getBranch(), branch);
    }

    @Test
    void addJobPosting() {
        HRCoordinator hrcord = createHR("Phillip");
        hrcord.addJobPosting("A Job Title", "Jobbing", "A Job", new ArrayList<>(),
                new ArrayList<>(), 3, today, today.plusDays(5), today.plusDays(5));
        assertEquals(hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).getTitle(),
                "A Job Title");

    }

    @Test
    void implementJobPosting() {

    }

    @Test
    void chooseInterviewConfiguration() {

    }
}