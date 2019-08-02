package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import ApplicantStuff.JobApplicationDocument;
import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.HRCoordinator;
import CompanyStuff.JobPostings.BranchJobPosting;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDocumentManagerTest {

    private Company createCompany() {
        return new Company("CompanyTest");
    }

    @Test
    void testConstructor() {
        Company company = new Company("CompanyTest");
        CompanyDocumentManager companyDocumentManager = (CompanyDocumentManager) new DocumentManagerFactory().createDocumentManager(company);
        assertEquals(company.getName(), companyDocumentManager.getFolder().getName());
        companyDocumentManager.getFolder().delete();
    }

    @Test
    void testCreateBranchFolder() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        assertTrue(branchFolder.exists());
        assertTrue(branchFolder.isDirectory());
        assertEquals(0, branchFolder.listFiles().length);
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testCreateBranchJobPostingFolder() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertTrue(branchJobPostingFolder.exists());
        assertTrue(branchJobPostingFolder.isDirectory());
        assertEquals(0, branchJobPostingFolder.listFiles().length);
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFolderForJobPosting() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertEquals(branchJobPostingFolder, company.getDocumentManager().getFolderForJobPosting(jobPosting));
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testCreateApplicationFolder() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        assertTrue(applicationFolder.exists());
        assertTrue(applicationFolder.isDirectory());
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFolderForJobApplicationNoJobPostingForJobApp() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertNull(company.getDocumentManager().getFolderForJobApplication(jobApplication));
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFolderForJobApplicationHasJobPostingNotJobApp() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertNull(company.getDocumentManager().getFolderForJobApplication(jobApplication));
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFolderForJobApplicationHasApplicantDifferentJobPosting() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        BranchJobPosting jobPosting2 = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        JobApplication jobApplication2 = new JobApplication(applicant, jobPosting2, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        assertNull(company.getDocumentManager().getFolderForJobApplication(jobApplication2));
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFolderForJobApplicationHasJobApp() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        assertEquals(applicationFolder, company.getDocumentManager().getFolderForJobApplication(jobApplication));
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFilesForJobApplicationNoPostingForJobApp() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertEquals(0, company.getDocumentManager().getFilesForJobApplication(jobApplication).length);
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFilesForJobApplicationHasJobPostingNotJobApp() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        assertEquals(0, company.getDocumentManager().getFilesForJobApplication(jobApplication).length);
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFilesForJobApplicationHasApplicantDifferentJobPosting() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        BranchJobPosting jobPosting2 = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        JobApplication jobApplication2 = new JobApplication(applicant, jobPosting2, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        assertEquals(0, company.getDocumentManager().getFilesForJobApplication(jobApplication2).length);
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFilesForJobApplicationHasJobAppEmptyFolder() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        assertEquals(0, company.getDocumentManager().getFilesForJobApplication(jobApplication).length);
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testGetFilesForJobApplicationHasJobAppNonEmptyFolder() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        File file = new File(applicationFolder.getPath() + "/test.txt");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assertEquals(1, company.getDocumentManager().getFilesForJobApplication(jobApplication).length);
        file.delete();
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testAddFilesForJobApplicationNoJobAppFolder() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        File file3 = new File("test3.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        company.getDocumentManager().addFilesForJobApplication(jobApplication, new ArrayList<>(Arrays.asList(file1, file2, file3)));
        file1.delete();
        file2.delete();
        file3.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testAddFilesForJobApplicationHasJobAppFolder() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        company.getDocumentManager().createApplicationFolder(branchJobPostingFolder, jobApplication);
        File applicationFolder = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        File file3 = new File("test3.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        company.getDocumentManager().addFilesForJobApplication(jobApplication, new ArrayList<>(Arrays.asList(file1, file2, file3)));
        assertEquals(3, company.getDocumentManager().getFilesForJobApplication(jobApplication).length);
        assertEquals(3, applicationFolder.listFiles().length);
        assertTrue(applicationFolder.listFiles()[0].getName().startsWith("test"));
        file1.delete();
        file2.delete();
        file3.delete();
        for (File file : applicationFolder.listFiles()) {
            file.delete();
        }
        applicationFolder.delete();
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }


    @Test
    void testApplicationDocumentsTransferredNoJobPosting() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        assertFalse(company.getDocumentManager().applicationDocumentsTransferred(jobPosting));
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testApplicationDocumentsTransferredHasJobPosting() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        assertTrue(company.getDocumentManager().applicationDocumentsTransferred(jobPosting));
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testTransferApplicationDocumentsNoApplications() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        company.getDocumentManager().transferApplicationDocuments(jobPosting);
        assertEquals(0, company.getDocumentManager().getFolderForJobPosting(jobPosting).listFiles().length);
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
    }

    @Test
    void testTransferApplicationDocumentsHasApplications() {
        Company company = this.createCompany();
        Branch branch = company.createBranch("BranchTest", "L4B4P8");
        HRCoordinator hrc = new HRCoordinator("hr", "password", "name", "email", branch,
                LocalDate.now());
        BranchJobPosting jobPosting = hrc.addJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30),
                LocalDate.of(2019, 8, 10));
        company.getDocumentManager().createBranchJobPostingFolder(jobPosting);
        File branchFolder = new File(company.getDocumentManager().getFolder().getPath() + "/" + branch.getName());
        File branchJobPostingFolder = new File(branchFolder.getPath() + "/" + jobPosting.getId() + "_" + jobPosting.getTitle());
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        Applicant applicant2 = new Applicant("ApplicantTest2", "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "Toronto");
        JobApplication jobApplication = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 29));
        JobApplication jobApplication2 = new JobApplication(applicant2, jobPosting, LocalDate.of(2019, 7, 29));

        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JobApplicationDocument jobAppDoc1 = new JobApplicationDocument(file1);
        JobApplicationDocument jobAppDoc2 = new JobApplicationDocument(file2);
        ArrayList<JobApplicationDocument> files = new ArrayList<>(Arrays.asList(jobAppDoc1, jobAppDoc2));
        jobApplication.addFiles(files);
        jobApplication2.addFiles(files);

        company.getDocumentManager().transferApplicationDocuments(jobPosting);
        assertEquals(2, company.getDocumentManager().getFolderForJobPosting(jobPosting).listFiles().length);
        assertEquals(2, branchJobPostingFolder.listFiles().length);
        File jobAppFolder1 = new File(branchJobPostingFolder.getPath() + "/" + applicant.getUsername());
        File jobAppFolder2 = new File(branchJobPostingFolder.getPath() + "/" + applicant2.getUsername());
        assertTrue(jobAppFolder1.exists());
        assertEquals(2, jobAppFolder1.listFiles().length);
        assertTrue(jobAppFolder2.exists());
        assertEquals(2, jobAppFolder2.listFiles().length);
        for (File file : jobAppFolder1.listFiles()) {
            file.delete();
        }
        for (File file : jobAppFolder2.listFiles()) {
            file.delete();
        }
        for (File file : branchJobPostingFolder.listFiles()) {
            file.delete();
        }
        branchJobPostingFolder.delete();
        branchFolder.delete();
        company.getDocumentManager().getFolder().delete();
        file1.delete();
        file2.delete();
    }
}