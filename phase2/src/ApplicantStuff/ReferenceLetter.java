package ApplicantStuff;

import java.io.File;

class ReferenceLetter extends JobApplicationDocument {

    // === Class variables ===
    private static String FILE_TYPE = "ReferenceLetter";

    ReferenceLetter(File folder, String contents) {
        super(folder, ReferenceLetter.FILE_TYPE, contents);
    }

    ReferenceLetter(File folder, File file) {
        super(folder, file);
    }
}
