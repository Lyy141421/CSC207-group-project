package Miscellaneous;

import CompanyStuff.Interview;

import java.time.LocalDate;
import java.util.Comparator;

public class InterviewTimeComparator implements Comparator<Interview> {
    /**
     * Comparator that compares interviews in terms of time.
     */

    /**
     * Compare two interviews in terms of date.
     *
     * @param interview1 The first interview in question.
     * @param interview2 The second interview in question.
     * @return a negative number, zero or a positive number based on if the first interview is before, at the same time
     * or after the second interview, respectively.
     */
    public int compare(Interview interview1, Interview interview2) {
        LocalDate date1 = interview1.getTime().getDate();
        String timeSlot1 = interview1.getTime().getTimeSlot();
        LocalDate date2 = interview2.getTime().getDate();
        String timeSlot2 = interview2.getTime().getTimeSlot();
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isEqual(date2)) {
            return InterviewTime.timeSlots.indexOf(timeSlot1) - InterviewTime.timeSlots.indexOf(timeSlot2);
        } else {
            return 1;
        }
    }

}
