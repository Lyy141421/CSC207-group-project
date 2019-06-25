public interface Storable {
    //Should Include a final variable filename for every implementation

    /**
     *
     */
    String getId();

    /**
     *
     */
    void saveSelf();

    /**
     *
     */
    void loadSelf();

}