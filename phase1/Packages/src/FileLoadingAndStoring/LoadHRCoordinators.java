package FileLoadingAndStoring;

import UsersAndJobObjects.Company;
import UsersAndJobObjects.HRCoordinator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadHRCoordinators extends LoadObjects<HRCoordinator> {

    /**
     * Loads the HR Coordinator.
     */
    void loadOne(HRCoordinator hrCoordinator){
        FileSystem.mapPut(HRCoordinator.FILENAME, hrCoordinator.getUsername(), this);
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
        hrCoordinator.setCompany((Company) FileSystem.subLoader(Company.class, (String)
                        ((ArrayList) data.get("Company")).get(0), (String) ((ArrayList) data.get("Company")).get(1)));
    }
}
