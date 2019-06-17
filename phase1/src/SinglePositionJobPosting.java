import java.time.LocalDate;
import java.util.ArrayList;

class SinglePositionJobPosting extends JobPosting {

    SinglePositionJobPosting(String title, String field, String description, ArrayList<String> requirements,
                             LocalDate postDate, LocalDate closeDate) {
        super(title, field, description, requirements, postDate, closeDate);
    }
}
