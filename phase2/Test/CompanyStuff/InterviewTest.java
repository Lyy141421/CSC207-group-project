package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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
        return new Applicant(name, "ABC123", "Legal Name",
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

    Interview createInterviewGroup(String name) {
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
    }

    @Test
    void getInterviewCoordinatorToNotes() {
    }

    @Test
    void getInterviewCoordinator() {
    }

    @Test
    void getTime() {
    }

    @Test
    void getJobApplications() {
    }

    @Test
    void getOtherInterviewers() {
    }

    @Test
    void getOtherInterviewersToNotes() {
    }

    @Test
    void getRoundNumber() {
    }

    @Test
    void setTime() {
    }

    @Test
    void setInterviewCoordinatorNotes() {
    }

    @Test
    void setOtherInterviewNotes() {
    }

    @Test
    void setResults() {
    }

    @Test
    void setResult() {
    }

    @Test
    void getAllInterviewersToNotes() {
    }

    @Test
    void getAllNotes() {
    }

    @Test
    void isPassed() {
    }

    @Test
    void isIncomplete() {
    }

    @Test
    void isScheduled() {
    }

    @Test
    void getAllInterviewers() {
    }

    @Test
    void removeApplication() {
    }

    @Test
    void findJobAppById() {
    }

    @Test
    void hasAlreadyWrittenNotesForInterview() {
    }

    @Test
    void getIntervieweeNames() {
    }

    @Test
    void getCategoryValuesForInterviewerUnscheduledOrIncomplete() {
    }

    @Test
    void getCategoryValuesForInterviewerScheduled() {
    }

    @Test
    void getMiniDescriptionForHR() {
    }
}