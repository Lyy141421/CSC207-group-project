package GUIClasses;

public class MethodsTheGuiCallsBeforeLogin {
    // === Methods FOR GUI to call ===
    /*
     */
/**
 * Check whether the date inputted by the user as getToday is valid.
 * @param getToday     Today's date that is selected by the user.
 * @return true iff the date selected by the user is on or after the previous login date.
 *//*

    boolean getValidTodayDate(LocalDate getToday) {
        return !getToday.isBefore(JobApplicationSystem.getPreviousLoginDate());
    }
*/

}
