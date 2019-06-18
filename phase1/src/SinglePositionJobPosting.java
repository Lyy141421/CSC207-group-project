import java.time.LocalDate;
import java.util.ArrayList;

class SinglePositionJobPosting extends JobPosting {

    SinglePositionJobPosting() {
    }

    SinglePositionJobPosting(String title, String field, String description, ArrayList<String> requirements,
                             Company company, LocalDate postDate, LocalDate closeDate) {
        super(title, field, description, requirements, company, postDate, closeDate);
    }
}
