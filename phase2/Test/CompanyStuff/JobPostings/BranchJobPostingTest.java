package CompanyStuff.JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

public class BranchJobPostingTest {

    @Test
    void testConstructor() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        CompanyJobPosting companyJobPosting = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        BranchJobPosting posting = new BranchJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, branch, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4), companyJobPosting.getId());
        assert posting.getTitle().equalsIgnoreCase("Test Job");
        assert posting.getField().equalsIgnoreCase("HR");
        assert posting.getDescription().equalsIgnoreCase("This is a job.");
        assert posting.getRequiredDocuments().isEmpty();
        assert posting.getTags().isEmpty();
        assert posting.getNumPositions() == 1;
        assert posting.getBranch().equals(branch);
        assert posting.getPostDate().equals(LocalDate.now());
        assert posting.getApplicantCloseDate().equals(LocalDate.now().plusDays(3));
        assert posting.getReferenceCloseDate().equals(LocalDate.now().plusDays(4));
        assert !posting.isFilled();
        assert posting.getJobApplications().isEmpty();
        assert branch.getJobPostingManager().getBranchJobPostings().contains(posting);
    }

    @Test
    void testEquals() {
        Company company = new Company("HoraceCorp");
        Branch branch1 = company.createBranch("HQ", "M5S2E8");
        Branch branch2 = company.createBranch("South Branch", "N9G0A3");
        HRCoordinator hrc1 = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1, LocalDate.now());
        HRCoordinator hrc2 = new HRCoordinator("boris", "ABC", "Boris Businessman",
                "boris@gmail.com", branch2, LocalDate.now());
        CompanyJobPosting companyJobPosting = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        BranchJobPosting posting1 = new BranchJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, branch1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4), companyJobPosting.getId());
        BranchJobPosting posting2 = new BranchJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, branch1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4), companyJobPosting.getId());
        CompanyJobPosting posting3 = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        BranchJobPosting posting4 = hrc1.implementJobPosting(posting3, 1, LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));
        BranchJobPosting posting5 = hrc2.implementJobPosting(posting3, 1, LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));
        assert !posting1.equals(posting2);
        assert !posting3.equals(posting4); // Actually tests CompanyJobPosting.equals but BJP equals would auto-fail it
        assert !posting4.equals(posting5);
    }
}
