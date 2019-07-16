package DocumentManagers;

import Main.User;

import java.io.File;

public class UserDocumentManager extends DocumentManager<User> {

    // === Class variables ===
    public static String FOLDER = DocumentManager.INITIAL_PATH + "/users";

    UserDocumentManager(User user) {
        super(user);
        this.setFolder(new File(FOLDER + "/" + this.getObject().getUsername()));
    }
}
