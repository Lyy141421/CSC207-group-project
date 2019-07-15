package DocumentManagers;

import Main.User;

import java.io.File;

class UserDocumentManager extends DocumentManager<User> {

    UserDocumentManager(User user) {
        super(user);
        this.setFolder(new File(DocumentManager.INITIAL_PATH + "/users/" + this.getObject().getUsername()));
    }
}
