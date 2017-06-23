package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import android.util.Log;

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


final class ProxyServer {
    private static final int BUFFER_SIZE = 4 * 1024;
    private static final String LOG_TAG = ProxyServer.class.getSimpleName();

    private Socket socket;

    private String sourceUrl = "https://gist.githubusercontent.com/Rio517/5c95cc6402da8c5e37bc579111e14350/raw/b8ac727658a2aae2a4338d1cb7b1e91aca6288db/z_output.json";

    ProxyServer(Socket socket) {
        this.socket = socket;
    }

    public void serve() {
        DataOutputStream response = null;
        BufferedReader in = null;
        try {
            response =
                    new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String clientUrl = getClientRequestURL(in);
            if (clientUrl != null) {
                File cachedJson = findCachedJSON(clientUrl);
                InputStream is = null;

                try {
                    if (cachedJson != null) {
                        File testFile = new File("/Users/johntangarife/Documents/StudioProjects/SuitePadTask/datasourceapp/src/main/assets/sample.json");
                        is = new FileInputStream(testFile);

                        response.writeBytes("HTTP/1.1 200 OK\n");
                        response.writeBytes("Content-Type: text/json\n");
                        response.writeBytes("Content-Length: " + testFile.length() + "\n\n");

                    } else {
                        redirectConnectionToServer(clientUrl, is);
                        byte by[] = new byte[BUFFER_SIZE];
                        int index = is.read(by, 0, BUFFER_SIZE);

                        while (index != -1) {
                            response.write(by, 0, index);
                            index = is.read(by, 0, BUFFER_SIZE);
                        }
                        response.flush();
                    }

                    sendResponseToClient(response, is);

                } catch (Exception e) {
                    System.err.println("Encountered exception: " + e);
                    e.printStackTrace();
                    response.writeBytes("");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close response all resources
            try {
                if (response != null) {
                    response.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing resources", e);
            }
        }
    }

    /**
     * Return response to client request
     *
     * @param response from original request
     * @param is       Stream with response body
     */
    private void sendResponseToClient(DataOutputStream response, InputStream is) throws IOException {
        byte by[] = new byte[BUFFER_SIZE];
        int index = is.read(by, 0, BUFFER_SIZE);

        while (index != -1) {
            response.write(by, 0, index);
            index = is.read(by, 0, BUFFER_SIZE);
        }
        response.flush();
    }

    /**
     * Make petition to original destination
     *
     * @param clientUrl original request url
     * @return Buffered reader with response
     * @throws IOException
     */
    private void redirectConnectionToServer(String clientUrl, InputStream inputStream)
            throws IOException {
        URL url = new URL(clientUrl);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(false);
        BufferedReader br = null;

        if (conn.getContentLength() > 0) {
            try {
                inputStream = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream));
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        }
    }

    private File findCachedJSON(String url) {
        return null;
    }

    /**
     * Get request url from client
     *
     * @param in socket input stream
     * @return original String url
     * @throws IOException
     */
    private String getClientRequestURL(BufferedReader in) throws IOException {
        int counter = 0;
        String inputLine;
        String requestUrl = null;
        while ((inputLine = in.readLine()) != null) {
            StringTokenizer tok = new StringTokenizer(inputLine);
            tok.nextToken();

            //parse the first line of the request to find the url
            if (counter == 0) {
                String[] tokens = inputLine.split(" ");
                requestUrl = tokens[1];
                Log.d(LOG_TAG, "Call from: " + requestUrl);
            }
            counter++;
        }
        return requestUrl;
    }
}
