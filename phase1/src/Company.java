import java.util.ArrayList;
import java.util.HashMap;

class Company {

    // Elaine: I'm adding these so that my code doesn't have red underlines. This is unfinished.
    // === Instance variables ===
    // The total number of companies registered in this system
    private static int totalNum;
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
     */
    Company() {
    }

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
     * Get the name of this company.
     *
     * @return the name of this company.
     */
    String getName() {
        return this.name;
    }

    /**
     * Get the map of field to interviewers for this company.
     * @return the map of field to interviewers for this company.
     */
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

    /**
     * Find the interviewer with the least amount of interviews in this job field.
     * @param jobField  The job field of the interviewer to be found.
     * @return the interviewer with the least amount of interviews in this field.
     */
    Interviewer findInterviewer(String jobField) {
        int minNumberOfInterviews = 0;
        Interviewer interviewerSoFar = null;
        for (Interviewer interviewer : this.fieldToInterviewers.get(jobField)) {
            int numberOfInterviews = interviewer.getInterviews().size();
            if (numberOfInterviews <= minNumberOfInterviews) {
                interviewerSoFar = interviewer;
            }
        }
        return interviewerSoFar;
    }

    /**
     * Add a job posting to this company.
     * @param jobPosting    The job posting to be added.
     */
    void addJobPosting(JobPosting jobPosting) {
        this.jobPostings.add(jobPosting);
    }

    /**
     * Add an interviewer to this company.
     *
     * @param interviewer The interviewer to be added.
     */
    void addInterviewer(Interviewer interviewer) {
        String field = interviewer.getField();
        if (!this.fieldToInterviewers.containsKey(field)) {
            this.fieldToInterviewers.put(field, new ArrayList<>());
        }
        this.fieldToInterviewers.get(field).add(interviewer);
    }

    /**
     * Report whether this company is the same as other.
     * @param other     The company to be compared with.
     * @return true iff this company is the same as other.
     */
    boolean equals(Company other) {
        return this.ID == other.ID;
    }


}
