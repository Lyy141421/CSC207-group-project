package FileLoadingAndStoring;

public interface Storable {
    //Should Include a final variable filename for every implementation

    /**
     *
     */
    String getIdString();

    /**
     *
     */
    void saveSelf();

    /**
     *
     */
    void loadSelf();

}