package ApplicantStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Status implements Serializable {

    // === Class variables ===
    // Job application statuses as constants
    private static final int ARCHIVED = -3;
    private static final int SUBMITTED = -2;
    private static final int UNDER_REVIEW = -1;
    // Map of statuses and their identifying integers
    private TreeMap<Integer, String> descriptions = new TreeMap<Integer, String>() {{
        put(Status.ARCHIVED, "Archived");
        put(Status.SUBMITTED, "Submitted");
        put(Status.UNDER_REVIEW, "Under review");
    }};
    // The integer that represents a "Hired" status
    private int HIRED;

    // === Instance variable ===
    private int value = Status.SUBMITTED;

    // === Constructors ===
    Status() {
    }

    // === Getters ===
    public int getValue() {
        return this.value;
    }

    String getDescription() {
        return this.descriptions.get(this.value);
    }

    // === Setters ===
    public void setDescriptions(ArrayList<Object[]> interviewConfiguration) {
        for (int i = 0; i < interviewConfiguration.size(); i++) {
            this.descriptions.put(i, (String) interviewConfiguration.get(i)[0]);
        }
        this.descriptions.put(interviewConfiguration.size(), "Hired");
        this.HIRED = interviewConfiguration.size();
    }

    private void setValue(int value) {
        this.value = value;
    }

    // === Other methods ===

    /**
     * Set this status as "Archived".
     */
    public void setArchived() {
        this.setValue(Status.ARCHIVED);
    }

    /**
     * Set this status as "Hired".
     */
    public void setHired() {
        this.setValue(this.HIRED);
    }

    /**
     * Advance this status.
     */
    public void advanceStatus() {
        this.value++;
    }

    @Override
    public String toString() {
        return this.descriptions.get(this.value);
    }

    /**
     * Checks whether this status is set to hired.
     *
     * @return true iff this status is set to hired.
     */
    boolean isHired() {
        return this.value == this.HIRED;
    }

    /**
     * Checks whether this status is set to archived.
     *
     * @return true iff this status is set to archived.
     */
    boolean isArchived() {
        return this.value == Status.ARCHIVED;
    }

}
