package CompanyStuff.JobPostings;

import CompanyStuff.Branch;

import java.util.ArrayList;

public interface JobPostingObservable {
    ArrayList<CompanyJobPosting> observerList = new ArrayList<>();

    void attachJobPosting(CompanyJobPosting companyJobPosting);

    void notifyAllJobPostings(Branch branch);
}
