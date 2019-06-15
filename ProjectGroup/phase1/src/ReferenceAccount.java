import java.time.LocalDate;

class ReferenceAccount extends UserAccount {

    ReferenceAccount(String legalName, String email, String password, LocalDate dateCreated) {
        super(legalName, email, password, dateCreated);
    }

}
