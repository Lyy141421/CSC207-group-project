package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.CompanyJobPosting;
import DocumentManagers.DocumentManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    private LocalDate today = LocalDate.of(2019, 7, 29);

    Company createCompany(String name){
        Company company = new Company(name);
        company.createBranch(name + " Branch", "Toronto");
        createHR(company, name + "HR");
        return company;
    }

    HRCoordinator createHR(Company company, String name) {
        return new HRCoordinator(name, "ABC123", name + " LeagalName",
                name + "@gmail.com", company.getBranches().get(0), today);
    }

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
        Company company = createCompany("Segufix");
        assertTrue(company.getCompanyJobPostings().size() == 0);
        CompanyJobPosting posting1 = new CompanyJobPosting("Title", "Field", "Description",
                new ArrayList<String>(), new ArrayList<String>(), company);
        assertTrue(company.getCompanyJobPostings().size() == 1);
        //todo check if test below is valid
//        BranchJobPosting posting2 = new BranchJobPosting("Title", "Field", "Description",
//                new ArrayList<String>(), new ArrayList<String>(), 1, company.getBranches().get(0),
//                today, today.plusDays(5), today.plusDays(5), 1);
//        company.addCompanyJobPosting(posting1);
//        assertTrue(company.getCompanyJobPostings().size() == 1);
//        company.addCompanyJobPosting(posting2);
//        assertTrue(company.getCompanyJobPostings().size() == 1);
    }

    @Test
    void testgetBranch() {
        Company company = createCompany("Segufix");
        assertNull(company.getBranch("ABC"));
        assertEquals(company.getBranches().get(0), company.getBranch("Segufix Branch"));

    }

    @Test
    void testGetDocumentManager() {
        Company company = createCompany("Segufix");
        assertTrue(company.getDocumentManager() instanceof DocumentManager);
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
                "dudebro@gmail.com", "Toronto", LocalDate.now());
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
        Company company3 = new Company("NotHoraceCorp");
        assertEquals(company1, company2);
        assertNotEquals(company1, company3);
    }
}