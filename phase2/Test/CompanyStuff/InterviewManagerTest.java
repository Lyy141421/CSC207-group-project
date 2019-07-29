package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

public class InterviewManagerTest {

    InterviewManager createInterviewManagerWithTwoApplications() {
        Company company = new Company("HoraceCorp");
        Branch branch = new Branch("HQ", "Toronto", company);
        BranchJobPosting branchJobPosting = new BranchJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, branch, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        ArrayList<JobApplication> applicationsInConsideration = createTwoApplications();
        return new InterviewManager(branchJobPosting, applicationsInConsideration);
    }

    ArrayList<JobApplication> createTwoApplications() {
        Company company = new Company("HoraceCorp");
        Branch branch = company.createBranch("HQ", "M5S2E8");
        HRCoordinator hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch, LocalDate.now());
        BranchJobPosting branchJobPosting = hrc.addJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 2, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        Applicant applicant1 = new Applicant("steve", "ABC", "Steve Dude", "steve@gmail.com",
                LocalDate.now(), "Toronto");
        Applicant applicant2 = new Applicant("bob", "ABC", "Bob Dude", "bob@gmail.com",
                LocalDate.now(), "London");
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        JobApplication jobApplication1 = new JobApplication(applicant1, branchJobPosting, LocalDate.now());
        JobApplication jobApplication2 = new JobApplication(applicant2, branchJobPosting, LocalDate.now());
        applicationsInConsideration.add(jobApplication1);
        applicationsInConsideration.add(jobApplication2);
        return applicationsInConsideration;
    }
//
//    @Test
//    void testConstructor() {
//        InterviewManager interviewManager = createInterviewManagerWithTwoApplications();
//        ArrayList<JobApplication> applicationsInConsideration = createTwoApplications();
//        assert interviewManager.getApplicationsInConsideration().equals(applicationsInConsideration);
//        assert interviewManager.getApplicationsRejected().isEmpty();
//        assert interviewManager.getInterviewConfiguration().isEmpty();
//    }
//
//    @Test
//    void testGetNumOpenPositions() {
//        InterviewManager interviewManager = createInterviewManagerWithTwoApplications();
////        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
////        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "Phone interview"});
////        interviewManager.setInterviewConfiguration(interviewConfiguration);
//        assert interviewManager.getNumOpenPositions() == 2;
//        interviewManager.reject(interviewManager.getApplicationsInConsideration().get(0));
//        assert interviewManager.getNumOpenPositions() == 1;
//    }
}
