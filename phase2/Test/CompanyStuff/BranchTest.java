package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import CompanyStuff.JobPostings.BranchJobPostingManager;
import CompanyStuff.JobPostings.CompanyJobPosting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class BranchTest {

    @Test
    void testConstructor() {
        Company company = new Company("HoraceCorp");
        Branch branch = new Branch("HQ", "Toronto", company);
        assert branch.getName().equalsIgnoreCase("HQ");
        assert branch.getCma().equalsIgnoreCase("Toronto");
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

        Applicant applicant = new Applicant("jsmith", "password", "John Smith",
                "john_smith@gmail.com", "Toronto", LocalDate.of(2019, 7, 20));
        CompanyJobPosting companyJobPosting = new CompanyJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10),
                companyJobPosting.getId());
        jobPosting.createInterviewManager();
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person"});
        jobPosting.getInterviewManager().setInterviewConfiguration(interviewConfiguration);
        JobApplication jobApp = new JobApplication(applicant, jobPosting, LocalDate.of(2019, 7, 21));

        new Interview(jobApp, interviewer1, jobPosting.getInterviewManager());
        new Interview(jobApp, interviewer2, jobPosting.getInterviewManager());
        new Interview(jobApp, interviewer2, jobPosting.getInterviewManager());
        assert branch.findInterviewerByField("HR").equals(interviewer1);
    }
}
