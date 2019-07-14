package ApplicantStuff;

import java.io.File;

class CV extends JobApplicationDocument {

    // === Class variables ===
    private static final String FILE_TYPE = "CV";

    CV(File folder, String contents) {
        super(folder, CV.FILE_TYPE, contents);
    }

    CV(File folder, File file) {
        super(folder, file);
    }

}
