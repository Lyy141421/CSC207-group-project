class ApplicantInterface extends UserInterface {
    /**
     * The general interface for an applicant
     */

    // === Instance variables ===
    private Applicant applicant; // The applicant currently logged in

    // === Constructor ===

    /**
     * Create the interface for this Applicant.
     *
     * @param applicant The applicant currently logged in
     */
    ApplicantInterface(Applicant applicant) {
        this.applicant = applicant;
    }

    void browseOpenJobPostingsNotAppliedTo() {
        /*
        ArrayList<JobPosting> openJobPostings = applicant.getOpenJobPostingsNotAppliedTo(LocalDate.now());
        - Prompt user if they would like to filter postings by field, company, both, or neither (checkboxes?)
        If user wants to filter by field:
            - Prompt user to enter name of field
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getField().equalsIgnoreCase(field)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getCompany().getName()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching field "field"
            }
        Else if user wants to filter by company:
            - Prompt user to enter name of company
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getCompany().getName().equalsIgnoreCase(companyName)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getField()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching company "companyName"
            }
        Else if user wants to filter by field and company:
            - Prompt user to enter name of field
            - Prompt user to enter name of company
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                if (posting.getField().equalsIgnoreCase(field)
                        && posting.getCompany.getName().equalsIgnoreCase(companyName)) {
                    noPostingsFound = false;
                    - Show posting.getTitle()
                    - Show posting.getId()
                    - Show posting.getDescription()
                    - Show posting.getRequirements()
                    - Show posting.getNumPositions()
                    - Show posting.getPostDate()
                    - Show posting.getCloseDate()
                }
            }
            If (noPostingsFound) {
                - Tell user that no postings were found matching field "field" and company "companyName"
            }
        Else:
            boolean noPostingsFound = true;
            for (JobPosting posting : openJobPostings) {
                noPostingsFound = false;
                - Show posting.getTitle()
                - Show posting.getId()
                - Show posting.getField()
                - Show posting.getDescription()
                - Show posting.getRequirements()
                - Show posting.getNumPositions()
                - Show posting.getCompany.getName()
                - Show posting.getPostDate()
                - Show posting.getCloseDate()
            }
            If (noPostingsFound) {
                - Tell user that the system does not currently have any postings
            }
         */
    }

    void applyForJob() {
        /*
        - Prompt for name of company applicant wishes to apply to
        if (JobApplicationSystem.getCompany(companyName) == null) {
            - Tell user that no company "companyName" was found
        }
        else {
            Company company = JobApplicationSystem.getCompany(companyName);
            - Prompt for id of posting applicant wishes to apply for (keep id in string format)
            if (company.getJobPostingManager().getJobPosting(id) == null) {
                - Tell user that no job posting was found with the given id
            }
            else {
                //TODO finish once clarification is obtained
            }

        }

        */
    }



}
