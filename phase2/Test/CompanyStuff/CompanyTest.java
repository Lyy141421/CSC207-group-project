package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import DocumentManagers.DocumentManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
    void testConstructor() {
        Company company = new Company("HoraceCorp");
        assertTrue(company.getName().equalsIgnoreCase("HoraceCorp"));
        assertTrue(company.getBranches().isEmpty());
        assertTrue(company.getCompanyJobPostings().isEmpty());
        assertTrue(company.getDocumentManager() instanceof DocumentManager);
    }

    @Test
    void testAddCompanyJobPosting() {
    }

    @Test
    void testGetDocumentManager() {
    }

    @Test
    void testAddBranch() {
    }

    @Test
    void testCreateBranch() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        assertTrue(company.getBranches().contains(branch));
        assertTrue(branch.getName().equalsIgnoreCase("HQ"));
        assert branch.getCma().equalsIgnoreCase("Toronto");
        assertEquals(branch.getCompany(), company);
    }

    @Test
    void testGetAllApplicationsToCompany() {
        // NOTE: do NOT use this as an example of how to properly create branches/companies/HRC;
        // I am doing the bare minimum to make this test work
        Company company = new Company("HoraceCorp");
        Branch branch1 = company.createBranch("HQ", "M5S2E8");
        HRCoordinator hrc1 = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1, LocalDate.now());
        BranchJobPosting branchJobPosting1 = hrc1.addJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        Applicant applicant = new Applicant("dudebro7", "ABC", "DudeBro Smith",
                "dudebro@gmail.com", LocalDate.now(), "Toronto");
        JobApplication application1 = new JobApplication(applicant, branchJobPosting1, LocalDate.now());
        Branch branch2 = company.createBranch("South Branch", "N9G0A3");
        HRCoordinator hrc2 = new HRCoordinator("boris", "ABC", "Boris Businessman",
                "boris@gmail.com", branch2, LocalDate.now());
        BranchJobPosting branchJobPosting2 = hrc2.addJobPosting("Best Job", "HR",
                "This is a better job.", new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));
        JobApplication application2 = new JobApplication(applicant, branchJobPosting2,
                LocalDate.now().plusDays(1));
        assertEquals(2, company.getAllApplicationsToCompany(applicant).size());
        assertTrue(company.getAllApplicationsToCompany(applicant).contains(application1));
        assertTrue(company.getAllApplicationsToCompany(applicant).contains(application2));
    }

    @Test
    void equals() {
        Company company1 = new Company("HoraceCorp");
        Company company2 = new Company("HORACECORP");
        assertEquals(company1, company2);
    }
}