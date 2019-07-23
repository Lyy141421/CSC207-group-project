package ApplicantStuff;

import CompanyStuff.Branch;
import CompanyStuff.Company;
import CompanyStuff.Interview;
import CompanyStuff.Interviewer;
import CompanyStuff.JobPostings.BranchJobPosting;
import DocumentManagers.ApplicantDocumentManager;
import Main.JobApplicationSystem;
import Miscellaneous.InterviewTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantTest {

    Applicant createApplicant(String name) {
        return new Applicant(name, "password", "John Smith",
                "john_smith@gmail.com", LocalDate.of(2019, 7, 20), "L4B3Z9");
    }

    Applicant createApplicantWithOneApplicationAndOneReference() {
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting = this.createJobPosting();
        JobApplication jobApp = new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 21));
        Reference reference = new Reference("bob@gmail.com", LocalDate.of(2019, 8, 1));
        jobApp.addReferences(new ArrayList<>(Arrays.asList(reference)));
        return applicant;
    }

    ArrayList<Applicant> createThreeApplicantsOneJobPosting() {
        Applicant applicant1 = this.createApplicant("app1");
        Applicant applicant2 = this.createApplicant("app2");
        Applicant applicant3 = this.createApplicant("app3");
        ArrayList<Applicant> applicants = new ArrayList<>(Arrays.asList(applicant1, applicant2, applicant3));
        BranchJobPosting jobPosting = this.createJobPosting();
        for (Applicant applicant : applicants) {
            JobApplication jobApp = new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 21));
            Reference reference = new Reference("bob@gmail.com", LocalDate.of(2019, 8, 1));
            jobApp.addReferences(new ArrayList<>(Arrays.asList(reference)));
        }
        return applicants;
    }

    BranchJobPosting createJobPosting() {
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        return new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
    }

    BranchJobPosting createJobPosting2() {
        Company company = new Company("Company2");
        Branch branch = new Branch("Branch2", "L4B3Z9", company);
        return new BranchJobPosting("Title2", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
    }

    Branch createNewBranchSameCompany() {
        Company company = this.createJobPosting().getCompany();
        return new Branch("Branch2", "L4B3Z9", company);
    }

    BranchJobPosting createNewJobPostingSameBranch() {
        Branch branch = this.createJobPosting().getBranch();
        return new BranchJobPosting("Title2", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
    }

    JobApplicationSystem createJobApplicationSystemWith2OpenJobPostings() {
        JobApplicationSystem jas = new JobApplicationSystem();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        Company company = jas.createCompany("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        new BranchJobPosting("Title2", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 8, 30), LocalDate.of(2019, 8, 31));
        return jas;
    }

    @Test
    void testConstructor() {
        Applicant applicant = this.createApplicant("jsmith");
        assertEquals("L4B3Z9", applicant.getCMA());
        assertNotEquals("l4b3z9", applicant.getCMA());  // TODO Does capitalization count?
        assertNotNull(applicant.getJobApplicationManager());
        assertTrue(applicant.getJobApplicationManager() instanceof JobApplicationManager);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertNotNull(applicant.getDocumentManager());
        assertTrue(applicant.getDocumentManager() instanceof ApplicantDocumentManager);
        assertNotNull(applicant.getDocumentManager());
        assertTrue(applicant.getDocumentManager().getFolder().getName().startsWith(applicant.getUsername()));
    }

    @Test
    void testRegisterJobApplication() {
        Applicant applicant = this.createApplicant("jsmith");
        Company company = new Company("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(), 1, branch, LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10), LocalDate.now());
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertTrue(jobPosting.getJobApplications().isEmpty());
        JobApplication jobApp = new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 21));
        assertEquals(1, applicant.getJobApplicationManager().getJobApplications().size());
        assertEquals(1, jobApp.getJobPosting().getJobApplications().size());
    }

    @Test
    void testWithdrawJobApplicationBeforeApplicantCloseDate() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 28));
        JobApplication jobApp = jobPosting.getJobApplications().get(0);
        applicant.withdrawJobApplication(LocalDate.of(2019, 7, 28), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertTrue(jobPosting.getJobApplications().isEmpty());
        assertTrue(jobApp.getReferences().get(0).getJobAppsForReference().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationOnApplicantCloseDate() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 30));
        JobApplication jobApp = jobPosting.getJobApplications().get(0);
        applicant.withdrawJobApplication(LocalDate.of(2019, 7, 30), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertTrue(jobPosting.getJobApplications().isEmpty());
        assertTrue(jobApp.getReferences().get(0).getJobAppsForReference().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterApplicantCloseDateBeforeReferenceCloseDate() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        JobApplication jobApp = jobPosting.getJobApplications().get(0);
        applicant.withdrawJobApplication(LocalDate.of(2019, 7, 31), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
        assertTrue(jobApp.getReferences().get(0).getJobAppsForReference().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationOnReferenceCloseDate() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 10));
        JobApplication jobApp = jobPosting.getJobApplications().get(0);
        applicant.withdrawJobApplication(LocalDate.of(2019, 8, 10), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
        assertTrue(jobApp.getReferences().get(0).getJobAppsForReference().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterReferenceCloseDateBeforeInterviewsSetUp() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        applicant.withdrawJobApplication(LocalDate.of(2019, 8, 11), jobPosting);
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
    }

    @Test
    void testWithdrawJobApplicationAfterOneOnOneInterviewSetUpNotScheduled() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        applicant.withdrawJobApplication(LocalDate.of(2019, 8, 11), jobPosting);

        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
        assertTrue(interviewer.getInterviews().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterOneOnOneInterviewScheduled() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer = new Interviewer("Interviewer", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer);
        jobPosting.getInterviewManager().setUpOneOnOneInterviews();
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 11), 3));
        applicant.withdrawJobApplication(LocalDate.of(2019, 8, 11), jobPosting);

        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
        assertTrue(interviewer.getInterviews().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterGroupInterviewSetUpMultipleInterviewersOneApplicant() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer1 = new Interviewer("Interviewer1", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer2 = new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer3 = new Interviewer("Interviewer3", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer1);
        jobPosting.getBranch().addInterviewer(interviewer2);
        jobPosting.getBranch().addInterviewer(interviewer3);
        jobPosting.getInterviewManager().setUpGroupInterview(interviewer1, new ArrayList<>(Arrays.asList(interviewer2, interviewer3)));
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 13), 3));

        assertTrue(applicant.withdrawJobApplication(LocalDate.of(2019, 8, 14), jobPosting));
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertTrue(interview.getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
        assertTrue(interview.getJobApplications().isEmpty());
        assertTrue(interviewer1.getInterviews().isEmpty());
        assertTrue(interviewer2.getInterviews().isEmpty());
        assertTrue(interviewer3.getInterviews().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterGroupInterviewCompletedMultipleInterviewersOneApplicant() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        Interviewer interviewer1 = new Interviewer("Interviewer1", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer2 = new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer3 = new Interviewer("Interviewer3", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer1);
        jobPosting.getBranch().addInterviewer(interviewer2);
        jobPosting.getBranch().addInterviewer(interviewer3);
        jobPosting.getInterviewManager().setUpGroupInterview(interviewer1, new ArrayList<>(Arrays.asList(interviewer2, interviewer3)));
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 13), 3));
        JobApplication jobApp = jobPosting.getJobApplications().get(0);
        interview.setResults(new HashMap<JobApplication, Boolean>() {{
            put(jobApp, true);
        }});

        assertTrue(applicant.withdrawJobApplication(LocalDate.of(2019, 8, 14), jobPosting));
        assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        assertTrue(interview.getJobApplications().isEmpty());
        assertEquals(1, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertEquals(1, jobPosting.getInterviewManager().getApplicationsRejected().size());
    }

    @Test
    void testWithdrawJobApplicationAfterGroupInterviewSetUpMultipleInterviewersApplicantsOneWithdrawal() {
        ArrayList<Applicant> applicants = this.createThreeApplicantsOneJobPosting();
        Applicant applicant1 = applicants.get(0);
        Applicant applicant2 = applicants.get(1);
        Applicant applicant3 = applicants.get(2);
        BranchJobPosting jobPosting = applicant1.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        JobApplication jobApp1 = jobPosting.getJobApplications().get(0);
        Interviewer interviewer1 = new Interviewer("Interviewer1", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer2 = new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer3 = new Interviewer("Interviewer3", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer1);
        jobPosting.getBranch().addInterviewer(interviewer2);
        jobPosting.getBranch().addInterviewer(interviewer3);
        jobPosting.getInterviewManager().setUpGroupInterview(interviewer1, new ArrayList<>(Arrays.asList(interviewer2, interviewer3)));
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 13), 3));
        applicant1.withdrawJobApplication(LocalDate.of(2019, 8, 13), jobPosting);

        assertTrue(applicant1.getJobApplicationManager().getJobApplications().isEmpty());
        assertFalse(applicant2.getJobApplicationManager().getJobApplications().isEmpty());
        assertFalse(applicant3.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(2, interview.getJobApplications().size());
        assertEquals(3, jobPosting.getJobApplications().size());
        assertEquals(2, jobPosting.getInterviewManager().getApplicationsInConsideration().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp1));
        assertEquals(1, interviewer1.getInterviews().size());
        assertEquals(1, interviewer2.getInterviews().size());
        assertEquals(1, interviewer3.getInterviews().size());
    }

    @Test
    void testWithdrawJobApplicationAfterGroupInterviewSetUpMultipleInterviewersMultipleApplicantsMultipleWithdrawals() {
        ArrayList<Applicant> applicants = this.createThreeApplicantsOneJobPosting();
        Applicant applicant1 = applicants.get(0);
        Applicant applicant2 = applicants.get(1);
        BranchJobPosting jobPosting = applicant1.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        JobApplication jobApp1 = jobPosting.getJobApplications().get(0);
        JobApplication jobApp2 = jobPosting.getJobApplications().get(1);
        JobApplication jobApp3 = jobPosting.getJobApplications().get(2);
        Interviewer interviewer1 = new Interviewer("Interviewer1", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer2 = new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer3 = new Interviewer("Interviewer3", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer1);
        jobPosting.getBranch().addInterviewer(interviewer2);
        jobPosting.getBranch().addInterviewer(interviewer3);
        jobPosting.getInterviewManager().setUpGroupInterview(interviewer1, new ArrayList<>(Arrays.asList(interviewer2, interviewer3)));
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 13), 3));
        applicant1.withdrawJobApplication(LocalDate.of(2019, 8, 12), jobPosting);
        applicant2.withdrawJobApplication(LocalDate.of(2019, 8, 13), jobPosting);

        assertTrue(applicant1.getJobApplicationManager().getJobApplications().isEmpty());
        assertEquals(3, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().contains(jobApp3));
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp1));
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp2));
        assertEquals(1, interviewer1.getInterviews().size());
        assertEquals(1, interviewer2.getInterviews().size());
        assertEquals(1, interviewer3.getInterviews().size());
    }

    @Test
    void testWithdrawJobApplicationAfterGroupInterviewSetUpMultipleInterviewersMultipleApplicantsAllWithdrawn() {
        ArrayList<Applicant> applicants = this.createThreeApplicantsOneJobPosting();
        Applicant applicant1 = applicants.get(0);
        Applicant applicant2 = applicants.get(1);
        Applicant applicant3 = applicants.get(2);
        BranchJobPosting jobPosting = applicant1.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForApplications(LocalDate.of(2019, 7, 31));
        jobPosting.getBranch().getJobPostingManager().updateJobPostingsClosedForReferences(LocalDate.of(2019, 8, 11));
        JobApplication jobApp1 = jobPosting.getJobApplications().get(0);
        JobApplication jobApp2 = jobPosting.getJobApplications().get(1);
        JobApplication jobApp3 = jobPosting.getJobApplications().get(2);
        Interviewer interviewer1 = new Interviewer("Interviewer1", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer2 = new Interviewer("Interviewer2", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        Interviewer interviewer3 = new Interviewer("Interviewer3", "password", "Legal Name", "email", jobPosting.getBranch(), "field", LocalDate.of(2019, 7, 10));
        jobPosting.getBranch().addInterviewer(interviewer1);
        jobPosting.getBranch().addInterviewer(interviewer2);
        jobPosting.getBranch().addInterviewer(interviewer3);
        jobPosting.getInterviewManager().setUpGroupInterview(interviewer1, new ArrayList<>(Arrays.asList(interviewer2, interviewer3)));
        Interview interview = jobPosting.getInterviewManager().getApplicationsInConsideration().get(0).getLastInterview();
        interview.setTime(new InterviewTime(LocalDate.of(2019, 8, 13), 3));

        assertTrue(applicant1.withdrawJobApplication(LocalDate.of(2019, 8, 12), jobPosting));
        assertTrue(applicant2.withdrawJobApplication(LocalDate.of(2019, 8, 13), jobPosting));
        assertTrue(applicant3.withdrawJobApplication(LocalDate.of(2019, 8, 13), jobPosting));
        for (Applicant applicant : applicants) {
            assertTrue(applicant.getJobApplicationManager().getJobApplications().isEmpty());
        }
        assertTrue(interview.getJobApplications().isEmpty());
        assertEquals(3, jobPosting.getJobApplications().size());
        assertTrue(jobPosting.getInterviewManager().getApplicationsInConsideration().isEmpty());
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp1));
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp2));
        assertTrue(jobPosting.getInterviewManager().getApplicationsRejected().contains(jobApp3));
        assertTrue(interviewer1.getInterviews().isEmpty());
        assertTrue(interviewer2.getInterviews().isEmpty());
        assertTrue(interviewer3.getInterviews().isEmpty());
    }

    @Test
    void testWithdrawJobApplicationAfterPostingIsFilled() {
        Applicant applicant1 = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant1.getJobApplicationManager().getJobApplications().get(0).getJobPosting();
        jobPosting.setFilled();
        boolean withdrawn = applicant1.withdrawJobApplication(LocalDate.of(2019, 8, 12), jobPosting);
        assertFalse(withdrawn);
    }

    @Test
    void testHasAppliedToPosting() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        BranchJobPosting jobPosting = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting();

        assertFalse(applicant.hasAppliedToPosting(this.createNewJobPostingSameBranch()));
        assertFalse(applicant.hasAppliedToPosting(this.createJobPosting2()));
        assertTrue(applicant.hasAppliedToPosting(jobPosting));
    }

    @Test
    void testHasAppliedToBranch() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        Branch branch = applicant.getJobApplicationManager().getJobApplications().get(0).getJobPosting().getBranch();

        assertFalse(applicant.hasAppliedToBranch(this.createJobPosting2().getBranch()));
        assertFalse(applicant.hasAppliedToBranch(this.createNewBranchSameCompany()));
        assertTrue(applicant.hasAppliedToBranch(branch));
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToNoneOpen() {
        JobApplicationSystem jas = new JobApplicationSystem();
        jas.setToday(LocalDate.of(2019, 7, 31));
        Applicant applicant = this.createApplicant("jsmith");
        Company company = jas.createCompany("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));

        assertTrue(applicant.getOpenJobPostingsNotAppliedTo(jas).isEmpty());
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToOneOpenAndApplied() {
        JobApplicationSystem jas = new JobApplicationSystem();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        Company company = jas.createCompany("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertTrue(applicant.getOpenJobPostingsNotAppliedTo(jas).isEmpty());
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToOneOpenAndNotApplied() {
        JobApplicationSystem jas = new JobApplicationSystem();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        Company company = jas.createCompany("Company");
        Branch branch = new Branch("Branch", "L4B3Z9", company);
        BranchJobPosting jobPosting = new BranchJobPosting("Title", "field", "descriptionhujedk",
                new ArrayList<>(Arrays.asList("CV", "Cover Letter", "Reference Letter")), new ArrayList<>(),
                1, branch, LocalDate.of(2019, 7, 15), LocalDate.of(2019, 7, 30), LocalDate.of(2019, 8, 10));
        assertEquals(1, applicant.getOpenJobPostingsNotAppliedTo(jas).size());
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToMultipleOpenAndSomeApplied() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertEquals(1, applicant.getOpenJobPostingsNotAppliedTo(jas).size());
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToMultipleOpenAndAllApplied() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertTrue(applicant.getOpenJobPostingsNotAppliedTo(jas).isEmpty());
    }

    @Test
    void testGetOpenJobPostingsNotAppliedToMultipleOpenAndNoneApplied() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        assertEquals(2, applicant.getOpenJobPostingsNotAppliedTo(jas).size());
    }

    @Test
    void testIsInactiveNoApplication() {
        Applicant applicant = this.createApplicant("jsmith");
        assertFalse(applicant.isInactive(LocalDate.of(2019, 7, 28)));
    }

    @Test
    void testIsInactiveOneAppClosed29Days() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        assertFalse(applicant.isInactive(LocalDate.of(2019, 8, 28)));
    }

    @Test
    void testIsInactiveOneAppClosed30Days() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        assertTrue(applicant.isInactive(LocalDate.of(2019, 8, 29)));
    }

    @Test
    void testIsInactiveOneAppClosed31Days() {
        Applicant applicant = this.createApplicantWithOneApplicationAndOneReference();
        assertTrue(applicant.isInactive(LocalDate.of(2019, 8, 30)));
    }

    @Test
    void testIsInactiveMultipleAppsAllNotClosed() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 7, 28));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertFalse(applicant.isInactive(jas.getToday()));
    }

    @Test
    void testIsInactiveMultipleAppsLastNotClosed() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 8, 10));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertFalse(applicant.isInactive(jas.getToday()));
    }

    @Test
    void testIsInactiveMultipleAppsLastClosed29Days() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 9, 28));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertFalse(applicant.isInactive(jas.getToday()));
    }

    @Test
    void testIsInactiveMultipleAppsLastClosed30Days() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 9, 29));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertTrue(applicant.isInactive(jas.getToday()));
    }

    @Test
    void testIsInactiveMultipleAppsLastClosed31Days() {
        JobApplicationSystem jas = this.createJobApplicationSystemWith2OpenJobPostings();
        jas.setToday(LocalDate.of(2019, 9, 30));
        Applicant applicant = this.createApplicant("jsmith");
        BranchJobPosting jobPosting =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(0);
        BranchJobPosting jobPosting2 =
                jas.getCompanies().get(0).getBranches().get(0).getJobPostingManager().getBranchJobPostings().get(1);
        new JobApplication(applicant, jobPosting, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        new JobApplication(applicant, jobPosting2, new ArrayList<>(), LocalDate.of(2019, 7, 28));
        assertTrue(applicant.isInactive(jas.getToday()));
    }
}