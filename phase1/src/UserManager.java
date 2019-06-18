import java.io.BufferedReader;

class UserManager {
    // Class that stores, creates and updates children of the User class

    // === Instance Variables ===
    private User[] allUsers = new User[50]; //Array in which all users are stored

    UserManager(BufferedReader usersFile) {
        //TODO: instantiate existing users upon startup by reading usersFile
    }

    // === Array operations on allUsers ===

    /**
     * Adds the passed user to allUsers. Checks and expands allUsers size when required.
     *
     * @param newUser The user object being added to allUsers
     */
    private void addToAllUsers(User newUser) {
        this.allUsersSizeCheck();
        for(int i=0; i < this.allUsers.length - 1; i++) {
            if(this.allUsers[i] == null) {
                this.allUsers[i] = newUser;
                break;
            }
        }
    }

    /**
     * Helper function for any method that adds to allUsers.
     * Checks if allUsers is full with usersIsFull, and expands the size of the array if it is.
     */
    private void allUsersSizeCheck() {
        if(this.usersIsFull()) {
            int oldArrayLen = this.allUsers.length;
            User[] newArray = new User[oldArrayLen + 10];
            System.arraycopy(this.allUsers, 0, newArray, 0, oldArrayLen);
            this.allUsers = newArray;
        }
    }

    /**
     * Helper function for allUsersSizeCheck
     * Checks if the array storing users allUsers is full or not.
     */
    private boolean usersIsFull() {
        for(int i=0; i < this.allUsers.length - 1; i++) {
            if(this.allUsers[i] == null) {
                return false;
            }
        }
        return true;
    }
}
