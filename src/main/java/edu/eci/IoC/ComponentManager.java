package edu.eci.IoC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Base64;

@Component
public class ComponentManager {

    public static String filepath = "src\\main\\resource\\pepe-crying.png";
    public static String htmlPath = "src\\main\\resource\\fullHtml.html";
    @GetMapping("/hello")
    public static String getHello() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" +
                "Its Aliveeeeeeeeeeeee, its Aliveeeeeeeeeee";
    }

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

    @GetMapping("/host")
    public static String getHTMLPages() throws IOException{
        File file = new File(htmlPath);
        StringBuilder body = fromArchiveToString(file);
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n" + //
                "<html>\r\n" + //
                "    <head>\r\n" + //
                "        <meta charset=\"UTF-8\">\r\n" + //
                "        <title>File Adder</title>\r\n" + //
                "    </head>\r\n" + //
                "    <body>\r\n" + //
                "        <pre>" + body + "</pre>\r\n" + //
                "    </body>\r\n" + //
                "</html>";
    }
    /**
     * This method re write the file into a line by line String Builder
     * 
     * @param file
     * @return the file components in a StringBuilder
     * @throws IOException
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
