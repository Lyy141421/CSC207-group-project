import java.util.ArrayList;
import java.util.HashMap;

class Company {

    // Elaine: I'm adding these so that my code doesn't have red underlines. This is unfinished.
    // === Instance variables ===
    // The total number of companies registered in this system
    static int totalNum;
    // The unique identifier for this company
    private int ID;
    // The name of this company
    private String name;
    // The interviewers in this company
    private HashMap<String, ArrayList<Interviewer>> fieldToInterviewers = new HashMap<>();
    // A list of job postings for positions in this company.
    private ArrayList<JobPosting> jobPostings = new ArrayList<>();


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

    String getName() {
        return this.name;
    }

    HashMap<String, ArrayList<Interviewer>> getFieldToInterviewers() {
        return this.fieldToInterviewers;
    }

    /**
     * Get the job postings for positions in this company.
     *
     * @return the list of job postings for positions in this company.
     */
    ArrayList<JobPosting> getJobPostings() {
        return this.jobPostings;
    }

    // === Setters ===
    void setJobPostings(ArrayList<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
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

    void addInterviewer(String field, Interviewer interviewer) {
        if (!this.fieldToInterviewers.containsKey(field)) {
            this.fieldToInterviewers.put(field, new ArrayList<>());
        }
        this.fieldToInterviewers.get(field).add(interviewer);
    }

    void addInterview(JobPosting jobPosting, Interview interview) {

    }

    boolean equals(Company other) {
        return false;
    }

}
