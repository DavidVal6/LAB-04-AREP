package edu.eci.arep;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerExecuter {
    public static void main(String[] args) {
        HTTPServer httpServer = HTTPServer.getInstance();
        if(httpServer != null){
            try {
                httpServer.start(args);
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
