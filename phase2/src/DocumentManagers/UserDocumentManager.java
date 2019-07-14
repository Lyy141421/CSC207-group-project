package DocumentManagers;

import Main.User;

import java.io.File;

public abstract class UserDocumentManager extends DocumentManager {

    // === Instance variables ===
    private User user;

    public UserDocumentManager(User user) {
        super();
        this.user = user;
        this.setFolder(new File("phase2/userDocuments/" + this.user.getUsername()));
        this.makeSubDirectories();
    }

    public UserDocumentManager(User user, File folder) {
        super(folder);
    }

    public void makeSubDirectories() {
    }
}
