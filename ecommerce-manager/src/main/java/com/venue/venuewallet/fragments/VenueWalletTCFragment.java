package com.venue.venuewallet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.utils.Utility;

/**
 * Created by manager on 05-12-2017.
 */

public class VenueWalletTCFragment extends Fragment implements GetAccessToken {

    String cardId = null;

    public static VenueWalletTCFragment newInstance() {
        return new VenueWalletTCFragment();
    }

    public static VenueWalletTCFragment newInstance(String id) {
        Bundle arg = new Bundle();
        arg.putString("cardId", id);
        VenueWalletTCFragment f = new VenueWalletTCFragment();
        f.setArguments(arg);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_wallet_tc_fragment, container, false);

        initView(view);

        return view;
    }

    void initView(View view) {
        if (getArguments() != null) {
            cardId = getArguments().getString("cardId", null);
        }
        WebView webView = view.findViewById(R.id.webview);
        final Button declinebtn = view.findViewById(R.id.declinebtn);
        final Button acceptbtn = view.findViewById(R.id.acceptbtn);
        declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utility().emkitButtonLogEvent(getActivity(), getResources().getString(R.string.venue_wallet_log_cat_wallet), declinebtn.getText().toString());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utility().emkitButtonLogEvent(getActivity(), getResources().getString(R.string.venue_wallet_log_cat_wallet), acceptbtn.getText().toString());
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
                // Storing terms and condition in local
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("terms", true);
                editor.commit();
                getActivity().getSupportFragmentManager().popBackStack();
                if (cardId == null) {
                    VenueWalletManager.getInstance(getActivity()).getFragmentFromLibrary(getActivity(), VenueWalletAddCardFragment.newInstance());
                } else {
                    VenueWalletManager.getInstance(getActivity()).getFragmentFromLibrary(getActivity(), VenueWalletAddCardFragment.newInstance(cardId));
                }
            }
        });
        setWebViewProperties(webView);
    }

    private void setWebViewProperties(WebView webView) {
        webView.setWebViewClient(new MainWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.loadUrl(getResources().getString(R.string.venue_wallet_tc_url));

    }

    @Override
    public void getAccessTokenSuccess(String accessToken) {

    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }

    private class MainWebViewClient extends WebViewClient {

        public void onPageFinished(WebView webView, String url) {
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }
    }
}

