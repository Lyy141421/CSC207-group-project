package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantDocumentManagerTest {

    private ApplicantDocumentManager createApplicantDocumentManager() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        return applicant.getDocumentManager();
    }

    Branch createCompanyBranchEmployeesAndJobPostings() {
        Company company1 = new Company("Company");
        Branch branch1A = company1.createBranch("Branch", "Toronto");
        HRCoordinator hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, LocalDate.of(2019, 7, 15));
        new Interviewer("joe", "ABC", "Horace Businessman",
                "horace@gmail.com", branch1A, "field", LocalDate.of(2019, 7, 15));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, Applicant.INACTIVE_DAYS),
                LocalDate.of(2019, 8, 10));
        hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 8, 3),
                LocalDate.of(2019, 8, 10));
        return branch1A;
    }

    @Test
    void testConstructor() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        assertEquals("ApplicantTest", applicantDocumentManager.getFolder().getName());
        assertTrue(applicantDocumentManager.getFolder().exists());
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testContainsFileNoFile() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        File file = new File("test.txt");
        assertFalse(applicantDocumentManager.containsFile(file));
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testContainsFileHasFile() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        File file = new File(applicantDocumentManager.getFolder().getPath() + "/test.txt");
        File file2 = new File(applicantDocumentManager.getFolder().getPath() + "/test2.txt");
        try {
            file.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assertTrue(applicantDocumentManager.containsFile(file));
        assertTrue(applicantDocumentManager.containsFile(new File(applicantDocumentManager.getFolder().getPath() + "/test.txt")));
        assertTrue(applicantDocumentManager.containsFile(file2));
        for (File fileSubmitted : applicantDocumentManager.getFolder().listFiles()) {
            fileSubmitted.delete();
        }
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testAddFilesToAccountEmptyList() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        ArrayList<JobApplicationDocument> files = applicantDocumentManager.addFilesToAccount(new ArrayList<>());
        assertEquals(0, applicantDocumentManager.getFolder().listFiles().length);
        assertTrue(files.isEmpty());
        applicantDocumentManager.getFolder().delete();
    }

    @Test
    void testAddFilesToAccountNonemptyListFilePathNotFromFolder() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        File file1 = new File("./test.txt");
        File file2 = new File("./test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<JobApplicationDocument> files = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file1, file2)));
        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        assertEquals(2, files.size());
        for (File file : applicantDocumentManager.getFolder().listFiles()) {
            assertTrue(file.delete());
        }
        assertTrue(file1.delete());
        assertTrue(file2.delete());
        assertEquals(0, applicantDocumentManager.getFolder().listFiles().length);
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
        // TODO fix
    void testAddFilesToAccountNonemptyListOnePathFromAccountOtherNot() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        File file1 = new File(applicantDocumentManager.getFolder().getPath() + "/test.txt");
        File file2 = new File("./test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<JobApplicationDocument> files = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file1, file2)));
        assertEquals(3, applicantDocumentManager.getFolder().listFiles().length);
        assertEquals(2, files.size());
        assertTrue(file2.delete());
        assertTrue(file1.delete());
        for (File file : applicantDocumentManager.getFolder().listFiles()) {
            assertTrue(file.delete());
        }
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testRemoveFilesFromAccountIfInactiveNoFiles() {
        ApplicantDocumentManager applicantDocumentManager = this.createApplicantDocumentManager();
        applicantDocumentManager.removeFilesFromAccountIfInactive(LocalDate.now());
        assertEquals(0, applicantDocumentManager.getFolder().listFiles().length);
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testRemoveFilesFromAccountIfInactiveTwoFilesWithinApplicant30Days() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        ApplicantDocumentManager applicantDocumentManager = applicant.getDocumentManager();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting1 = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting1, LocalDate.of(2019, 7, 29));
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<JobApplicationDocument> files = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file1, file2)));
        jobApplication.addFiles(files);

        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        applicantDocumentManager.removeFilesFromAccountIfInactive(LocalDate.of(2019, 7, 29).plusDays(29));
        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        assertTrue(file2.delete());
        assertTrue(file1.delete());
        for (File file : applicantDocumentManager.getFolder().listFiles()) {
            assertTrue(file.delete());
        }
        assertTrue(applicantDocumentManager.getFolder().delete());
    }

    @Test
    void testRemoveFilesFromAccountIfInactiveTwoFilesOnExactlyApplicant30Days() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        ApplicantDocumentManager applicantDocumentManager = applicant.getDocumentManager();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting1 = branch.getJobPostingManager().getBranchJobPostings().get(0);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting1, LocalDate.of(2019, 7, 29));
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<JobApplicationDocument> files = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file1, file2)));
        jobApplication.addFiles(files);

        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        applicantDocumentManager.removeFilesFromAccountIfInactive(jobPosting1.getApplicantCloseDate().plusDays(Applicant.INACTIVE_DAYS));
        assertEquals(0, applicantDocumentManager.getFolder().listFiles().length);
        for (File file : applicantDocumentManager.getFolder().listFiles()) {
            assertTrue(file.delete());
        }
        assertTrue(applicantDocumentManager.getFolder().delete());
        file1.delete();
        file2.delete();
    }

    @Test
    void testRemoveFilesFromAccountIfInactiveOneFileWithinOneFileAfter() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        ApplicantDocumentManager applicantDocumentManager = applicant.getDocumentManager();
        Branch branch = this.createCompanyBranchEmployeesAndJobPostings();
        BranchJobPosting jobPosting1 = branch.getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 = branch.getJobPostingManager().getBranchJobPostings().get(1);
        JobApplication jobApplication1 = new JobApplication(applicant, jobPosting1, LocalDate.of(2019, 7, 29));
        JobApplication jobApplication2 = new JobApplication(applicant, jobPosting2, LocalDate.of(2019, 7, 29));
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<JobApplicationDocument> files1 = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file1)));
        ArrayList<JobApplicationDocument> files2 = applicantDocumentManager.addFilesToAccount(new ArrayList<>(Arrays.asList(file2)));
        jobApplication1.addFiles(files1);
        jobApplication2.addFiles(files2);

        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        applicantDocumentManager.removeFilesFromAccountIfInactive(jobPosting1.getApplicantCloseDate().plusDays(Applicant.INACTIVE_DAYS));
        assertEquals(2, applicantDocumentManager.getFolder().listFiles().length);
        applicantDocumentManager.removeFilesFromAccountIfInactive(jobPosting2.getApplicantCloseDate().plusDays(Applicant.INACTIVE_DAYS));
        assertEquals(0, applicantDocumentManager.getFolder().listFiles().length);
        assertTrue(applicantDocumentManager.getFolder().delete());
        file2.delete();
        file1.delete();
    }
}