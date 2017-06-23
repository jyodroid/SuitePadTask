package de.suitepad.jyodroid.menuapp.network;

import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import de.suitepad.jyodroid.menuapp.menu.MenuView;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by johntangarife on 6/23/17.
 */

public class MenuWebViewClient extends WebViewClient {

    private static final String LOG_TAG = MenuWebViewClient.class.toString();
    private static final String MIME_TYPE = "text/json";
    private static final String CODING = "UTF-8";

    private final OkHttpClient client = new OkHttpClient();
    private MenuView mView;

    public MenuWebViewClient(MenuView MenuView) {
        mView = MenuView;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
        final Uri url = request.getUrl();
        try {
            Response response = redirect(url.toString(), request);
            return new WebResourceResponse(MIME_TYPE, CODING, response.body().byteStream());

        } catch (IOException ioe) {
            mView.showException(ioe);
            Log.e(LOG_TAG, "Problem returning response from proxy", ioe);
        } catch (Exception e) {
            mView.showException(e);
            Log.e(LOG_TAG, "Problem redirecting to proxy", e);
        }

        return super.shouldInterceptRequest(view, request);
    }

    private Response redirect(String url, WebResourceRequest webRequest) throws Exception {

        Request request = new Request.Builder()
                .headers(Headers.of(webRequest.getRequestHeaders()))
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            mView.showException(e);
            Log.e(LOG_TAG, "Problem calling to proxy", e);
        }

        return response;
    }
}
