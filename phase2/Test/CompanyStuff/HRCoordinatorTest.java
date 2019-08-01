package CompanyStuff;

import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import NotificationSystem.Notification;
import NotificationSystem.NotificationManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HRCoordinatorTest {

    private LocalDate today = LocalDate.of(2019, 7, 29);
    private Company company = new Company("company_a");
    private Branch branch = company.createBranch("BranchName", "E6H1P9");

    BranchJobPosting createPosting(String name){
        return new BranchJobPosting(name, "Sales", "none", new ArrayList<>(),
                new ArrayList<>(), 1, branch, today, today.plusDays(5), today.plusDays(5));
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
        HRCoordinator hrcord = createHR("Phillip");
        hrcord.addJobPosting("Sales Person For Hire", "Jobbing", "A Job", new ArrayList<>(),
                new ArrayList<>(), 3, today, today.plusDays(5), today.plusDays(5));
        hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).createInterviewManager();
        hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().updateJobPostingStatus();
        assertEquals(hrcord.getAllNotifications().size(), 1);
        assertEquals(hrcord.getAllNotifications().get(0).getMessage(),
              "There are no applications in consideration for theSales Person For Hire job posting (id " +
                      hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).getId()
                      + "). It has been automatically set to filled with 0 positions.");
        hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).getInterviewManager().updateJobPostingStatus();
        assertEquals(hrcord.getAllNotifications().size(), 2);
        hrcord.removeAllNotifications();
        assertEquals(hrcord.getAllNotifications().size(), 0);
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
        assertEquals(company.getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0).getTitle(),
                "A Job Title");

    }

    @Test
    void implementJobPosting() {
        HRCoordinator hrcord = createHR("Phillip");
        CompanyJobPosting posting = createCompanyJobPosting();
        hrcord.implementJobPosting(posting, 3, today, today.plusDays(5), today.plusDays(5));
        assertEquals(hrcord.getBranch().getJobPostingManager().getBranchJobPostings().get(0).getTitle(),
                "A Job Title");

    }

    @Test
    void chooseInterviewConfiguration() {
        HRCoordinator hrcord = createHR("Phillip");
        BranchJobPosting posting = createPosting("Posting Name");
        hrcord.addJobPosting("A Job Title", "Jobbing", "A Job", new ArrayList<>(),
                new ArrayList<>(), 3, today, today.plusDays(5), today.plusDays(5));
        ArrayList list = new ArrayList<>();
        list.add(new String[1]);
        list.add(new String[1]);
        list.add(new String[1]);
        posting.createInterviewManager();
        hrcord.chooseInterviewConfiguration(posting,
                list);
        assertEquals(posting.getInterviewManager().getFinalRoundNumber(), 2);
    }
}