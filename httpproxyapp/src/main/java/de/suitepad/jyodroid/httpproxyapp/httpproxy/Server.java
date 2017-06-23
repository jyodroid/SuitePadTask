package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by johntangarife on 6/22/17.
 */

public class Server {

    private static final int DEFAULT_PORT = 12340;
    private static final String LOG_TAG = Server.class.getSimpleName();

    private static void serve(int portNumber) {

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
//            Log.d(LOG_TAG, "Server started on port: " + portNumber);
            System.out.println(LOG_TAG + "Server started on port: " + portNumber);
            while (true) {
                new ServerThread(serverSocket.accept()).start();
            }


            //On finished??
//            serverSocket.close();
        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error creating socket on port " + portNumber, e);
            System.out.println(LOG_TAG + "Error creating socket on port " + portNumber);
            System.exit(-1);
        }
    }

    public static void main(String... args) {
        serve(DEFAULT_PORT);
    }

}
