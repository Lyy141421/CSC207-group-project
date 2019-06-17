import java.time.LocalDate;

class ProfessionalReference extends User {

    ProfessionalReference(String legalName, String email, String password, LocalDate dateCreated) {
        super(legalName, email, password, dateCreated);
    }

}
