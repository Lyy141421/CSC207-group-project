package GUIClasses;

interface AbstractFactory<T> {
    /**
     * Factory that creates appropriate objects.
     */

    /**
     * Creates the appropriate object based off of the parameter type.
     *
     * @param object The object for which something is to be created for.
     * @return the object created.
     */
    abstract T create(Object object);

}
