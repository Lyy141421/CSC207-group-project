package UsersAndJobObjects;

import java.io.File;
import java.io.IOException;

public class CoverLetter extends JobApplicationDocument {

    CoverLetter(User user, String contents) throws IOException {
        super(user, contents);
    }

    CoverLetter(User user, File file) {
        super(user, file);
    }

}
