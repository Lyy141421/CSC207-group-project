import java.time.LocalDate;
import java.util.Comparator;

class DateComparator implements Comparator<Interview> {
    /**
     * Comparator that compares interviews in terms of date.
     */

    /**
     * Compare two interviews in terms of date.
     *
     * @param interview1 The first interview in question.
     * @param interview2 The second interview in question.
     * @return -1, 0, or 1 based on if the first interview is before, on the same date or after the second interview,
     * respectively.
     */
    public int compare(Interview interview1, Interview interview2) {
        LocalDate date1 = interview1.getDate();
        LocalDate date2 = interview2.getDate();
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isEqual(date2)) {
            return 0;
        } else {
            return 1;
        }
    }

}
