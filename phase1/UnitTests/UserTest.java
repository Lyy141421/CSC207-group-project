import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    Applicant createApplicant() {
        return new Applicant("username", "password", "legalName",
                "email@gmail.com", LocalDate.of(2019, 6, 17));
    }

    @Test
    void testUserGetUsername() {
        Applicant a = this.createApplicant();
        assertEquals("username", a.getUsername());
    }

    @Test
    void testUserGetPassword() {
        Applicant a = this.createApplicant();
        assertEquals("password", a.getPassword());
    }

    @Test
    void testUserGetLegalName() {
        Applicant a = this.createApplicant();
        assertEquals("legalName", a.getLegalName());
    }

    @Test
    void testUserGetEmail() {
        Applicant a = this.createApplicant();
        assertEquals("email@gmail.com", a.getEmail());
    }

    @Test
    void testUserGetDateCreated() {
        Applicant a = this.createApplicant();
        assertEquals(LocalDate.of(2019, 6, 17), a.getDateCreated());
    }

    @Test
    void testUserSetUsername() {
        Applicant a = this.createApplicant();
        a.setUsername("newUsername");
        assertEquals("newUsername", a.getUsername());
    }

    @Test
    void testUserSetPassword() {
        Applicant a = this.createApplicant();
        a.setPassword("newPassword");
        assertEquals("newPassword", a.getPassword());
    }

    @Test
    void testUserSetLegalName() {
        Applicant a = this.createApplicant();
        a.setLegalName("newLegalName");
        assertEquals("newLegalName", a.getLegalName());
    }

    @Test
    void testUserSetEmail() {
        Applicant a = this.createApplicant();
        a.setEmail("newEmail@gmail.com");
        assertEquals("newEmail@gmail.com", a.getEmail());
    }

    @Test
    void testUserSetDateCreated() {
        Applicant a = this.createApplicant();
        a.setDateCreated(LocalDate.of(2019, 6, 18));
        assertEquals(LocalDate.of(2019, 6, 18), a.getDateCreated());
    }

    @Test
    void testUserHasSameUsername() {
        Applicant a = this.createApplicant();
        assertTrue(a.hasSameUsername("username"));
        assertFalse(a.hasSameUsername("username1"));
    }

    @Test
    void testUserEquals() {
        Applicant a = this.createApplicant();
        Applicant b = new Applicant("username", "password1", "legalName1",
                "email@yahoo.ca", LocalDate.now());
        Applicant c = new Applicant("username1", "password1", "legalName1",
                "email@yahoo.ca", LocalDate.now());
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
    }
}