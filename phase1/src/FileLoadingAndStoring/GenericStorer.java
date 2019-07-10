package FileLoadingAndStoring;

import Main.JobApplicationSystem;

abstract class GenericStorer<T> {
    /**
     * Stores the object in the appropriate file.
     */

    /**
     * Store this object.
     * @param jobApplicationSystem The job application system being used.
     * @param object    The object to be stored.
     */
    abstract void storeOne(JobApplicationSystem jobApplicationSystem, T object);
}
