package DocumentManagers;

import CompanyStuff.Company;

import java.io.File;
import java.io.IOException;
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
        this.makeSubDirectories();
    }

    /**
     * Make subdirectories for this applicant.
     */
    private void makeSubDirectories() {
        File branchFolder = new File(this.getFolder().getPath() + "/branches");
        try {
            branchFolder.mkdir();
            branchFolder.createNewFile();
            File jobPostingsFolder = new File(branchFolder.getPath() + "/jobPostings");
            try {
                jobPostingsFolder.mkdir();
                jobPostingsFolder.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this branch
    }
}
