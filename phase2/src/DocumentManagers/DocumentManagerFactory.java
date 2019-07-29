package DocumentManagers;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.Company;

public class DocumentManagerFactory {
    /*
     * A document manager factory.
     */

    /**
     * Creates a company document manager.
     *
     * @param company The company that the manager is for.
     * @return the company document manager for this company.
     */
    public CompanyDocumentManager createCompanyDocumentManager(Company company) {
        return new CompanyDocumentManager(company);
    }

    /**
     * Creates an applicant document manager.
     *
     * @param applicant The applicant that the manager is for.
     * @return the applicant document manager for this applicant.
     */
    public ApplicantDocumentManager createApplicantDocumentManager(Applicant applicant) {
        return new ApplicantDocumentManager(applicant);
    }

}
