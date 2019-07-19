package DocumentManagers;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;
import CompanyStuff.Company;

public class DocumentManagerFactory {

    public DocumentManager createDocumentManager(Object object) {
        if (object instanceof Applicant) {
            return new ApplicantDocumentManager((Applicant) object);
        } else if (object instanceof Company) {
            return new CompanyDocumentManager((Company) object);
        }
        return null;
    }

}
