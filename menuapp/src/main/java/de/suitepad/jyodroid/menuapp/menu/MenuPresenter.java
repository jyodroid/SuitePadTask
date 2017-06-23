package de.suitepad.jyodroid.menuapp.menu;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by johntangarife on 6/23/17.
 */

final class MenuPresenter {

    private static final String LOG_TAG = MenuPresenter.class.toString();
    private MenuView mView;

    MenuPresenter(MenuView menuView) {
        this.mView = menuView;
    }

    void setProxyValues(String ip, int port) {
        System.setProperty("http.proxyHost", ip);
//        System.setProperty("http.proxyHost", getIPAddress(true));
        System.setProperty("http.proxyPort", String.valueOf(port));
    }

    private String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (isIPv4)
                            return hostAddress;
                    }
                }
            }
        } catch (Exception ex) {
            mView.showException(ex);
            Log.e(LOG_TAG, "Exception setting local ip address: ", ex);
        } // for now eat exceptions
        return "";
    }
}
