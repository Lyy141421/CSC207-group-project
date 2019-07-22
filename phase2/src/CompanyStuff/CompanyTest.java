package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import DocumentManagers.CompanyDocumentManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class CompanyTest {
    public static void main (String[] args) {
        testConstructor();
        testCreateBranch();
        testGetAllApplicationsToCompany();
        testEquals();
    }
    static void testConstructor() {
        Company company = new Company("HoraceCorp");
        assert company.getName().equalsIgnoreCase("HoraceCorp");
        assert company.getBranches().isEmpty();
        assert company.getCompanyJobPostings().isEmpty();
        assert company.getDocumentManager() instanceof CompanyDocumentManager;
    }

    static void testCreateBranch() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        assert company.getBranches().contains(branch);
        assert branch.getName().equalsIgnoreCase("HQ");
        assert branch.getCMA().equalsIgnoreCase("Toronto");
        assert branch.getCompany().equals(company);
    }

    static void testGetAllApplicationsToCompany() {
        // NOTE: do NOT use this as an example of how to properly create branches/companies/HRC;
        // I am doing the bare minimum to make this test work
        Company company = new Company("HoraceCorp");
        Branch branch1 = company.createBranch("HQ", "M5S2E8");
        HRCoordinator hrc1 = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1, LocalDate.now());
        BranchJobPosting branchJobPosting1 = hrc1.addJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(3));
        Applicant applicant = new Applicant("dudebro7", "ABC", "DudeBro Smith",
                "dudebro@gmail.com", LocalDate.now(), "Toronto");
        JobApplication application1 = new JobApplication(applicant, branchJobPosting1, new ArrayList<>(), LocalDate.now());
        branchJobPosting1.addJobApplication(application1);
        Branch branch2 = company.createBranch("South Branch", "N9G0A3");
        HRCoordinator hrc2 = new HRCoordinator("boris", "ABC", "Boris Businessman",
                "boris@gmail.com", branch2, LocalDate.now());
        BranchJobPosting branchJobPosting2 = hrc2.addJobPosting("Best Job", "HR",
                "This is a better job.", new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(3));
        JobApplication application2 = new JobApplication(applicant, branchJobPosting2, new ArrayList<>(),
                LocalDate.now().plusDays(1));
        branchJobPosting2.addJobApplication(application2);
        assert company.getAllApplicationsToCompany(applicant).size() == 2;
        assert company.getAllApplicationsToCompany(applicant).contains(application1);
        assert company.getAllApplicationsToCompany(applicant).contains(application2);
    }

    static void testEquals() {
        Company company1 = new Company("HoraceCorp");
        Company company2 = new Company("HORACECORP");
        assert company1.equals(company2);
    }
}
