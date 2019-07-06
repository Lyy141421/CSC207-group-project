package GUIClasses;

import UsersAndJobObjects.User;

public class InterfaceFactory implements AbstractFactory<UserInterface> {

    /**
     * Create the appropriate interface for this user.
     * @param object  The user who logged in.
     * @return  the appropriate interface for this user.
     */
    public UserInterface create(Object object) {
        User user = (User) object;
        switch (user.getClass().getName()) {
            case "UsersAndJobObjects.Applicant":
                return new ApplicantInterface(user);
            case "UsersAndJobObjects.Interviewer":
                return new InterviewerInterface(user);
            case "UsersAndJobObjects.HRCoordinator":
                return new HRCoordinatorInterface(user);
        }
        return null;
    }
}
