package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

/**
 * Created by johntangarife on 6/22/17.
 */


//TODO: Use safe thread model for android.
//TODO: try to use dependency injection on mSocket
public class ServerThread extends Thread {

    private static final String LOG_TAG = ServerThread.class.getSimpleName();
    private final int BUFFER_SIZE = 32768;
    private Socket mSocket;

    public ServerThread(Socket socket) {
        super(ServerThread.class.getSimpleName());
        this.mSocket = socket;
    }

    public void run() {

        BufferedReader bufferedReader = null;
        DataOutputStream outputStream = null;
        BufferedReader inputStream = null;
        try {

            outputStream = new DataOutputStream(mSocket.getOutputStream());
            inputStream =
                    new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            String inputLine, outputLine;
            int counter = 0;
            String urlToCall = null;

            //Get request from client
            while ((inputLine = inputStream.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(inputLine);
                tokenizer.nextToken();

                if (counter == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    Log.d(LOG_TAG, "Url to be called " + urlToCall);
                }
                counter++;
            }

            // Resend request to real server
            // TODO: try to use Retrofit
            URL url = new URL(urlToCall);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);

            //Receive response
            InputStream responseInputStream = null;
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            if (urlConnection.getContentLength() > 0) {
                responseInputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(responseInputStream));
            }

            //Return response
            byte[] clientResponse = new byte[BUFFER_SIZE];
            int index = responseInputStream.read(clientResponse, 0, BUFFER_SIZE);

            while (index != -1) {
                outputStream.write(clientResponse, 0, BUFFER_SIZE);
            }
            outputStream.flush();


        } catch (IOException ioe) {
            Log.e(LOG_TAG, "Error retrieving data from socket", ioe);
        } catch (NullPointerException npe) {
            Log.e(LOG_TAG, "Some operation with null", npe);
        } finally {
            try {
                if (mSocket != null) {
                    mSocket.close();
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                Log.e(LOG_TAG, "Error retrieving data from socket", ioe);
            }
        }
    }


}
