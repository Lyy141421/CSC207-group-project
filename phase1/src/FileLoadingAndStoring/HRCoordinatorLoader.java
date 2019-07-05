package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class HRCoordinatorLoader extends GenericLoader<HRCoordinator> {

    /**
     * Loads the HR Coordinator.
     * @param hrCoordinator The HR Coordinator to be loaded.
     */
    void loadOne(HRCoordinator hrCoordinator){
        HashMap data = FileSystem.read(HRCoordinator.FILENAME, hrCoordinator.getUsername());
        this.loadPrelimData(hrCoordinator, data);
        this.loadCompany(hrCoordinator, data);
    }

    /**
     * Load the preliminary data for this HR Coordinator.
     *
     * @param data The data for this HR Coordinator.
     */
    private void loadPrelimData(HRCoordinator hrCoordinator, HashMap data) {
        hrCoordinator.setPassword((String) data.get("password"));
        hrCoordinator.setLegalName((String) data.get("legalName"));
        hrCoordinator.setEmail((String) data.get("email"));
        hrCoordinator.setPassword((String) data.get("password"));
        hrCoordinator.setDateCreated(LocalDate.parse((String) data.get("dateCreated")));
    }

    /**
     * Load this HR Coordinator's company.
     *
     * @param data The data for this HR Coordinator.
     */
    private void loadCompany(HRCoordinator hrCoordinator, HashMap data) {
        hrCoordinator.setCompany((Company) LoaderManager.subLoad(Company.class, (String)
                        ((ArrayList) data.get("Company")).get(0), (String) ((ArrayList) data.get("Company")).get(1)));
    }
}
