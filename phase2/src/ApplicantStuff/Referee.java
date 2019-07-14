package ApplicantStuff;

import Main.User;

import java.time.LocalDate;

public class Referee extends User {

    // === Instance variables ===
    private RefereeDocumentManager documentManager;

    Referee(String username, String password, String legalName, String email, LocalDate dateCreated) {
        super(username, password, legalName, email, dateCreated);
        this.documentManager = new RefereeDocumentManager(this);
    }

    Referee(String username, String password, String legalName, String email, LocalDate dateCreated,
            RefereeDocumentManager documentManager) {
        super(username, password, legalName, email, dateCreated);
        this.documentManager = documentManager;
    }


}
