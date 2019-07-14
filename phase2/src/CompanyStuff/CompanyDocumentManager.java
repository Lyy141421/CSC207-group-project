package CompanyStuff;

import DocumentManagers.DocumentManager;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class CompanyDocumentManager extends DocumentManager implements Observer {

    // === Instance variables ===
    private Company company;

    CompanyDocumentManager(Company company) {
        super();
        this.company = company;
    }

    CompanyDocumentManager(Company company, File folder) {
        super(folder);
        this.company = company;
    }

    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this company
        // company folder -> (each job posting has a folder) -> (folder for each applicant) -> (documents submitted for each applicant)
    }
}
