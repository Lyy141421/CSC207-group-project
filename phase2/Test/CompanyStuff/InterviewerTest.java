package CompanyStuff;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.JobPostings.BranchJobPosting;
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
                name + "@gmail.com", "Toronto", today);
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
        ArrayList<JobApplication> list = new ArrayList<>();
        list.add(jobApplication);
        jobApplication.getJobPosting().setInterviewManager(new InterviewManager(jobApplication.getJobPosting(), list));
        ArrayList<String[]> interviewConfigurationOneOnOne = new ArrayList<>();
        interviewConfigurationOneOnOne.add(new String[]{Interview.ONE_ON_ONE, "In-person interview"});
        jobApplication.getJobPosting().getInterviewManager().setInterviewConfiguration(interviewConfigurationOneOnOne);
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
        assertEquals(interviewer.getAllNotifications().get(0).getTitle(), "TestN");
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
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
        interviewer.removeNotification(notification);
        assertTrue(interviewer.getNotificationManager().getNotifications().size() == 0);
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
    void getEarliestTimeAvailableForNewInterview() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 5).getDate(), today.plusDays(5));
        ArrayList<Interview> lst = new ArrayList<>();
        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 0).getDate(), today.plusDays(0));

        Interview interview = createInterview(interviewer);
        lst.add(interview);
        Interview interview2 = createInterview(interviewer);
        lst.add(interview2);
        Interview interview3 = createInterview(interviewer);
        lst.add(interview3);
        Interview interview4 = createInterview(interviewer);
        lst.add(interview4);
        Interview interview5 = createInterview(interviewer);
        lst.add(interview5);
        Interview interview6 = createInterview(interviewer);
        lst.add(interview6);
        Interview interview7 = createInterview(interviewer);
        lst.add(interview7);
        Interview interview8 = createInterview(interviewer);
        lst.add(interview8);

        interviewer.setInterviews(lst);
        interviewer.scheduleInterview(interview, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(0)));
        interviewer.scheduleInterview(interview2, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(1)));
        interviewer.scheduleInterview(interview3, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(2)));
        interviewer.scheduleInterview(interview4, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(3)));
        interviewer.scheduleInterview(interview5, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(4)));
        interviewer.scheduleInterview(interview6, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(5)));
        interviewer.scheduleInterview(interview7, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(6)));
        interviewer.scheduleInterview(interview8, new InterviewTime(today.plusDays(1), InterviewTime.timeSlots.get(7)));

        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 0).getDate(), today.plusDays(0));
        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 1).getDate(), today.plusDays(2));
        assertEquals(interviewer.getEarliestTimeAvailableForNewInterview(today, 2).getDate(), today.plusDays(2));
    }

    @Test
    void findJobAppById() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        assertNull(interviewer.findJobAppById(1));
        createInterview(interviewer);
        assertEquals(interviewer.findJobAppById(interviewer.getInterviews().get(0).getJobApplications().get(0).getId()),
                interviewer.getInterviews().get(0).getJobApplications().get(0));
    }

    @Test
    void isAvailable() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        assertTrue(interviewer.isAvailable(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM)));
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertTrue(!interviewer.isAvailable(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM)));
    }

    @Test
    void isAvailable1() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertTrue(interviewer.isAvailable(today));
    }

    @Test
    void getFirstDateAvailableOnOrAfterDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertTrue(interviewer.getFirstDateAvailableOnOrAfterDate(today) == today);
        interviewer.getInterviews().get(1).setTime(new InterviewTime(today, InterviewTime.TWO_TO_THREE_PM));
        interviewer.getInterviews().get(2).setTime(new InterviewTime(today, InterviewTime.THREE_TO_FOUR_PM));
        interviewer.getInterviews().get(3).setTime(new InterviewTime(today, InterviewTime.TEN_TO_ELEVEN_AM));
        interviewer.getInterviews().get(4).setTime(new InterviewTime(today, InterviewTime.NOON_TO_ONE_PM));
        interviewer.getInterviews().get(5).setTime(new InterviewTime(today, InterviewTime.ELEVEN_AM_TO_NOON));
        interviewer.getInterviews().get(6).setTime(new InterviewTime(today, InterviewTime.ONE_TO_TWO_PM));
        interviewer.getInterviews().get(7).setTime(new InterviewTime(today, InterviewTime.FOUR_TO_FIVE_PM));
        assertEquals(interviewer.getFirstDateAvailableOnOrAfterDate(today), today.plusDays(1));
    }

    @Test
    void getTimeSlotsFilledOnDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertEquals(interviewer.getTimeSlotsFilledOnDate(today).get(0), "9-10 am");
        assertEquals(interviewer.getTimeSlotsFilledOnDate(today).size(), 1);
    }

    @Test
    void getTimeSlotsAvailableOnDate() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        lst.add(createInterview(interviewer));
        interviewer.setInterviews(lst);
        interviewer.getInterviews().get(0).setTime(new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertEquals(InterviewTime.timeSlots.size() - 1, interviewer.getTimeSlotsAvailableOnDate(today).size());
    }

    @Test
    void removeInterview() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        Interview interview = createInterview(interviewer);
        lst.add(interview);
        interviewer.setInterviews(lst);
        interviewer.removeInterview(interview);
        assertTrue(interviewer.getInterviews().size() == 0);
        interviewer.removeInterview(interview);
    }

    @Test
    void scheduleInterview() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        Interview interview = createInterview(interviewer);
        lst.add(interview);
        interviewer.setInterviews(lst);
        interviewer.scheduleInterview(interview, new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertTrue(interviewer.getInterviews().get(0).getTime().getTimeSlot().equals(InterviewTime.NINE_TO_TEN_AM));
    }

    @Test
    void getUnscheduledInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        Interview interview = createInterview(interviewer);
        lst.add(interview);
        interviewer.setInterviews(lst);
        assertEquals(interviewer.getUnscheduledInterviews().get(0), interview);
        interviewer.scheduleInterview(interview, new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertTrue(interviewer.getUnscheduledInterviews().size() == 0);
    }

    @Test
    void getScheduledUpcomingInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        Interview interview = createInterview(interviewer);
        lst.add(interview);
        interviewer.setInterviews(lst);
        assertTrue(interviewer.getScheduledUpcomingInterviews(today).size() == 0);
        interviewer.scheduleInterview(interview, new InterviewTime(today, InterviewTime.NINE_TO_TEN_AM));
        assertEquals(interviewer.getScheduledUpcomingInterviews(today).get(0), interview);
    }

    @Test
    void getAllIncompleteInterviews() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        ArrayList<Interview> lst = new ArrayList<>();
        Interview interview = createInterview(interviewer);
        lst.add(interview);
        interviewer.setInterviews(lst);
        assertEquals(interviewer.getAllIncompleteInterviews().size(), 1);
        interviewer.getAllIncompleteInterviews().get(0).setResult(interviewer.getAllIncompleteInterviews().get(0).getJobApplications().get(0), true);
        assertEquals(interviewer.getAllIncompleteInterviews().size(), 0);
    }

//    @Test
//    void getIncompleteInterviewsAlreadyOccurred() {
//    }
//
//    @Test
//    void getIncompleteInterviewsAlreadyOccuredAsCoordinator() {
//    }
//
//    @Test
//    void getIncompleteInterviewsAlreadyOccurredNotAsCoordinator() {
//    }
//
//    @Test
//    void getListOfIntervieweeJobApplications() {
//    }
//
//    @Test
//    void getDisplayedProfileCategories() {
//    }
//
//    @Test
//    void getDisplayedProfileInformation() {
//    }

    @Test
    void addInterview() {
        Interviewer interviewer = this.createInterviewer("Phillip");
        Interview interview = createInterview(interviewer);
        assertEquals(interviewer.getInterviews().size(), 0);
        interviewer.addInterview(interview);
        assertEquals(interviewer.getInterviews().size(), 1);
        assertEquals(interviewer.getInterviews().get(0), interview);
    }
}