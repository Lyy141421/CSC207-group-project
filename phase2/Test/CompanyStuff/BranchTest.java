package CompanyStuff;

import CompanyStuff.Branch;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BranchTest {

    @Test
    void testConstructor() {
        Company company = new Company("HoraceCorp");
        Branch branch = new Branch("HQ", "Toronto", company);
        assert branch.getName().equalsIgnoreCase("HQ");
        assert branch.getCMA().equalsIgnoreCase("Toronto");
        assert branch.getCompany().equals(company);
        assert branch.getHrCoordinators().isEmpty();
        assert branch.getFieldToInterviewers().isEmpty();
        assert branch.getJobPostingManager() instanceof BranchJobPostingManager;
        assert company.getBranches().contains(branch);
    }

    @Test
    void testEquals() {
        Company company1 = new Company("HoraceCorp");
        Company company2 = new Company("BorisCorp");
        Branch branch1 = new Branch("HQ", "Toronto", company1);
        Branch branch2 = new Branch("HQ", "Toronto", company1);
        Branch branch3 = new Branch("South Branch", "Windsor", company1);
        Branch branch4 = new Branch("HQ", "Toronto", company2);
        assert branch1.equals(branch2);
        assert !branch1.equals(branch3);
        assert !branch1.equals(branch4);
    }

    @Test
    void testFindInterviewerByField() {
        // Using default Interview constructor for efficiency; don't create objects like this
        Company company = new Company("HoraceCorp");
        Branch branch = new Branch("HQ", "Toronto", company);
        Interviewer interviewer1 = new Interviewer("bob", "ABC", "Bob Mann", "bob@gmail.com",
                branch, "HR", LocalDate.now());
        Interviewer interviewer2 = new Interviewer("anne", "ABC", "Anne Mann", "anne@gmail.com",
                branch, "HR", LocalDate.now());
        Interview interview1 = new Interview();
        Interview interview2 = new Interview();
        Interview interview3 = new Interview();
        interviewer1.addInterview(interview1);
        interviewer2.addInterview(interview2);
        interviewer2.addInterview(interview3);
        assert branch.findInterviewerByField("HR").equals(interviewer1);
    }
}
