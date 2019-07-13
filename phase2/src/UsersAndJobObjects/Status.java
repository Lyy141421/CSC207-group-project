package UsersAndJobObjects;

import java.io.Serializable;
import java.util.HashMap;

public class Status implements Serializable {

    // === Class variables ===
    // Job application statuses as constants
    private static final int ARCHIVED = -3;
    private static final int SUBMITTED = -2;
    private static final int UNDER_REVIEW = -1;
    private static final int PHONE_INTERVIEW = 0;
    private static final int IN_PERSON_1 = 1;
    private static final int IN_PERSON_2 = 2;
    private static final int IN_PERSON_3 = 3;
    private static final int HIRED = 4;
    // Map of statuses and their identifying integers
    private static HashMap<Integer, String> descriptions = new HashMap<Integer, String>() {{
        put(Status.ARCHIVED, "Archived");
        put(Status.SUBMITTED, "Submitted");
        put(Status.UNDER_REVIEW, "Under review");
        put(Status.PHONE_INTERVIEW, "Phone interview");
        put(Status.IN_PERSON_1, "In-person interview round 1");
        put(Status.IN_PERSON_2, "In-person interview round 2");
        put(Status.IN_PERSON_3, "In-person interview round 3");
        put(Status.HIRED, "Hired");
    }};

    // === Instance variable ===
    private int value = Status.SUBMITTED;

    // === Constructors ===
    public Status(int value) {
        this.value = value;
    }

    // === Getters ===
    public int getValue() {
        return this.value;
    }

    // === Setters ===
    public void setValue(int value) {
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
        this.setValue(Status.HIRED);
    }

    @Override
    public String toString() {
        return Status.descriptions.get(this.value);
    }

    // ============================================================================================================== //
    // === Package-private methods ===

    // === Constructors ===
    Status() {
    }

    // === Getters ===
    String getDescription() {
        return Status.descriptions.get(this.value);
    }

    // === Other methods ===

    /**
     * Checks whether this status is set to hired.
     *
     * @return true iff this status is set to hired.
     */
    boolean isHired() {
        return this.value == Status.HIRED;
    }

    /**
     * Checks whether this status is set to archived.
     *
     * @return true iff this status is set to archived.
     */
    boolean isArchived() {
        return this.value == Status.ARCHIVED;
    }

    /**
     * Advance this status.
     */
    void advanceStatus() {
        this.value++;
    }


}
