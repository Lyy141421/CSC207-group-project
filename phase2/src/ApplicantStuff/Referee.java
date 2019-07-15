package ApplicantStuff;

import DocumentManagers.DocumentManager;
import DocumentManagers.DocumentManagerFactory;
import Main.User;

import java.time.LocalDate;

public class Referee extends User {

    // === Instance variables ===
    private DocumentManager documentManager;

    Referee(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.documentManager = new DocumentManagerFactory().createDocumentManager(this);
    }
}
