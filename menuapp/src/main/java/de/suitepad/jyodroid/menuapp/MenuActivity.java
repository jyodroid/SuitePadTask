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
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    private ViewHolder mViewHolder;

    @BindView(R.id.menu_root_view)
    View mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

//        HttpHost proxy = new HttpHost("someproxy", 8080);

//        setKitKatWebViewProxy(this, getIPAddress(true), 12340);

        ViewHolder mViewHolder = new ViewHolder(mRootView);


        mViewHolder.mainWebView.setWebViewClient(new myWebClient());


        // Simplest usage: note that an exception will NOT be thrown
        // if there is an error loading this page (see below).
//        mViewHolder.mainWebView.loadUrl("https://google.com/");
        mViewHolder.mainWebView.loadUrl("file:///android_asset/sample.html");

        // OR, you can also load from an HTML string:

//        String summary = "<html><body>You scored <b>192</b> points.</body></html>";

//        mViewHolder.mainWebView.loadData(summary, "text/html", null);

        // ... although note that there are restrictions on what this HTML can do.
        // See the JavaDocs for loadData() and loadDataWithBaseURL() for more info.


    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String method = request.getMethod();
            Uri url = request.getUrl();
            Map<String, String> headers = request.getRequestHeaders();

            try {
//                if (url.equals(new URL("http://someremoteurl.com/sample.json"))) {
                WebResourceResponse wr = new WebResourceResponse("text/json", "UTF-8", getAssets().open("initial_data.json"));

                return wr;
//                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

//            return super.shouldInterceptRequest(view, request);
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
            WebSettings webSettings = mainWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
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
}
