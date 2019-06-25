class Main {
    /**
     * Pseudocode for main method
     */

    void run() {
        /*
        1. Log-in / sign-up
        2. Direct user to specific user-type "main" page
            - Create instances of that specific user interface
            - Call the 'run' method for the specific user interface
        3. Exit
         */
    }

    void signUpOrLogIn() {
        /*
        - Ask whether user wants to sign up or log in
        - Direct user to specific sign-up or log-in page
        - Ask for user type, username, password
        - Sign-up:
            - If username already exists, show message that username has already been taken and to choose something else
            - If username does not exist, create a new user of that type
            - Prompt for username and password until user types in a username that is not already taken
        - Log-in
            - If username exists, check password
            - If username does not exist, display message that username cannot be found
            - Prompt for username and password until log-in is successful
        - Record the date upon logging-in?
         */
    }

    void exit() {
        /*
        1. Write the data to files
        2. Display friendly message
         */
    }
}
