package DocumentManagers;

import CompanyStuff.Company;
import DocumentManagers.DocumentManager;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class CompanyDocumentManager extends DocumentManager<Company> implements Observer {

    // === Instance variables ===
    private Company company;

    CompanyDocumentManager(Company company) {
        super(company);
        this.company = company;
        this.setFolder(new File(DocumentManager.INITIAL_PATH + "/companies/" + this.getObject().getName()));
    }

    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this company
        // company folder -> (each job posting has a folder) -> (folder for each applicant) -> (documents submitted for each applicant)
    }
}
