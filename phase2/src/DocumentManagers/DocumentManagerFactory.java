package DocumentManagers;

import ApplicantStuff.Applicant;
import CompanyStuff.Branch;

public class DocumentManagerFactory {

    public DocumentManager createDocumentManager(Object object) {
        if (object instanceof Applicant) {
            return new ApplicantDocumentManager((Applicant) object);
        } else if (object instanceof Branch) {
            return new CompanyDocumentManager((Branch) object);
        }
        return null;
    }

}
