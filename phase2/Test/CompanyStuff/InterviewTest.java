package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InterviewTest {

    private LocalDate today = LocalDate.of(2019, 7, 29);
    private Company company = new Company("company_a");
    private Branch branch = company.createBranch("BranchName", "E6H1P9");
    private HRCoordinator hrcoordinator = new HRCoordinator("Stacy", "ABC123",
            "Stacy Anderson" + " LeagalName", "Stacy@gmail.com", branch, today.plusDays(-5));
    private Interviewer interviewer = new Interviewer("George", "ABC123", "George's Real Name", "George@gmail.com", branch,
            "Sales", today.plusDays(-5));

    BranchJobPosting createJobPostingHR() {
        return hrcoordinator.addJobPosting("Title", "Sales", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1,
                today.plusDays(-5), today.plusDays(-3), today.plusDays(-3));
    }

    BranchJobPosting createJobPostingHRGroup() {
        return hrcoordinator.addJobPosting("Title", "Sales", "description",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 2,
                today.plusDays(-5), today.plusDays(-3), today.plusDays(-3));
    }

    Applicant createApplicant(String name) {
        return new Applicant(name, "ABC123", name + " Legal Name",
                name + "@gmail.com", today.plusDays(-5), "Toronto");
    }

    Interviewer createInterviewer (String username){
        return new Interviewer(username, "ABC123", username + "'s Real Name", username + "@gmail.com", branch,
                "Sales", today.plusDays(-5));
    }

    JobApplication createJobApplication(Applicant app, BranchJobPosting posting){
        return new JobApplication(app, posting, today.plusDays(-5));
    }

//    Interview getInterview() {
//        BranchJobPosting posting = createJobPostingHR();
//        createJobApplication(createApplicant("Phillip"), posting);
//        createJobApplication(createApplicant("Will"), posting);
//        createJobApplication(createApplicant("Hannah"), posting);
//        createJobApplication(createApplicant("Jonathan"), posting);
//        posting.createInterviewManager();
//        posting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(today);
//        posting.getInterviewManager().setUpOneOnOneInterviews();
//        posting.getJobApplications().get(0).getInterviews();
//        return new Interview("");
//    }

    Interview createInterview(String name) {
        BranchJobPosting posting = createJobPostingHR();
        JobApplication app = createJobApplication(createApplicant(name), posting);
        posting.createInterviewManager();
        ArrayList<String[]> config = new ArrayList<>();
        config.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        config.add(new String[]{Interview.ONE_ON_ONE, "Over The Phone"});
        posting.getInterviewManager().setInterviewConfiguration(config);
        posting.getInterviewManager().advanceRound();
        return new Interview(app, interviewer, posting.getInterviewManager());
    }

    Interview createInterviewGroup() {
        ArrayList<Interviewer> interviewer_list = new ArrayList<>();
        interviewer_list.add(createInterviewer("Jonathan"));
        interviewer_list.add(createInterviewer("Riley"));
        BranchJobPosting posting = createJobPostingHRGroup();
        JobApplication app = createJobApplication(createApplicant("Will"), posting);
        JobApplication app2 = createJobApplication(createApplicant("Hannah"), posting);
        ArrayList<JobApplication> app_list = new ArrayList<>();
        app_list.add(app);
        app_list.add(app2);
        posting.createInterviewManager();
        ArrayList<String[]> config = new ArrayList<>();
        config.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        config.add(new String[]{Interview.ONE_ON_ONE, "Over The Phone"});
        posting.getInterviewManager().setInterviewConfiguration(config);
        posting.getInterviewManager().advanceRound();
        return new Interview(app_list, interviewer, interviewer_list, posting.getInterviewManager());
    }

    @Test
    void getId() {
        assertNotEquals(createInterview("Al").getId(), createInterview("Bill").getId());
    }

    @Test
    void getInterviewCoordinatorToNotes() {
        Interview interview = createInterview("Al");
        interview.setInterviewCoordinatorNotes("Help");
        assertEquals(interview.getInterviewCoordinatorToNotes().get(interviewer), "Help");
    }

    @Test
    void getInterviewCoordinator() {
        assertEquals(createInterview("Al").getInterviewCoordinator(), interviewer);
    }

    @Test
    void getTime() {
        Interview interview = createInterview("Al");
        InterviewTime time = new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM);
        interviewer.scheduleInterview(interview, time);
        assertEquals(interview.getTime(), time);
    }

    @Test
    void getJobApplications() {
        Interview interview = createInterview("Samantha");
        assertEquals(interview.getJobApplications().get(0).getApplicant().getUsername(), "Samantha");
    }

    @Test
    void getOtherInterviewers() {
        Interview interview = createInterviewGroup();
        assertEquals(interview.getOtherInterviewers().size(), 2);
    }

    @Test
    void getOtherInterviewersToNotes() {
        Interview interview = createInterviewGroup();
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(0), "Bad");
        assertEquals(interview.getOtherInterviewersToNotes().get(interview.getOtherInterviewers().get(0)), "Bad");
    }

    @Test
    void getRoundNumber() {
        Interview interview = createInterview("Samantha");
        assertEquals(interview.getRoundNumber(), 0);
    }

    @Test
    void setResults() {
        Interview interview = createInterviewGroup();
        HashMap<JobApplication, Boolean> map = new HashMap<>();
        map.put(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(0), true);
        map.put(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(1), false);
        interview.setResults(map);
        assertTrue(interview.isPassed(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(0)));
        assertFalse(interview.isPassed(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(1)));
    }

    @Test
    void setResult() {
        Interview interview = createInterview("Mack");
        interview.setResult(interview.getJobApplications().get(0), true);
        assertTrue(interview.isPassed(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(0)));
        interview.setResult(interview.getJobApplications().get(0), false);
        assertFalse(interview.isPassed(branch.getJobPostingManager().getBranchJobPostings().get(0).getJobApplications().get(0)));
    }

    @Test
    void getAllInterviewersToNotes() {
        Interview interview = createInterviewGroup();
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(0), "Bad");
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(1), "Sad");
        interview.setInterviewCoordinatorNotes("Great");
        assertEquals(interview.getAllInterviewersToNotes().get(interview.getOtherInterviewers().get(0)), "Bad");
        assertEquals(interview.getAllInterviewersToNotes().get(interview.getOtherInterviewers().get(1)), "Sad");
        assertEquals(interview.getAllInterviewersToNotes().get(interview.getInterviewCoordinator()), "Great");
    }

    @Test
    void getAllNotes() {
        Interview interview = createInterviewGroup();
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(0), "Bad");
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(1), "Sad");
        interview.setInterviewCoordinatorNotes("Great");
        assertTrue(interview.getAllNotes().contains("Bad"));
        assertTrue(interview.getAllNotes().contains("Sad"));
        assertTrue(interview.getAllNotes().contains("Great"));
        assertTrue(interview.getAllNotes().size() == 3);
    }

    @Test
    void isPassed() {
        Interview interview = createInterview("Mack");
        interview.setResult(interview.getJobApplications().get(0), true);
        assertTrue(interview.isPassed(interview.getJobApplications().get(0)));
    }

    @Test
    void isIncomplete() {
        Interview interview = createInterviewGroup();
        assertTrue(interview.isIncomplete());
        HashMap<JobApplication, Boolean> map = new HashMap<>();
        map.put(interview.getJobApplications().get(0), true);
        map.put(interview.getJobApplications().get(1), false);
        interview.setResults(map);
        assertFalse(interview.isIncomplete());
    }

    @Test
    void isScheduled() {
        Interview interview = createInterview("Nick");
        assertFalse(interview.isScheduled());
        InterviewTime time = new InterviewTime(today, InterviewTime.ELEVEN_AM_TO_NOON);
        interview.setTime(time);
        assertTrue(interview.isScheduled());
    }

    @Test
    void getAllInterviewers() {
        Interview interview = createInterviewGroup();
        assertTrue(interview.getAllInterviewers().size() == 3);
        assertTrue(interview.getAllInterviewers().contains(interview.getInterviewCoordinator()));
        assertTrue(interview.getAllInterviewers().contains(interview.getOtherInterviewers().get(0)));
        assertTrue(interview.getAllInterviewers().contains(interview.getOtherInterviewers().get(0)));
    }

    @Test
    void removeApplication() {
        Interview interview = createInterviewGroup();
        assertEquals(interview.getJobApplications().size(), 2);
        interview.removeApplication(interview.getJobApplications().get(0));
        assertEquals(interview.getJobApplications().size(), 1);

    }

    @Test
    void findJobAppById() {
        Interview interview = createInterviewGroup();
        assertEquals(interview.findJobAppById(interview.getJobApplications().get(0).getId()), interview.getJobApplications().get(0));
    }

    @Test
    void hasAlreadyWrittenNotesForInterview() {
        Interview interview = createInterviewGroup();
        assertFalse(interview.hasAlreadyWrittenNotesForInterview(interview.getOtherInterviewers().get(0)));
        assertFalse(interview.hasAlreadyWrittenNotesForInterview(interview.getOtherInterviewers().get(1)));
        assertFalse(interview.hasAlreadyWrittenNotesForInterview(interview.getInterviewCoordinator()));
        interview.setOtherInterviewNotes(interview.getOtherInterviewers().get(0), "Bad");
        assertTrue(interview.hasAlreadyWrittenNotesForInterview(interview.getOtherInterviewers().get(0)));
        assertFalse(interview.hasAlreadyWrittenNotesForInterview(interview.getOtherInterviewers().get(1)));
        interview.setInterviewCoordinatorNotes("Great");
        assertTrue(interview.hasAlreadyWrittenNotesForInterview(interview.getInterviewCoordinator()));
    }

    @Test
    void getIntervieweeNames() {
        Interview interview = createInterviewGroup();
        assertEquals(interview.getIntervieweeNames(), "Will Legal Name, Hannah Legal Name");
    }

//    @Test
//    void getCategoryValuesForInterviewerUnscheduledOrIncomplete() {
//    }
//
//    @Test
//    void getCategoryValuesForInterviewerScheduled() {
//    }
//
//    @Test
//    void getMiniDescriptionForHR() {
//    }
}