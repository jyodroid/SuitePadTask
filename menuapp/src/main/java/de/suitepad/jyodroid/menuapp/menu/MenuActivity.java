package de.suitepad.jyodroid.menuapp.menu;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.suitepad.jyodroid.menuapp.BuildConfig;
import de.suitepad.jyodroid.menuapp.R;
import de.suitepad.jyodroid.menuapp.network.MenuWebViewClient;

public class MenuActivity extends AppCompatActivity implements MenuView {

    private ViewHolder mViewHolder;
    private static final String LOCAL_PAGE_PATH = "file:///android_asset/sample.html";
    private MenuPresenter mPresenter;

    @BindView(R.id.menu_root_view)
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        mViewHolder = new ViewHolder(mRootView, this);

        mPresenter = new MenuPresenter(this);
        mPresenter.setProxyValues("172.23.2.183", BuildConfig.PROXY_PORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewHolder.mainWebView.loadUrl(LOCAL_PAGE_PATH);
    }

    @Override
    public void showException(Exception e) {
        Snackbar.make(mRootView, "Something went wrong: " + e.getMessage(), Snackbar.LENGTH_LONG)
                .show();
    }

    static class ViewHolder {

        @BindView(R.id.main_web_view)
        WebView mainWebView;

        public ViewHolder(View view, MenuView menuView) {
            ButterKnife.bind(this, view);
            setWebSettings(menuView);
        }

        private void setWebSettings(MenuView menuView) {
            mainWebView.clearCache(true);
            mainWebView.setWebViewClient(new MenuWebViewClient(menuView));
            WebSettings webSettings = mainWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
    }
}
