package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
import Managers.JobPostingManager;
import Miscellaneous.InterviewTime;
import NotificationSystem.Notification;
import NotificationSystem.NotificationManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InterviewerTest {

    LocalDate today = LocalDate.of(2019, 7, 29);
    Company company_a = new Company("company_a");
    Branch branch_a = company_a.createBranch("BranchName", "E6H1P9");
    HRCoordinator HRCord = new HRCoordinator("Stacy", "ABC123", "Stacy O",
            "Stacy@gmail.com", branch_a, today);


    Branch createBranch (String name){
        return company_a.createBranch(name, "Skiff Lake");
    }

    Interviewer createInterviewer (String username){
        return new Interviewer(username, "ABC123", username + "'s Real Name", username + "@gmail.com", branch_a,
                "Sales", today);
    }

    Applicant createApplicant(String name) {
        return new Applicant(name, "ABC123", name + " Lastname",
                name + "@gmail.com", today, "Toronto");
    }

    BranchJobPosting createPosting(String name){
        return new BranchJobPosting(name, "Sales", "none", new ArrayList<>(),
                new ArrayList<>(), 1, branch_a, today, today.plusDays(5), today.plusDays(5));
    }

    JobApplication createJobApplication(Interviewer interviewer){
        BranchJobPosting branchJobPosting = createPosting("TestPosting");
        return new JobApplication(createApplicant("Zach"), branchJobPosting, today);
    }

    Interview createInterview(Interviewer interviewer){
        JobApplication jobApplication = createJobApplication(interviewer);
        return new Interview(jobApplication, interviewer, jobApplication.getJobPosting().getInterviewManager());
    }

    @Test
    void testConstructor() {
        Interviewer interviewer = this.createInterviewer("Phillip");

        assertEquals(interviewer.getUsername(), "Phillip");
        assertEquals(interviewer.getPassword(), "ABC123");
        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
        assertEquals(interviewer.getDateCreated(), today);

        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
        assertEquals(interviewer.getBranch().getName(), "BranchName");
        assertEquals(interviewer.getBranch().getCma(), "Fredericton");
        assertEquals(interviewer.getField(), "Sales");
    }

    @Test
    void getUsername() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getUsername(), "Phillip");
    }

    @Test
    void getPassword() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getPassword(), "ABC123");
    }

    @Test
    void getLegalName() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
    }

    @Test
    void getEmail() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
    }

    @Test
    void getDateCreated() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getDateCreated(), today);
    }

    @Test
    void getNotificationManager() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
    }

    @Test
    void setUsername() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setUsername("Not Phillip");
        assertEquals(interviewer.getUsername(), "Not Phillip");
    }

    @Test
    void setEmail() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setEmail("Phillip.Soetebeer@gmail.com");
        assertEquals(interviewer.getEmail(), "Phillip.Soetebeer@gmail.com");
    }

    @Test
    void update() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.update(new Notification("TestN", "TestN text"));
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
        interviewer.update(5);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
    }

    @Test
    void getAllNotifications() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.update(new Notification("TestN", "TestN text"));
        interviewer.update(new Notification("TestN2", "TestN text2"));
        assertTrue(interviewer.getAllNotifications().size() == 2);
    }

    @Test
    void addNotification() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.addNotification(new Notification("TestN", "TestN text"));
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
    }

    @Test
    void removeNotification() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        Notification notification = new Notification("TestN", "TestN text");
        interviewer.addNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 1);
        interviewer.removeNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
    }

    @Test
    void getBranch() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getBranch() instanceof Branch);
        assertEquals(interviewer.getBranch().getName(), "BranchName");
    }

    @Test
    void getField() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getField(), "Sales");
    }

    @Test
    void getInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertTrue(interviewer.getInterviews() instanceof ArrayList);
    }

    @Test
    void setField() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setField("Quality Assurance");
        assertEquals(interviewer.getField(), "Quality Assurance");
    }

    @Test
    void setBranch() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        interviewer.setBranch(createBranch("Branch 2.0"));
        assertEquals(interviewer.getBranch().getName(), "Branch 2.0");
    }

    @Test
    void setInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        assertTrue(interviewer.getInterviews().size() == 1);
    }

    @Test
    void getEarliestTimeAvailableForNewInterview() { //todo test this more
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 5).getDate(), today.plusDays(5));
    }

    @Test
    void findJobAppById() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        assertTrue(interviewer.findJobAppById(interviewer.getInterviews().get(0).getId()) instanceof JobApplication);
    }

    @Test
    void isAvailable() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        assertTrue(interviewer.isAvailable(new InterviewTime(today, "9-10 am")));
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, "9-10 am"));
        assertTrue(!interviewer.isAvailable(new InterviewTime(today, "9-10 am")));
    }

    @Test
    void isAvailable1() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, "9-10 am"));
        assertTrue(interviewer.isAvailable(today));
    }

    @Test
    void getFirstDateAvailableOnOrAfterDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, "9-10 am"));
        assertTrue(interviewer.getFirstDateAvailableOnOrAfterDate(today) == today);
    }

    @Test
    void getTimeSlotsFilledOnDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, "9-10 am"));
        assertEquals(interviewer.getTimeSlotsFilledOnDate(today).get(0), "2019-07-29 at 9-10 am");
    }

    @Test
    void getTimeSlotsAvailableOnDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, "9-10 am"));
        assertEquals(interviewer.getTimeSlotsAvailableOnDate(today).size(), 7);
    }

    @Test
    void removeInterview() {
    }

    @Test
    void scheduleInterview() {
    }

    @Test
    void getUnscheduledInterviews() {
    }

    @Test
    void getScheduledUpcomingInterviews() {
    }

    @Test
    void getAllIncompleteInterviews() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccurred() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccuredAsCoordinator() {
    }

    @Test
    void getIncompleteInterviewsAlreadyOccurredNotAsCoordinator() {
    }

    @Test
    void getListOfIntervieweeJobApplications() {
    }

    @Test
    void getDisplayedProfileCategories() {
    }

    @Test
    void getDisplayedProfileInformation() {
    }

    @Test
    void addInterview() {
    }
}