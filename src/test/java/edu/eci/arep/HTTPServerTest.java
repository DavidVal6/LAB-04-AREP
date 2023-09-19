package edu.eci.arep;
import org.junit.Before;
import org.junit.Test;

import edu.eci.arep.HTTPServer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class HTTPServerTest {

    private HTTPServer httpServer;

    @Before
    public void setUp() {
        httpServer = HTTPServer.getInstance();
    }

    @Test
    public void testGetHomeIndex() {
        String homeIndex = httpServer.getHomeIndex();
        assertNotNull(homeIndex);
        assertTrue(homeIndex.contains("UPLOAD A FILE"));
    }

    @Test
    public void testGetFileRemaster() {
        String filename = "test.txt"; // Ajusta el nombre del archivo según tu estructura de archivos
        String fileContent = httpServer.getFileRemaster(filename);
        assertNotNull(fileContent);
        assertTrue(fileContent.contains("Contenido del archivo de prueba")); // Ajusta el contenido esperado
    }

    @Test
    public void testToHTML() throws IOException {
        // Simulamos un archivo HTML existente en el sistema
        String filename = "test.html"; // Ajusta el nombre del archivo HTML en tu sistema
        String htmlContent = httpServer.toHTML(new File(filename));
        assertNotNull(htmlContent);
        assertTrue(htmlContent.contains("<html>")); // Ajusta la comprobación según el contenido esperado
    }

    @Test
    public void testToImage() throws IOException {
        // Simulamos un archivo de imagen existente en el sistema
        String filename = "test.jpg"; // Ajusta el nombre del archivo de imagen en tu sistema
        String imageContent = httpServer.toImage(new File(filename), "jpg");
        assertNotNull(imageContent);
        assertTrue(imageContent.contains("data:image/jpeg;base64,")); // Ajusta la comprobación según el contenido esperado
    }
}