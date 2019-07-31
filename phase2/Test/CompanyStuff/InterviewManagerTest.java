package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

public class InterviewManagerTest {
    Company company = new Company("HoraceCorp");
    Branch branch = new Branch("HQ", "Toronto", company);
    HRCoordinator hrc;
    Interviewer interviewer1;
    Interviewer interviewer2;
    Applicant applicant1;
    Applicant applicant2;
    Applicant applicant3;
    BranchJobPosting branchJobPosting1;
    JobApplication application1;
    JobApplication application2;
    JobApplication application3;
    InterviewManager interviewManager;

    void createHRCAndTwoInterviewers() {
        this.hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch, LocalDate.now());
        this.interviewer1 = new Interviewer("anne", "ABC", "Anne Mann", "anne@gmail.com",
                branch, "HR", LocalDate.now());
        this.interviewer2 = new Interviewer("bob", "ABC", "Bob Mann", "bob@gmail.com",
                branch, "HR", LocalDate.now());
    }

    InterviewManager createInterviewManagerFromBJPWithTwoApplications() {
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add(this.application1);
        applicationsInConsideration.add(this.application2);
        return new InterviewManager(this.branchJobPosting1, applicationsInConsideration);
    }

    void createBJPWithTwoApplications(HRCoordinator hrc) {
        this.branchJobPosting1 = hrc.addJobPosting("Test Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        this.applicant1 = new Applicant("steve", "ABC", "Steve Dude", "steve@gmail.com",
                LocalDate.now(), "Toronto");
        this.applicant2 = new Applicant("bob", "ABC", "Bob Dude", "bob@gmail.com",
                LocalDate.now(), "London");
        this.application1 = new JobApplication(applicant1, branchJobPosting1, LocalDate.now());
        this.application2 = new JobApplication(applicant2, branchJobPosting1, LocalDate.now());
    }

    @Test
    InterviewManager testConstructor() {
        createHRCAndTwoInterviewers();
        createBJPWithTwoApplications(this.hrc);
        InterviewManager interviewManager = createInterviewManagerFromBJPWithTwoApplications();
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add(this.application1);
        applicationsInConsideration.add(this.application2);
        assert interviewManager.getApplicationsInConsideration().equals(applicationsInConsideration);
        assert interviewManager.getApplicationsRejected().isEmpty();
        assert interviewManager.getInterviewConfiguration().isEmpty();
        return interviewManager;
    }

    @Test
    void testOneRoundInterviewConfigurationOneOnOne() {
        interviewManager = testConstructor();

        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        interviewManager.setInterviewConfiguration(interviewConfiguration);

        testSetUpOneOnOneInterviews();

        Interview interview1 = interviewer1.getInterviews().get(0);
        Interview interview2 = interviewer2.getInterviews().get(0);
        InterviewTime time = new InterviewTime(LocalDate.now().plusDays(5), "9-10 am");
        interviewer1.scheduleInterview(interview1, time);
        interviewer2.scheduleInterview(interview2, time);

        testCurrentRoundIsOverOneOnOne(interview1, interview2);

        interviewManager.reject(application1); //TODO: remove
        testApplicationListsUpdatedAfterRound();

        testHireApplicants();
    }

    @Test
    void testOneRoundInterviewConfigurationGroup() {
        interviewManager = testConstructor();

        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.GROUP, "Group interview"});
        interviewManager.setInterviewConfiguration(interviewConfiguration);

        testSetUpGroupInterview();

        testCurrentRoundIsOverGroup();

        interviewManager.reject(application1); //TODO: remove
        testApplicationListsUpdatedAfterRound();

        testHireApplicants();
    }

    @Test
    private void testSetUpOneOnOneInterviews() {
        interviewManager.setUpOneOnOneInterviews();
        assert interviewer1.getInterviews().size() == 1;
        assert interviewer2.getInterviews().size() == 1;
    }

    @Test
    private void testSetUpGroupInterview() {
        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        interviewManager.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        assert interviewer1.getInterviews().size() == 1;
        assert interviewer2.getInterviews().size() == 1;
        assert interviewer1.getInterviews().get(0).equals(interviewer2.getInterviews().get(0));

    }

    @Test
    private void testCurrentRoundIsOverOneOnOne(Interview interview1, Interview interview2) {
        interview2.setResult(application1, false);
        interview1.setResult(application2, true);
        assert interviewManager.currentRoundIsOver();
    }

    @Test
    private void testCurrentRoundIsOverGroup() {
        Interview interview = interviewer1.getInterviews().get(0);
        HashMap<JobApplication, Boolean> resultsMap = new HashMap<>();
        resultsMap.put(application1, false);
        resultsMap.put(application2, true);
        interview.setResults(resultsMap);
        assert interviewManager.currentRoundIsOver();
    }

    @Test
    private void testApplicationListsUpdatedAfterRound() {
        assert interviewManager.getApplicationsInConsideration().equals(Collections.singletonList(application2));
        assert interviewManager.getApplicationsRejected().equals(Collections.singletonList(application1));
    }

    @Test
    private void testHireApplicants() {
        interviewManager.hireApplicants(interviewManager.getApplicationsInConsideration());
        assert interviewManager.getBranchJobPosting().isFilled();
        assert application1.getStatus().isArchived();
        assert application2.getStatus().isHired();
    }
}
