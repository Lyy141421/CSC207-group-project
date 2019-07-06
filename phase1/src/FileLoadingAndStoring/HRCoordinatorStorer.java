package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;

import java.util.HashMap;

public class HRCoordinatorStorer extends GenericStorer<HRCoordinator> {

    /**
     * Stores the HR Coordinator.
     * @param hrCoordinator     The HR Coordinator to be stored.
     */
    void storeOne(HRCoordinator hrCoordinator){
        LoaderManager.mapPut(HRCoordinator.FILENAME, hrCoordinator.getUsername(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", hrCoordinator.getPassword());
        data.put("legalName", hrCoordinator.getLegalName());
        data.put("email", hrCoordinator.getEmail());
        data.put("dateCreated", hrCoordinator.getDateCreated());
        data.put("Company", new String[]{Company.FILENAME,
                hrCoordinator.getCompany().getName()});
        StorerManager.subStore(hrCoordinator.getCompany());
        FileSystem.write(HRCoordinator.FILENAME, hrCoordinator.getUsername(), data);
    }
}
