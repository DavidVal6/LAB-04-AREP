package edu.eci.arep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import edu.eci.IoC.Component;
import edu.eci.IoC.GetMapping;



public class HTTPServer {

    public static File file;
    public static Socket clientSocket;
    public static PrintWriter out;
    public static Map<String,Method> services = new HashMap<>();
    public static final String pathToClasses = "edu/eci/IoC";
    private static OutputStream outputStream = null;
    private static HTTPServer instance = new HTTPServer();

    private HTTPServer() {
    }

    public static HTTPServer getInstance() {
        return instance;
    }

    public void start(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        inversionOfControl();
        ServerSocket serverSocket = createServerSocket();
    
        boolean running = true;
        while (running) {
            Socket clientSocket = acceptClientConnection(serverSocket);
            processClientRequest(clientSocket);
        }
    
        serverSocket.close();
    }
    
    private ServerSocket createServerSocket() {
        try {
            return new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
            return null; // Este retorno nunca se alcanza, pero evita un error de compilación.
        }
    }
    
    private Socket acceptClientConnection(ServerSocket serverSocket) {
        try {
            System.out.println("Listo para recibir ...");
            return serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
            return null; // Este retorno nunca se alcanza, pero evita un error de compilación.
        }
    }
    
    private void processClientRequest(Socket clientSocket) throws IOException, InvocationTargetException, IllegalAccessException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
    
            String request = readClientRequest(in);
            String response = handleClientRequest(request);
            sendServerResponse(out, response);
    
        } finally {
            clientSocket.close();
        }
    }
    
    private String readClientRequest(BufferedReader in) throws IOException {
        String inputLine, request = "/simple";
        String verb = "";
        boolean firstLine = true;
    
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (firstLine) {
                String[] requestTokens = inputLine.split(" ");
                if (requestTokens.length >= 2) {
                    request = requestTokens[1];
                    verb = requestTokens[0];
                }
                firstLine = false;
            }
            if (inputLine.contains("title?name")) {
                String[] firstSplit = inputLine.split("=");
                // uriString = firstSplit[1].split("HTTP")[0]; // No se usa uriString en este código
            }
            if (!in.ready()) {
                break;
            }
        }
    
        return verb + " " + request;
    }
    
    private String handleClientRequest(String request) throws InvocationTargetException, IllegalAccessException {
        String[] requestParts = request.split(" ");
        String verb = requestParts[0];
        String path = requestParts[1];
    
        if ("GET".equals(verb) && services.containsKey(path)) {
            return services.get(path).invoke(null).toString();
        } else {
            return getHomeIndex();
        }
    }
    
    private void sendServerResponse(PrintWriter out, String response) {
        out.println(response);
        out.close();
    }


    public static PrintWriter getOut() {
        return out;
    }

    public static String getFileRemaster(String inputString) {
        String path = "AREP-LAB3\\project3\\src\\main\\resource\\";
        return getTheArchive(inputString, path);
    }

    /**
     * This method will find the boundaries part in the response finding the name of
     * the file of it
     * so it can be search in the path that is has been selcted where the files will
     * be located
     * 
     * @param inputString is te receive respons where is the name of the file
     * @return it returns the html where the file is show depending the type of the
     *         file, also
     *         uses another mehthod where is search the type
     */
    public static String findBoundaries(String inputString) {
        System.out.println(inputString);
        String[] parts = inputString.split(";");
        String filename = null;
        for (String part : parts) {
            if (part.trim().startsWith("filename")) {
                // Extract the name parameter
                String[] nameParts = part.split("=");
                if (nameParts.length > 1) {
                    filename = nameParts[1].trim().replace("\"", "");
                }
            }
        }
        String path = "src\\main\\resource\\";
        return getTheArchive(filename, path);
    }

    /**
     * this method have the mission to getting the correct html using the extension
     * of the file
     * the important part is that it uses a switch thinking in possibility of
     * extension
     * 
     * @param filename is the name of the file
     * @param path     is the setted directory where the method will search the file
     * @return returns the html using another method depending in the extension of
     *         the file
     */
    public static String getTheArchive(String filename, String path) {
        String completePath = path + filename;
        file = new File(completePath);
        int extensionIndex = filename.lastIndexOf(".");
        String type = extensionIndex != -1 ? filename.substring(extensionIndex + 1) : null;
        if (file.exists()) {
            System.out.println("Existe");
            try {
                switch (type) {
                    case "html":
                        return toHTML(file);
                    case "txt":
                        return toHTML(file);
                    case "js":
                        return toJs(file);

                    case "css":
                        return toCSS(file);

                    case "jpg":
                        return toImage(file, type);
                    case "jpge":
                        return toImage(file, type);

                    case "jpeg":
                        return toImage(file, type);

                    case "png":
                        return toImage(file, type);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("NO existe");
        }
        return "404";
    }

    /**
     * This method its used to present a image file, the image files are made intro
     * base64
     * resource :
     * https://es.stackoverflow.com/questions/8334/porque-el-tama%C3%B1o-de-una-imagen-codificada-en-base64-es-diferente-al-original
     * 
     * @param file
     * @param type if is jpg,png etc
     * @return the HTML wwith the image if the image is big it will be a lit of bit
     *         slow
     * @throws IOException
     */
    public static String toImage(File file, String type) throws IOException {
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
                + "         <center><img src=\"data:image/jpeg;base64," + base64 + "\" alt=\"image\"></center>" + "\r\n"
                + "    </body>\r\n"
                + "</html>";
    }

    public static String toHTML(File file) throws IOException {
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

    public static String toCSS(File file) throws IOException {
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

    public static String toJs(File file) throws IOException {
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

    public static Socket getClientSocket() {
        return clientSocket;
    }


    private List<Class<?>> getClasses(){
        List<Class<?>> classes = new ArrayList<>();
        try{
            for (String cp: getClassP()){
                File file = new File(cp + "/" + pathToClasses);
                if(file.exists() && file.isDirectory()){
                    for (File cf: Objects.requireNonNull(file.listFiles())){
                        if(cf.isFile() && cf.getName().endsWith(".class")){
                            String rootTemp = pathToClasses.replace("/",".");
                            String className = rootTemp+"."+cf.getName().replace(".class","");
                            Class<?> clasS =  Class.forName(className);
                            classes.add(clasS);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    public ArrayList<String> getClassP(){
         String classPath = System.getProperty("java.class.path");
        String[] classPaths =  classPath.split(System.getProperty("path.separator"));
        return new ArrayList<>(Arrays.asList(classPaths));
    }
    
    public  void inversionOfControl() throws ClassNotFoundException{
        List<Class<?>> classes = getClasses();
        for (Class<?> clasS:classes){
            if(clasS.isAnnotationPresent(Component.class)){
                Class<?> c = Class.forName(clasS.getName());
                Method[] m = c.getMethods();
                for (Method me: m){
                    if(me.isAnnotationPresent(GetMapping.class)){
                        String key = me.getAnnotation(GetMapping.class).value();
                        services.put(key,me);
                    }
                }
            }

        }
    }

    public static String getHomeIndex() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>File Upload</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background-color: #f0f0ff;\n" +
                "            font-family: \"Ubuntu\", sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            text-align: center;\n" +
                "            margin-top: 50px;\n" +
                "        }\n" +
                "\n" +
                "        label, input[type=\"file\"], input[type=\"button\"] {\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>UPLOAD A FILE</h1>\n" +
                "    <form action=\"/upload\" method=\"POST\" enctype=\"multipart/form-data\">\n" +
                "        <label for=\"file\">Choose a file:</label><br>\n" +
                "        <input type=\"file\" id=\"file\" name=\"file\"><br><br>\n" +
                "        <input type=\"button\" value=\"Upload (POST)\" onclick=\"uploadFile('POST')\">\n" +
                "    </form>\n" +
                "\n" +
                "    <br><br>\n" +
                "\n" +
                "    <form action=\"/uploadGet\" method=\"GET\">\n" +
                "    <label for=\"fileGet\">Choose a file for GET request:</label><br>\n" +
                "    <input type=\"file\" id=\"fileGet\" name=\"file\"><br><br>\n" +
                "    <input type=\"button\" value=\"Upload (GET)\" onclick=\"uploadFile('GET')\">\n" +
                "</form>\n" +
                "\n" +
                "    <div id=\"uploadMsg\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "        function uploadFile(method) {\n" +
                "            const fileInput = method === 'POST' ? document.getElementById(\"file\") : document.getElementById(\"fileGet\");\n"
                +
                "            const file = fileInput.files[0];\n" +
                "            const formData = new FormData();\n" +
                "            formData.append(\"file\", file);\n" +
                "\n" +
                "            const xhr = new XMLHttpRequest();\n" +
                "            xhr.onload = function () {\n" +
                "                document.getElementById(\"uploadMsg\").innerHTML = this.responseText;\n" +
                "            };\n" +
                "            xhr.open(method, \"/upload\");\n" +
                "            xhr.send(formData);\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

}