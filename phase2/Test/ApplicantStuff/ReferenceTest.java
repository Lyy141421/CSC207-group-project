package ApplicantStuff;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceTest {

    Branch createCompanyBranchEmployeesAndJobPostings() {
        Company company1 = new Company("Company");
        Branch branch1A = company1.createBranch("Branch", "Toronto");
        HRCoordinator hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, LocalDate.of(2019, 7, 15));
        new Interviewer("joe", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, "field", LocalDate.of(2019, 7, 15));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 8, 3),
                LocalDate.of(2019, 8, 10));
        return branch1A;
    }

    Applicant createApplicant() {
        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        return applicant;
    }

    JobApplication createJobApplication(Applicant applicant, BranchJobPosting jobPosting) {
        return new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));
    }

    Reference createReference() {
        return new Reference("bob@gmail.com", LocalDate.of(2019, 8, 1));
    }

    @Test
    void testConstructor() {
        Reference reference = this.createReference();
        assertTrue(reference.getJobAppsForReference().isEmpty());
        assertEquals(reference.getEmail(), reference.getUsername());
    }

    @Test
    void testAddJobApplication() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        reference.addJobApplication(jobApp);
        assertEquals(1, reference.getJobAppsForReference().size());
        assertEquals(applicant, jobApp.getApplicant());
    }

    @Test
    void testRemoveJobApplication() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        assertEquals(2, reference.getJobAppsForReference().size());
        reference.removeJobApplication(jobApp);
        assertEquals(1, reference.getJobAppsForReference().size());
        reference.removeJobApplication(jobApp2);
        assertTrue(reference.getJobAppsForReference().isEmpty());
        reference.removeJobApplication(jobApp2);
        assertTrue(reference.getJobAppsForReference().isEmpty());
    }

    @Test
    void testRemoveJobPostingNoJobPosting() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Reference reference = this.createReference();
        reference.removeJobPosting(jobPosting);
        reference.removeJobPosting(jobPosting2);
    }

    @Test
    void testRemoveJobPostingOneJobApp() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        reference.addJobApplication(jobApp);
        assertEquals(1, reference.getJobAppsForReference().size());
        reference.removeJobPosting(jobPosting);
        assertTrue(reference.getJobAppsForReference().isEmpty());
    }

    @Test
    void testRemoveJobPostingMultipleJobAppsOneJobPosting() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting);
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        assertEquals(2, reference.getJobAppsForReference().size());
        reference.removeJobPosting(jobPosting);
        assertTrue(reference.getJobAppsForReference().isEmpty());
    }

    @Test
    void testRemoveJobPostingMixedJobPostings() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        assertEquals(2, reference.getJobAppsForReference().size());
        reference.removeJobPosting(jobPosting);
        assertEquals(1, reference.getJobAppsForReference().size());
        reference.removeJobPosting(jobPosting2);
        assertTrue(reference.getJobAppsForReference().isEmpty());
    }

    @Test
    void testGetJobPostingsNoJobPostings() {
        Reference reference = this.createReference();
        assertTrue(reference.getJobPostings().isEmpty());
    }

    @Test
    void testGetJobPostingsOneJobPostingMultipleApps() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting);
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        assertEquals(1, reference.getJobPostings().size());
        assertEquals(jobPosting, reference.getJobPostings().get(0));
    }

    @Test
    void testGetJobPostingsMultipleJobPostings() {
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        Reference reference = this.createReference();
        Applicant applicant = this.createApplicant();
        JobApplication jobApp = this.createJobApplication(applicant, jobPosting);
        JobApplication jobApp2 = this.createJobApplication(applicant, jobPosting2);
        reference.addJobApplication(jobApp);
        reference.addJobApplication(jobApp2);
        assertEquals(2, reference.getJobPostings().size());
        assertTrue(reference.getJobPostings().contains(jobPosting));
        assertTrue(reference.getJobPostings().contains(jobPosting2));
    }
}