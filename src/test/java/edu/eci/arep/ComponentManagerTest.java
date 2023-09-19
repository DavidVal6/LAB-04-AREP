package edu.eci.arep;
import org.junit.Before;
import org.junit.Test;

import edu.eci.IoC.ComponentManager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class ComponentManagerTest {

    private ComponentManager componentManager;

    @Before
    public void setUp() {
        componentManager = new ComponentManager();
    }

    @Test
    public void testGetHello() {
        String helloResponse = ComponentManager.getHello();
        assertNotNull(helloResponse);
        assertTrue(helloResponse.contains("HTTP/1.1 200 OK"));
        assertTrue(helloResponse.contains("Its Aliveeeeeeeeeeeee, its Aliveeeeeeeeeee"));
    }

    @Test
    public void testGetImage() {
        try {
            String imageResponse = ComponentManager.getImage();
            assertNotNull(imageResponse);
            assertTrue(imageResponse.contains("HTTP/1.1 200 OK"));
            assertTrue(imageResponse.contains("<center><h1>Imagen Funcionando</h1></center>"));
            assertTrue(imageResponse.contains("data:image/jpeg;base64,"));
        } catch (IOException e) {
            fail("IOException occurred while testing getImage: " + e.getMessage());
        }
    }

    @Test
    public void testGetHTMLPages() {
        try {
            String htmlResponse = ComponentManager.getHTMLPages();
            assertNotNull(htmlResponse);
            assertTrue(htmlResponse.contains("HTTP/1.1 200 OK"));
            assertTrue(htmlResponse.contains("<!DOCTYPE html>"));
            assertTrue(htmlResponse.contains("<pre>"));
        } catch (IOException e) {
            fail("IOException occurred while testing getHTMLPages: " + e.getMessage());
        }
    }

    @Test
    public void testFromArchiveToString() {
        try {
            String filePath = "src/main/resource/hello.html"; // Ajusta la ruta a un archivo HTML de prueba en tu sistema
            File testFile = new File(filePath);
            StringBuilder body = ComponentManager.fromArchiveToString(testFile);
            assertNotNull(body);
            assertTrue(body.toString().contains("<html>")); // Ajusta la comprobación según el contenido esperado
        } catch (IOException e) {
            fail("IOException occurred while testing fromArchiveToString: " + e.getMessage());
        }
    }
}