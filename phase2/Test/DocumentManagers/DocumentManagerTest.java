package DocumentManagers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {

    @Test
    void testConstructor() {
        DocumentManager documentManager = new DocumentManager();
        assertNull(documentManager.getFolder());
        File folder = new File(DocumentManager.INITIAL_PATH + "/test/test2");
        documentManager.setFolder(folder);
        assertEquals("test", documentManager.getFolder().getName());
    }
}