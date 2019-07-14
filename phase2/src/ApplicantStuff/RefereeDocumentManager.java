package ApplicantStuff;

import DocumentManagers.UserDocumentManager;

import java.io.File;

public class RefereeDocumentManager extends UserDocumentManager {

    // === Instance variables ===
    private Referee referee;

    RefereeDocumentManager(Referee referee) {
        super(referee);
        this.referee = referee;
    }

    RefereeDocumentManager(Referee referee, File folder) {
        super(referee, folder);
        this.referee = referee;
    }
}
