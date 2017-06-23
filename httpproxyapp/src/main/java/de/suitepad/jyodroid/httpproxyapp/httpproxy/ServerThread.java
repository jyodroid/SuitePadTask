package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;

    private String sourceUrl = "https://gist.githubusercontent.com/Rio517/5c95cc6402da8c5e37bc579111e14350/raw/b8ac727658a2aae2a4338d1cb7b1e91aca6288db/z_output.json";

    public ServerThread(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            int cnt = 0;
            String urlToCall = "";
            ///////////////////////////////////
            //begin get request from client
            while ((inputLine = in.readLine()) != null) {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }
                //parse the first line of the request to find the url
                if (cnt == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    //can redirect this to output log
                    System.out.println("Request for : " + urlToCall);
                }

                cnt++;
            }
            //end get request from client
            ///////////////////////////////////


            BufferedReader rd = null;
            try {
                //System.out.println("sending request
                //to real server for url: "
                //        + urlToCall);
                ///////////////////////////////////
                //begin send request to server, get response from server
                URL url = new URL(urlToCall);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                //not doing HTTP posts
                conn.setDoOutput(false);
                //System.out.println("Type is: "
                //+ conn.getContentType());
                //System.out.println("content length: "
                //+ conn.getContentLength());
                //System.out.println("allowed user interaction: "
                //+ conn.getAllowUserInteraction());
                //System.out.println("content encoding: "
                //+ conn.getContentEncoding());
                //System.out.println("content type: "
                //+ conn.getContentType());

                // Get the response
                InputStream is = null;

//                HttpURLConnection huc = (HttpURLConnection)conn;
                if (conn.getContentLength() > 0) {
                    try {
                        is = conn.getInputStream();
                        rd = new BufferedReader(new InputStreamReader(is));
                    } catch (IOException ioe) {
                        System.out.println(
                                "********* IO EXCEPTION **********: " + ioe);
                    }
                }

                //end send request to server, get response from server
                ///////////////////////////////////

                ///////////////////////////////////
                //begin send response to client
                File testFile = new File("/Users/johntangarife/Documents/StudioProjects/SuitePadTask/datasourceapp/src/main/assets/sample.json");

                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: text/json\n");
                out.writeBytes("Content-Length: " + testFile.length() + "\n\n");

                is = new FileInputStream(testFile);
                byte by[] = new byte[BUFFER_SIZE];
                int index = is.read(by, 0, BUFFER_SIZE);

                while (index != -1) {
                    out.write(by, 0, index);
                    index = is.read(by, 0, BUFFER_SIZE);
                }
                out.flush();

                //end send response to client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                e.printStackTrace();
                //encountered error - just send nothing back, so
                //processing can continue
                out.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
