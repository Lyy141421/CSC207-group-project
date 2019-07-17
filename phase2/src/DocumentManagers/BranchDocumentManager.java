package DocumentManagers;

import CompanyStuff.Branch;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class BranchDocumentManager extends DocumentManager<Branch> implements Observer {

    // === Class variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/companies";

    // === Instance variables ===
    private Branch branch;

    BranchDocumentManager(Branch branch) {
        super(branch);
        this.branch = branch;
        this.setFolder(new File(FOLDER + "/" + this.getObject().getName()));
    }

    @Override
    public void update(Observable o, Object arg) {
        // when job posting closes, copy all the documents submitted into the folder for this branch
        // branch folder -> (each job posting has a folder) -> (folder for each applicant) -> (documents submitted for each applicant)
    }
}