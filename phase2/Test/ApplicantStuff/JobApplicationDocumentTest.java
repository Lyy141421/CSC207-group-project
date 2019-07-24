package ApplicantStuff;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationDocumentTest {

    @Test
    void testConstructor1() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./sample.txt"));
        assertEquals("sample.txt", jobAppDoc.getFile().getName());
        assertTrue(jobAppDoc.getFile().isFile());
    }

    @Test
    void testConstructor2() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./sample.txt"), "username");
        assertEquals("sample.txt", jobAppDoc.getFile().getName());
        assertTrue(jobAppDoc.getFile().exists());
        assertTrue(jobAppDoc.getFile().isFile());
        assertTrue(jobAppDoc.getFile().getParentFile().isDirectory());
        assertEquals("username", jobAppDoc.getFile().getParentFile().getName());
        jobAppDoc.getFile().delete();
    }

    @Test
    void testConstructor3() {
        // the username directory must be empty for this test to be true
        JobApplicationDocument jobAppDoc = new JobApplicationDocument("Hello", "CV", "username");
        assertEquals("CV.txt", jobAppDoc.getFile().getName());
        assertTrue(jobAppDoc.getFile().exists());
        assertTrue(jobAppDoc.getFile().isFile());
        assertTrue(jobAppDoc.getFile().getParentFile().isDirectory());
        assertTrue(jobAppDoc.getFile().getParentFile().getName().equals("username"));
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(jobAppDoc.getFile().getPath()));
            assertEquals("Hello", fileReader.readLine());
            assertNull(fileReader.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        jobAppDoc.getFile().delete();
    }

    @Test
    void testCopyFile() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./sample.txt"));
        jobAppDoc.copyFile("./files/sample.txt");
        assertTrue(new File("./files/sample.txt").exists());
        assertTrue(new File("./sample.txt").exists());
        assertNotEquals(new File("./files/sample.txt"), new File("./sample.txt"));
    }

    @Test
    void testCreateNewFile() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./test.txt"));
        File newFile = jobAppDoc.createNewFile("./testNew.txt", "Testing 1, 2, 3");
        assertTrue(newFile.exists());
        assertEquals("testNew.txt", newFile.getName());
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(newFile.getPath()));
            assertEquals("Testing 1, 2, 3", fileReader.readLine());
            assertNull(fileReader.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        newFile.delete();
    }

    @Test
    void testSeparateExtension() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./test.txt"));
        String[] components = jobAppDoc.separateExtension("./folder/subfolder/test.txt");
        assertEquals("./folder/subfolder/test", components[0]);
        assertEquals(".txt", components[1]);

        components = jobAppDoc.separateExtension("./folder/subfolder/test.index.html");
        assertEquals("./folder/subfolder/test.index", components[0]);
        assertEquals(".html", components[1]);

    }

    @Test
    void testGetNewFilePath() {
        JobApplicationDocument jobAppDoc = new JobApplicationDocument(new File("./test.txt"));
        String newFilePath = jobAppDoc.getNewFilePath("./sample.txt");
        assertEquals("./sample(1).txt", newFilePath);
        try {
            new File(newFilePath).createNewFile();
            newFilePath = jobAppDoc.getNewFilePath("./sample.txt");
            assertEquals("./sample(2).txt", newFilePath);
            new File("./sample(1).txt").delete();
            newFilePath = jobAppDoc.getNewFilePath("./sample.txt");
            assertEquals("./sample(1).txt", newFilePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}