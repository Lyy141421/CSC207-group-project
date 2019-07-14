package ApplicantStuff;

import java.io.File;

class CoverLetter extends JobApplicationDocument {

    // === Class variables ===
    private static final String FILE_TYPE = "CoverLetter";

    CoverLetter(File folder, String contents) {
        super(folder, CoverLetter.FILE_TYPE, contents);
    }

    CoverLetter(File folder, File file) {
        super(folder, file);
    }

}
