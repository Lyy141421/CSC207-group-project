package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

public class InterviewManagerTest {
    private Company company = new Company("HoraceCorp");
    private Branch branch = new Branch("HQ", "Toronto", company);
    private HRCoordinator hrc;
    private Interviewer interviewer1;
    private Interviewer interviewer2;
    private Interviewer interviewer3;
    private Applicant applicant1;
    private Applicant applicant2;
    private Applicant applicant3;
    private BranchJobPosting branchJobPosting1;
    private BranchJobPosting branchJobPosting2;
    private JobApplication application1;
    private JobApplication application2;
    private JobApplication application3;
    private JobApplication application4;
    private JobApplication application5;
    private InterviewManager interviewManager1;
    private InterviewManager interviewManager2;

    void createHRCAndTwoInterviewers() {
        this.hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch, LocalDate.now());
        this.interviewer1 = new Interviewer("anne", "ABC", "Anne Mann", "anne@gmail.com",
                branch, "HR", LocalDate.now());
        this.interviewer2 = new Interviewer("bob", "ABC", "Bob Mann", "bob@gmail.com",
                branch, "HR", LocalDate.now());
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

    void createThirdApplication() {
        this.applicant3 = new Applicant("josie", "ABC", "Josie Dude", "josie@gmail.com",
                LocalDate.now(), "Windsor");
        this.application3 = new JobApplication(applicant3, branchJobPosting1, LocalDate.now());
    }

    InterviewManager createInterviewManagerFromBJPWithTwoApplications() {
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add(this.application1);
        applicationsInConsideration.add(this.application2);
        return new InterviewManager(this.branchJobPosting1, applicationsInConsideration);
    }

    void createSecondBJP(HRCoordinator hrc) {
        this.branchJobPosting2 = hrc.addJobPosting("Second Job", "HR", "This is a job.",
                new ArrayList<>(), new ArrayList<>(), 1, LocalDate.now(), LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4));
        this.application4 = new JobApplication(applicant1, branchJobPosting2, LocalDate.now());
        this.application5 = new JobApplication(applicant2, branchJobPosting2, LocalDate.now());
    }

    InterviewManager createInterviewManagerFromSecondBJP() {
        ArrayList<JobApplication> applicationsInConsideration = new ArrayList<>();
        applicationsInConsideration.add(this.application4);
        applicationsInConsideration.add(this.application5);
        return new InterviewManager(this.branchJobPosting2, applicationsInConsideration);
    }

    InterviewManager createInterviewManagerNoInterviewers() {
        this.hrc = new HRCoordinator("horace", "ABC", "Horace Businessman",
                "horace@gmail.com", branch, LocalDate.now());
        createBJPWithTwoApplications(this.hrc);
        return createInterviewManagerFromBJPWithTwoApplications();
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
    void testSetUpOneOnOneInterviewNoInterviewers() {
        interviewManager1 = createInterviewManagerNoInterviewers();

        testSetInterviewConfigurationOneOnOne();

        interviewManager1.setUpOneOnOneInterviews();

        assert hrc.getAllNotifications().size() == 1;
    }

    /* Note: The check that prevents HR from setting up group interviews with no interviewers happens outside of the
    scope of InterviewManager. */

    @Test
    void testGetEarliestTimeAvailableForAllInterviewersBusyAfterEarliestTime() {
        InterviewTime time1 = new InterviewTime(LocalDate.now().plusDays(7), "4-5 pm");
        InterviewTime time2 = new InterviewTime(LocalDate.now().plusDays(8), "10-11 am");
        InterviewTime time3 = new InterviewTime(LocalDate.now().plusDays(8), "1-2 pm");

        setUpEarliestTimeTest(time1, time2, time3);

        createSecondBJP(this.hrc);
        interviewManager2 = createInterviewManagerFromSecondBJP();

        testSetInterviewConfigurationGroupSecondBJP();

        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        otherInterviewers.add(interviewer3);
        interviewManager2.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        Interview interview4 = interviewer1.getInterviews().get(1);
        assert interview4.getTime().equals(new InterviewTime(LocalDate.now().plusDays(7), "9-10 am"));
    }

    @Test
    void testGetEarliestTimeAvailableForAllInterviewersBusySameDaySameTime() {
        InterviewTime time1 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");
        InterviewTime time2 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");
        InterviewTime time3 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");

        setUpEarliestTimeTest(time1, time2, time3);

        createSecondBJP(this.hrc);
        interviewManager2 = createInterviewManagerFromSecondBJP();

        testSetInterviewConfigurationGroupSecondBJP();

        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        otherInterviewers.add(interviewer3);
        interviewManager2.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        Interview interview4 = interviewer1.getInterviews().get(1);
        assert interview4.getTime().equals(new InterviewTime(LocalDate.now().plusDays(7), "10-11 am"));
    }

    @Test
    void testGetEarliestTimeAvailableForAllInterviewersBusySameDayConsecutiveTimes() {
        InterviewTime time1 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");
        InterviewTime time2 = new InterviewTime(LocalDate.now().plusDays(7), "10-11 am");
        InterviewTime time3 = new InterviewTime(LocalDate.now().plusDays(7), "11 am - 12 pm");

        setUpEarliestTimeTest(time1, time2, time3);

        createSecondBJP(this.hrc);
        interviewManager2 = createInterviewManagerFromSecondBJP();

        testSetInterviewConfigurationGroupSecondBJP();

        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        otherInterviewers.add(interviewer3);
        interviewManager2.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        Interview interview4 = interviewer1.getInterviews().get(1);
        assert interview4.getTime().equals(new InterviewTime(LocalDate.now().plusDays(7), "12-1 pm"));
    }

    @Test
    void testGetEarliestTimeAvailableForAllInterviewersBusySameDayNonConsecutiveTimes() {
        InterviewTime time1 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");
        InterviewTime time2 = new InterviewTime(LocalDate.now().plusDays(7), "3-4 pm");
        InterviewTime time3 = new InterviewTime(LocalDate.now().plusDays(7), "12-1 pm");

        setUpEarliestTimeTest(time1, time2, time3);

        createSecondBJP(this.hrc);
        interviewManager2 = createInterviewManagerFromSecondBJP();

        testSetInterviewConfigurationGroupSecondBJP();

        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        otherInterviewers.add(interviewer3);
        interviewManager2.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        Interview interview4 = interviewer1.getInterviews().get(1);
        assert interview4.getTime().equals(new InterviewTime(LocalDate.now().plusDays(7), "10-11 am"));
    }

    @Test
    void testGetEarliestTimeAvailableForAllInterviewersBusyDifferentDays() {
        InterviewTime time1 = new InterviewTime(LocalDate.now().plusDays(7), "9-10 am");
        InterviewTime time2 = new InterviewTime(LocalDate.now().plusDays(8), "10-11 am");
        InterviewTime time3 = new InterviewTime(LocalDate.now().plusDays(9), "11 am - 12 pm");

        setUpEarliestTimeTest(time1, time2, time3);

        createSecondBJP(this.hrc);
        interviewManager2 = createInterviewManagerFromSecondBJP();

        testSetInterviewConfigurationGroupSecondBJP();

        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        otherInterviewers.add(interviewer3);
        interviewManager2.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        Interview interview4 = interviewer1.getInterviews().get(1);
        assert interview4.getTime().equals(new InterviewTime(LocalDate.now().plusDays(7), "10-11 am"));
    }

    private void setUpEarliestTimeTest(InterviewTime time1, InterviewTime time2, InterviewTime time3) {
        interviewManager1 = testConstructor();
        createThirdApplication();
        interviewManager1.getApplicationsInConsideration().add(application3);
        this.interviewer3 = new Interviewer("carol", "ABC", "Carol Mann",
                "carol@gmail.com", branch, "HR", LocalDate.now());

        testSetInterviewConfigurationOneOnOne();

        testSetUpOneOnOneInterviews();

        Interview interview1 = interviewer1.getInterviews().get(0);
        Interview interview2 = interviewer2.getInterviews().get(0);
        Interview interview3 = interviewer3.getInterviews().get(0);

        interviewer1.scheduleInterview(interview1, time1);
        interviewer2.scheduleInterview(interview2, time2);
        interviewer3.scheduleInterview(interview3, time3);
    }

    @Test
    void testOneRoundInterviewConfigurationOneOnOne() {
        interviewManager1 = testConstructor();

        testSetInterviewConfigurationOneOnOne();

        testSetUpOneOnOneInterviews();

        Interview interview1 = interviewer1.getInterviews().get(0);
        Interview interview2 = interviewer2.getInterviews().get(0);
        InterviewTime time = new InterviewTime(LocalDate.now().plusDays(5), "9-10 am");
        interviewer1.scheduleInterview(interview1, time);
        interviewer2.scheduleInterview(interview2, time);

        testCurrentRoundIsOverOneOnOne(interview1, interview2);

        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview1);
        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview2);

        testApplicationListsUpdatedAfterRoundVer1();

        testHireApplicants();
    }

    @Test
    void testOneRoundInterviewConfigurationGroup() {
        interviewManager1 = testConstructor();

        testSetInterviewConfigurationGroup();

        testSetUpGroupInterview();

        Interview interview = interviewer1.getInterviews().get(0);

        testCurrentRoundIsOverGroup(interview);

        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview);

        testApplicationListsUpdatedAfterRoundVer1();

        testHireApplicants();
    }

    @Test
    void testTwoRoundInterviewConfigurationGroupThenOneOnOne() {
        interviewManager1 = testConstructor();
        createThirdApplication();
        interviewManager1.getApplicationsInConsideration().add(application3);

        testSetInterviewConfigurationGroupThenOneOnOne();

        testSetUpGroupInterview();

        Interview interview = interviewer1.getInterviews().get(0);

        testCurrentRoundIsOverGroupThreeApplications(interview);

        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview);

        testApplicationListsUpdatedAfterRoundVer2();

        branchJobPosting1.advanceInterviewRound();

        testSetUpOneOnOneInterviews();

        Interview interview1 = interviewer1.getInterviews().get(0);
        Interview interview2 = interviewer2.getInterviews().get(0);
        InterviewTime time = new InterviewTime(LocalDate.now().plusDays(11), "9-10 am");
        interviewer1.scheduleInterview(interview1, time);
        interviewer2.scheduleInterview(interview2, time);

        testCurrentRoundIsOverOneOnOne(interview1, interview2);

        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview1);
        testUpdateInterviewersOfInterviewCompletionOrCancellation(interview2);

        testApplicationListsUpdatedAfterRoundVer3();

        testHireApplicants();
    }

    @Test
    private void testSetInterviewConfigurationOneOnOne() {
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        interviewManager1.setInterviewConfiguration(interviewConfiguration);
        assert interviewManager1.getFinalRoundNumber() == 0;
    }

    @Test
    private void testSetInterviewConfigurationGroup() {
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.GROUP, "Group interview"});
        interviewManager1.setInterviewConfiguration(interviewConfiguration);
        assert interviewManager1.getFinalRoundNumber() == 0;
    }

    @Test
    private void testSetInterviewConfigurationGroupSecondBJP() {
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.GROUP, "Group interview"});
        interviewManager2.setInterviewConfiguration(interviewConfiguration);
        assert interviewManager2.getFinalRoundNumber() == 0;
    }

    @Test
    private void testSetInterviewConfigurationGroupThenOneOnOne() {
        ArrayList<String[]> interviewConfiguration = new ArrayList<>();
        interviewConfiguration.add(new String[]{Interview.GROUP, "Group interview"});
        interviewConfiguration.add(new String[]{Interview.ONE_ON_ONE, "One-on-one interview"});
        interviewManager1.setInterviewConfiguration(interviewConfiguration);
        assert interviewManager1.getFinalRoundNumber() == 1;
    }

    @Test
    private void testSetUpOneOnOneInterviews() {
        interviewManager1.setUpOneOnOneInterviews();
        assert interviewer1.getInterviews().size() == 1;
        assert interviewer2.getInterviews().size() == 1;
    }

    @Test
    private void testSetUpGroupInterview() {
        ArrayList<Interviewer> otherInterviewers = new ArrayList<>();
        otherInterviewers.add(interviewer2);
        interviewManager1.setUpGroupInterview(interviewer1, otherInterviewers, LocalDate.now().plusDays(5), 2);
        assert interviewer1.getInterviews().size() == 1;
        assert interviewer2.getInterviews().size() == 1;
        assert interviewer1.getInterviews().get(0).equals(interviewer2.getInterviews().get(0));
        assert !interviewer1.getInterviews().get(0).getTime().getDate().isBefore(LocalDate.now().plusDays(7));
    }

    @Test
    private void testCurrentRoundIsOverOneOnOne(Interview interview1, Interview interview2) {
        interview2.setResult(application1, false);
        interview1.setResult(application2, true);
        assert interviewManager1.currentRoundIsOver();
    }

    @Test
    private void testCurrentRoundIsOverGroup(Interview interview) {
        HashMap<JobApplication, Boolean> resultsMap = new HashMap<>();
        resultsMap.put(application1, false);
        resultsMap.put(application2, true);
        interview.setResults(resultsMap);
        assert interviewManager1.currentRoundIsOver();
    }

    @Test
    private void testCurrentRoundIsOverGroupThreeApplications(Interview interview) {
        HashMap<JobApplication, Boolean> resultsMap = new HashMap<>();
        resultsMap.put(application1, true);
        resultsMap.put(application2, true);
        resultsMap.put(application3, false);
        interview.setResults(resultsMap);
        assert interviewManager1.currentRoundIsOver();
    }

    @Test
    private void testUpdateInterviewersOfInterviewCompletionOrCancellation(Interview interview) {
        interviewManager1.updateInterviewersOfInterviewCompletionOrCancellation(interview);
        boolean interviewersUpdated = true;
        for (Interviewer interviewer : interview.getAllInterviewers()) {
            if (!interviewer.getInterviews().isEmpty())
                interviewersUpdated = false;
        }
        assert interviewersUpdated;
    }

    @Test
    private void testApplicationListsUpdatedAfterRoundVer1() {
        assert interviewManager1.getApplicationsInConsideration().equals(Collections.singletonList(application2));
        assert interviewManager1.getApplicationsRejected().equals(Collections.singletonList(application1));
    }

    @Test
    private void testApplicationListsUpdatedAfterRoundVer2() {
        assert interviewManager1.getApplicationsInConsideration().equals(Arrays.asList(application1, application2));
        assert interviewManager1.getApplicationsRejected().equals(Collections.singletonList(application3));
    }

    @Test
    private void testApplicationListsUpdatedAfterRoundVer3() {
        assert interviewManager1.getApplicationsInConsideration().equals(Collections.singletonList(application2));
        assert interviewManager1.getApplicationsRejected().equals(Arrays.asList(application3, application1));
    }

    @Test
    private void testHireApplicants() {
        interviewManager1.hireApplicants(interviewManager1.getApplicationsInConsideration());
        assert interviewManager1.getBranchJobPosting().isFilled();
        assert application1.getStatus().isArchived();
        assert application2.getStatus().isHired();
    }
}
