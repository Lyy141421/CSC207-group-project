package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreHRCoordinators {

    /**
     * Stores all the HR Coordinators in this list.
     * @param hrCoordinators    The list of HR Coordinators to be stored.
     */
    public void storeAll(ArrayList<HRCoordinator> hrCoordinators) {
        for (HRCoordinator hrCoordinator : hrCoordinators) {
            this.storeOne(hrCoordinator);
        }
    }

    /**
     * Stores the HR Coordinator.
     */
    private void storeOne(HRCoordinator hrCoordinator){
        FileSystem.mapPut(HRCoordinator.FILENAME, hrCoordinator.getUsername(), this);
        HashMap<String, Object> data = new HashMap<>();
        data.put("password", hrCoordinator.getPassword());
        data.put("legalName", hrCoordinator.getLegalName());
        data.put("email", hrCoordinator.getEmail());
        data.put("dateCreated", hrCoordinator.getDateCreated());
        data.put("Company", new String[]{Company.FILENAME,
                hrCoordinator.getCompany().getName()});
        FileSystem.write(HRCoordinator.FILENAME, hrCoordinator.getUsername(), data);
    }
}
