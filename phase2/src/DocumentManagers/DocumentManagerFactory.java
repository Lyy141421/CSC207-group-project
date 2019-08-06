package DocumentManagers;

import ApplicantStuff.Applicant;
import ApplicantStuff.JobApplication;
import CompanyStuff.Branch;
import CompanyStuff.Company;

public class DocumentManagerFactory {
    /*
     * A document manager factory.
     */

    /**
     * Creates a document manager.
     *
     * @param object    The object that the manager is for.
     * @return the applicant document manager for this applicant.
     */
    public DocumentManager createDocumentManager(Object object) {
        if (object instanceof Company) {
            return new CompanyDocumentManager((Company) object);
        } else if (object instanceof Applicant) {
            return new ApplicantDocumentManager((Applicant) object);
        } else if (object instanceof JobApplication) {
            return new ReferenceLetterDocumentManager((JobApplication) object);
        }
        return null;
    }

}
