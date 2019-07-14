package UsersAndJobObjects;

import Managers.DocumentManager;

import java.io.IOException;
import java.time.LocalDate;

public class Reference extends User {

    Reference(String username, String password, String legalName, String email, LocalDate dateCreated) throws IOException {
        super(username, password, legalName, email, dateCreated);
    }

    Reference(String username, String password, String legalName, String email, LocalDate dateCreated,
              DocumentManager documentManager) {
        super(username, password, legalName, email, dateCreated, documentManager);
    }
}
