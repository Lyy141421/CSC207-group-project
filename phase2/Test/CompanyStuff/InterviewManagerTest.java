package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InterviewManagerTest {

    List createHRCAndTwoInterviewers() {
        Company company = new Company("HoraceCorp");
        Branch branch = new Branch("HQ", "Toronto", company);
        Interviewer interviewer1 = new Interviewer("anne", "ABC", "Anne Mann", "anne@gmail.com",
                branch, "HR", LocalDate.now());
        Interviewer interviewer2 = new Interviewer("bob", "ABC", "Bob Mann", "bob@gmail.com",
                branch, "HR", LocalDate.now());
        HRCoordinator hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch, LocalDate.now());
        return Arrays.asList(hrc, interviewer1, interviewer2);
    }

    InterviewManager createInterviewManagerFromBJPWithTwoApplications(List list) {
        BranchJobPosting branchJobPosting = (BranchJobPosting)list.get(0);
        JobApplication application1 = (JobApplication)list.get(1);
        JobApplication application2 = (JobApplication)list.get(2);
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add(application1);
        applicationsInConsideration.add(application2);
        return new InterviewManager(branchJobPosting, applicationsInConsideration);
    }

    List createBJPWithTwoApplications(HRCoordinator hrc) {
        BranchJobPosting branchJobPosting = hrc.addJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        Applicant applicant1 = new Applicant("steve", "ABC", "Steve Dude", "steve@gmail.com",
                LocalDate.now(), "Toronto");
        Applicant applicant2 = new Applicant("bob", "ABC", "Bob Dude", "bob@gmail.com",
                LocalDate.now(), "London");
        JobApplication jobApplication1 = new JobApplication(applicant1, branchJobPosting, LocalDate.now());
        JobApplication jobApplication2 = new JobApplication(applicant2, branchJobPosting, LocalDate.now());
        return Arrays.asList(branchJobPosting, jobApplication1, jobApplication2);
    }

    @Test
    List testConstructor() {
        List staffList = createHRCAndTwoInterviewers();
        HRCoordinator hrc = (HRCoordinator)staffList.get(0);
        List appList = createBJPWithTwoApplications(hrc);
        InterviewManager interviewManager = createInterviewManagerFromBJPWithTwoApplications(appList);
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add((JobApplication)appList.get(1));
        applicationsInConsideration.add((JobApplication)appList.get(2));
        assert interviewManager.getApplicationsInConsideration().equals(applicationsInConsideration);
        assert interviewManager.getApplicationsRejected().isEmpty();
        assert interviewManager.getInterviewConfiguration().isEmpty();
        return Arrays.asList(interviewManager, hrc, staffList.get(1), staffList.get(2), appList.get(0),
                appList.get(1), appList.get(2));
    }

    @Test
    void testOneRoundInterviewConfigurationOneOnOne() {
        List list = testConstructor();
        InterviewManager interviewManager = (InterviewManager)list.get(0);
        HRCoordinator hrc = (HRCoordinator)list.get(1);
        Interviewer interviewer1 = (Interviewer)list.get(2);
        Interviewer interviewer2 = (Interviewer)list.get(3);
        BranchJobPosting branchJobPosting = (BranchJobPosting)list.get(4);
        JobApplication application1 = (JobApplication)list.get(5);
        JobApplication application2 = (JobApplication)list.get(6);

        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        interviewManager.setInterviewConfiguration(interviewConfiguration);
        interviewManager.setUpOneOnOneInterviews();
        assert interviewer1.getInterviews().size() == 1;
        assert interviewer2.getInterviews().size() == 1;

        Interview interview1 = interviewer1.getInterviews().get(0);
        Interview interview2 = interviewer2.getInterviews().get(0);
        InterviewTime time = new InterviewTime(LocalDate.now().plusDays(5), "9-10 am");
        interviewer1.scheduleInterview(interview1, time);
        interviewer2.scheduleInterview(interview2, time);

        interview2.setResult(application1, false);
        interview1.setResult(application2, true);
        assert interviewManager.currentRoundIsOver();
        interviewManager.reject(application1); //TODO: remove
        assert interviewManager.getApplicationsInConsideration().equals(Collections.singletonList(application2));
        assert interviewManager.getApplicationsRejected().equals(Collections.singletonList(application1));

        interviewManager.hireApplicants(interviewManager.getApplicationsInConsideration());
        assert interviewManager.getBranchJobPosting().isFilled();
        assert application1.getStatus().isArchived();
        assert application2.getStatus().isHired();
    }


}
