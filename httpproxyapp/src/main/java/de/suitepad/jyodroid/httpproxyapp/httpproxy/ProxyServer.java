package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

/**
 * Created by johntangarife on 6/22/17.
 */


final class ProxyServer {
    private static final int BUFFER_SIZE = 4 * 1024;
    private static final String LOG_TAG = ProxyServer.class.getSimpleName();

    private Socket mSocket;
    private Context mContext;

    ProxyServer(Socket socket, Context context) {
        this.mSocket = socket;
        this.mContext = context;
    }

    void serve() {

        DataOutputStream response = null;
        BufferedReader income = null;

        try {
            response =
                    new DataOutputStream(mSocket.getOutputStream());
            income = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));

            String originalUrl = getOriginalUrl(income);
            String searchedFileName = getSerchedFileName(originalUrl);
            String searchedFileContent = getCacheSearchedFile(mContext, searchedFileName);

            if (!searchedFileContent.isEmpty()) {
                responseWithCachedData(response, searchedFileContent);
            } else {
                responseWithOriginalRequest(response, originalUrl);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in server", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in server", e);
        } catch (URISyntaxException e) {
            Log.e(LOG_TAG, "Error in server", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (income != null) {
                    income.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Make the original request
     *
     * @param response    to send to client
     * @param originalUrl from original request
     * @throws IOException
     */
    private void responseWithOriginalRequest(DataOutputStream response, String originalUrl) throws
            IOException {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(originalUrl);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);

            InputStream callIncome = null;

            try {
                if (urlConnection.getContentLength() > 0) {
                    callIncome = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(callIncome));
                }
            } catch (IOException ioe) {
                Log.e(LOG_TAG, "Connection error", ioe);
            }
            byte by[] = new byte[BUFFER_SIZE];
            int index = callIncome.read(by, 0, BUFFER_SIZE);

            while (index != -1) {
                response.write(by, 0, index);
                index = callIncome.read(by, 0, BUFFER_SIZE);
            }
            response.flush();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Encountered exception: " + e);
            //encountered error - just send nothing back, soprocessing can continue
            response.writeBytes("");
            response.flush();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    /**
     * respond with cached data to client
     *
     * @param response            to send to client
     * @param searchedFileContent data to send to client
     * @throws IOException
     */
    private void responseWithCachedData(DataOutputStream response, String searchedFileContent)
            throws IOException {

        response.writeBytes("HTTP/1.1 200 OK\n");
        response.writeBytes("Content-Type: text/json\n");
        response.writeBytes("Content-Length: " +
                searchedFileContent.getBytes().length + "\n\n");
        response.writeBytes(searchedFileContent);
        response.writeBytes("\n\n");
        response.flush();
    }

    /**
     * Find the cached file and return content
     *
     * @param context          from service
     * @param searchedFileName name of file to find on cache
     * @return content of the searched file
     * @throws JSONException
     */
    private String getCacheSearchedFile(Context context, String searchedFileName) throws JSONException {
        String[] projection = new String[]{
                "content"
        };

        Uri uri = Uri.parse("content://de.suitepad.jyodroid.datasourceapp/cached_file");

        ContentResolver cr = context.getContentResolver();
        Cursor cursor =
                cr.query(Uri.withAppendedPath(uri, searchedFileName), projection, null, null, null);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            JSONArray object = new JSONArray(cursor.getString(0));
            cursor.close();
            return object.toString();
        }

        return "";
    }

    /**
     * Obtain from url name of the searched file
     *
     * @param originalUrl from original request
     * @return name of the searched file
     * @throws URISyntaxException
     */
    private String getSerchedFileName(String originalUrl) throws URISyntaxException {
        String[] segments = new URI(originalUrl).getPath().split("/");
        return segments[segments.length - 1];
    }

    /**
     * Obtain the url from the original request
     *
     * @param income buffer of original request
     * @return
     * @throws IOException
     */
    private String getOriginalUrl(BufferedReader income) throws IOException {

        String inputLine = income.readLine();
        if (inputLine != null) {
            StringTokenizer tok = new StringTokenizer(inputLine);
            tok.nextToken();

            String[] tokens = inputLine.split(" ");
            return tokens[1];
        }

        return "";
    }
}