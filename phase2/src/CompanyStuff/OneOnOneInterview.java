package CompanyStuff;

import ApplicantStuff.JobApplication;

public class OneOnOneInterview extends Interview {

    // === Instance variables ===
    Interviewer interviewer;

    OneOnOneInterview(JobApplication jobApplication, InterviewManager interviewManager, Interviewer interviewer) {
        super(jobApplication, interviewManager);
        this.interviewer = interviewer;
    }

}
