package CompanyStuff.JobPostings;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CompanyJobPostingTest {

    @Test
    void testConstructor() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        CompanyJobPosting posting = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        assert posting.getTitle().equalsIgnoreCase("Test Job");
        assert posting.getField().equalsIgnoreCase("HR");
        assert posting.getDescription().equalsIgnoreCase("This is a job.");
        assert posting.getRequiredDocuments().isEmpty();
        assert posting.getTags().isEmpty();
        assert posting.getCompany().equals(company);
        assert posting.getBranches().size() == 0;
        assert company.getCompanyJobPostings().contains(posting);
    }

    @Test
    void testEquals() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        CompanyJobPosting posting1 = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        CompanyJobPosting posting2 = new CompanyJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), company);
        assert !posting1.equals(posting2);
    }
}
