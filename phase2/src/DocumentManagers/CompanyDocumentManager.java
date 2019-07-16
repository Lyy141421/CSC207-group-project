package DocumentManagers;

import CompanyStuff.Company;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class CompanyDocumentManager extends DocumentManager<Company> implements Observer {

    // === Class variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/companies";

    // === Instance variables ===
    private Company company;

    CompanyDocumentManager(Company company) {
        super(company);
        this.company = company;
        this.setFolder(new File(FOLDER + "/" + this.getObject().getName()));
    }

    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this company
        // company folder -> (each job posting has a folder) -> (folder for each applicant) -> (documents submitted for each applicant)
    }
}
