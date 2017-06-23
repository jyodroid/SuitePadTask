package de.suitepad.jyodroid.menuapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    private ViewHolder mViewHolder;
    private final OkHttpClient client = new OkHttpClient();

    @BindView(R.id.menu_root_view)
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        mViewHolder = new ViewHolder(mRootView);

        System.setProperty("http.proxyHost", "192.168.0.15");
        System.setProperty("http.proxyPort", "12340");
//        System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");

        mViewHolder.mainWebView.setWebViewClient(new myWebClient());
        mViewHolder.mainWebView.loadUrl("file:///android_asset/sample.html");
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
            final Uri url = request.getUrl();
            try {

//                Proxy proxyTest = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getIPAddress(true), 12344));

//                OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxyTest);
//                OkHttpClient client = builder.build();

                Response response = run(url.toString(), request);

                WebResourceResponse wr =
                        new WebResourceResponse("text/json", "UTF-8",
                                response.body().byteStream());


//                WebResourceResponse wr =
//                        new WebResourceResponse("text/json", "UTF-8",
//                                getAssets().open("initial_data.json"));

                return wr;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return super.shouldInterceptRequest(view, request);
        }
    }


    static class ViewHolder {

        @BindView(R.id.main_web_view)
        WebView mainWebView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            setWebSettings();
        }

        private void setWebSettings() {
            mainWebView.clearCache(true);
            WebSettings webSettings = mainWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Response run(String url, WebResourceRequest webRequest) throws Exception {

        Headers headers = Headers.of(webRequest.getRequestHeaders());

        Request request = new Request.Builder()
                .headers(headers)
//                .url("https://gist.githubusercontent.com/Rio517/5c95cc6402da8c5e37bc579111e14350/raw/b8ac727658a2aae2a4338d1cb7b1e91aca6288db/z_output.json")
                .url(url)
                .build();

        Response response = null;
        try {
            response = client
                    .newCall(request)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response;

    }

}
