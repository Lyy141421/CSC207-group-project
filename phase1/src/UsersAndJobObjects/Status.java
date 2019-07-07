package UsersAndJobObjects;

import java.util.HashMap;

public class Status {
    // === Class variables ===
    // Job application statuses as constants
    static final int ARCHIVED = -3;
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

    // === Constructor ===
    public Status(int value) {
        this.value = value;
    }

    // === Getters ===
    public int getValue() {
        return this.value;
    }

    String getDescription() {
        return Status.descriptions.get(this.value);
    }

    // === Setters ===
    public void setValue(int value) {
        this.value = value;
    }

    // === Other methods ===

    boolean isArchived() {
        return this.value == Status.ARCHIVED;
    }

    boolean isOnPhoneInterview() {
        return this.value == Status.PHONE_INTERVIEW;
    }

    /**
     * Advance this status.
     */
    void advanceStatus() {
        this.value ++;
    }

    /**
     * Set this status as "Archived".
     */
    void setArchived() {
        this.value = Status.ARCHIVED;
    }

    /**
     * Set this status as "Hired".
     */
    void setHired() {
        this.value = Status.HIRED;
    }
}
