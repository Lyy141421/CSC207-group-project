import static org.junit.jupiter.api.Assertions.*;

class InterviewerTest {

    Company company_a = new Company("company_a");
    Branch branch_a = company_a.createBranch("BranchName", "Skiff Lake");


    Interviewer createInterviewer (String username){
        return new Interviewer(username, "ABC123", username + "'s Real Name", username + "@gmail.com", branch_a,
                "Sales", LocalDate.of(2019, 7, 29))
    }

    @test
    void testConstructor() {
        Interviewer interviewer = this.createInterviewer("Phillip");

        assertEquals(interviewer.getUsername(), "Phillip");
        assertEquals(interviewer.getPassword(), "ABC123");
        assertEquals(interviewer.getLegalName(), "Phillip's Real Name");
        assertEquals(interviewer.getEmail(), "Phillip@gmail.com");
        assertEquals(interviewer.getDateCreated(), LocalDate.of(2019, 7, 29));

        assertTrue(interviewer.getNotificationManager() instanceof NotificationManager);
        assertEquals(interviewer.getBranch().getName(), "BranchName");
        assertEquals(interviewer.getBranch().getCma(), "Skiff Lake");
        assertEquals(interviewer.getField().getCma(), "Sales");
    }


}