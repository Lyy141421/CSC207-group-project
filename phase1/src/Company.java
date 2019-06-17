import java.util.ArrayList;

class Company {

    // Elaine: I'm adding these so that my code doesn't have red underlines
    // === Instance variables ===
    // The total number of companies registered in this system
    static int totalNum;
    // The unique identifier for this company
    private int ID;
    // The name of this company
    private String name;
    // A list of job postings for positions in this company.
    private ArrayList<JobPosting> jobPostings;


    // === Constructors ===

    /**
     * Create a new company.
     *
     * @param name The company's name.
     */
    Company(String name) {
        this.ID = totalNum;
        this.name = name;
        Company.totalNum++;
    }

    // === Getters ==

    /**
     * Get the job postings for positions in this company.
     *
     * @return the list of job postings for positions in this company.
     */
    ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    // === Other methods ===
    Applicant getFinalCandidate(JobPosting jobPosting) {
        return null;
    }

    Interviewer findInterviewer(String jobField) {
        return null;
    }

    void addJobPosting(JobPosting jobPosting) {

    }

    void addInterview(JobPosting jobPosting, Interview interview) {

    }

    boolean equals(Company other) {
        return false;
    }

}
