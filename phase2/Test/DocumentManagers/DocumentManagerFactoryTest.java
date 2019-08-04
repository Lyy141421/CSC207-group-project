package DocumentManagers;

import ApplicantStuff.Applicant;
import CompanyStuff.Company;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerFactoryTest {

    @Test
    void testCreateCompanyDocumentManager() {
        Company company = new Company("CompanyTest");
        DocumentManager companyDocumentManager = new DocumentManagerFactory().createDocumentManager(company);
        assertEquals(company.getName(), companyDocumentManager.getFolder().getName());
        companyDocumentManager.getFolder().delete();
    }

    @Test
    void testCreateApplicantDocumentManager() {
        Applicant applicant = new Applicant("ApplicantTest", "password", "John Smith",
                "john_smith@gmail.com", "Toronto", LocalDate.of(2019, 7, 20));
        DocumentManager applicantDocumentManager = new DocumentManagerFactory().createDocumentManager(applicant);
        assertEquals(applicant.getUsername(), applicantDocumentManager.getFolder().getName());
        applicantDocumentManager.getFolder().delete();
    }
}