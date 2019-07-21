package DocumentManagers;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.Company;

public class DocumentManagerFactory {

    public CompanyDocumentManager getCompanyDocumentManager(Object object) {
        return new CompanyDocumentManager((Company) object);
    }

    public ApplicantDocumentManager getApplicantDocumentManager(Object object) {
        return new ApplicantDocumentManager((Applicant) object);
    }

}
