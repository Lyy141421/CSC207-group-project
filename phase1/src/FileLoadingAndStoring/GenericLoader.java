package FileLoadingAndStoring;

import Main.JobApplicationSystem;

abstract class GenericLoader<T> {
    /**
     * Loads an object input memory.
     */

    /**
     * Loads one object of this type into memory.
     * @param jobApplicationSystem  The job application system being used.
     * @param object    The object being loaded.
     */
    abstract void loadOne(JobApplicationSystem jobApplicationSystem, T object);
}
