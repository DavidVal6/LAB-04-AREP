package edu.eci.IoC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Esta clase representa un administrador de componentes que proporciona varios servicios
 * web a través de anotaciones como `@GetMapping`. Proporciona métodos para obtener saludos,
 * mostrar imágenes y cargar páginas HTML.
 */
@Component
public class ComponentManager {

    /**
     * Ruta del archivo de imagen predeterminado.
     */
    public static String filepath = "src\\main\\resource\\pepe-crying.png";

    /**
     * Ruta del archivo HTML predeterminado.
     */
    public static String htmlPath = "src\\main\\resource\\fullHtml.html";

    /**
     * Obtiene una respuesta HTTP que contiene un saludo.
     *
     * @return Una cadena que representa una respuesta HTTP con un saludo.
     */
    @GetMapping("/hello")
    public static String getHello() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" +
                "Its Aliveeeeeeeeeeeee, its Aliveeeeeeeeeee";
    }

    /**
     * Obtiene una respuesta HTTP que contiene una imagen codificada en base64.
     *
     * @return Una cadena que representa una respuesta HTTP con una imagen.
     * @throws IOException Si ocurre un error al leer el archivo de imagen.
     */
    @GetMapping("/image")
    public static String getImage() throws IOException {
        File file = new File(filepath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>File Content</title>\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "         <center><h1>" + "Imagen Funcionando" + "</h1></center>" + "\r\n"
                + "         <center><img src=\"data:image/jpeg;base64," + base64 + "\" alt=\"image\"></center>" + "\r\n"
                + "    </body>\r\n"
                + "</html>";
    }

    /**
     * Obtiene una respuesta HTTP que muestra el contenido de un archivo HTML.
     *
     * @return Una cadena que representa una respuesta HTTP con el contenido HTML.
     * @throws IOException Si ocurre un error al leer el archivo HTML.
     */
    @GetMapping("/host")
    public static String getHTMLPages() throws IOException {
        File file = new File(htmlPath);
        StringBuilder body = fromArchiveToString(file);
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <meta charset=\"UTF-8\">\r\n"
                + "        <title>File Adder</title>\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <pre>" + body + "</pre>\r\n"
                + "    </body>\r\n"
                + "</html>";
    }

    /**
     * Este método reescribe el contenido de un archivo en un StringBuilder, línea por línea.
     *
     * @param file El archivo del que se va a leer el contenido.
     * @return Un StringBuilder que contiene el contenido del archivo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static StringBuilder fromArchiveToString(File file) throws IOException {
        StringBuilder body = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line).append("\n");
        }
        reader.close();
        return body;
    }
}
