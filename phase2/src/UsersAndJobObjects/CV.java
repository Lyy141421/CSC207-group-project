package UsersAndJobObjects;

import java.io.File;
import java.io.IOException;

public class CV extends JobApplicationDocument {

    CV(User user, String contents) throws IOException {
        super(user, contents);
    }

    CV(User user, File file) {
        super(user, file);
    }

}
