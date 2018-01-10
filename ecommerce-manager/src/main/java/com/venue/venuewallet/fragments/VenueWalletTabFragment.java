package com.venue.venuewallet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.holder.VenueWalletConfigNotifier;
import com.venue.venuewallet.model.EmvPaymentRequest;
import com.venue.venuewallet.model.VenueWalletConfigResponse;


/**
 * Created by manager on 05-12-2017.
 */

public class VenueWalletTabFragment extends Fragment implements GetAccessToken {

    public static VenueWalletTabFragment newInstance() {
        return new VenueWalletTabFragment();
    }

    public static VenueWalletTabFragment newInstance(String from) {
        VenueWalletTabFragment fragment = new VenueWalletTabFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    public RelativeLayout mywalletlayout, detailswalletLayout, header_layout;
    public TextView mywallet_textView, detailswallet_textView;
    public View mywallet_belowview, detailswallet_belowview;
    ProgressBar progressBar;
    String TAG = VenueWalletTabFragment.class.getSimpleName();
    VenueWalletConfigResponse configResponse;
    String from = null;
    EmvPaymentRequest paymentClass;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_wallet_tab_fragment, container, false);

        initView(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void initView(View view) {

        if (getArguments() != null) {
            from = getArguments().getString("from");
            paymentClass = (EmvPaymentRequest) getArguments().getSerializable("paymentClass");
        }
        if (from == null) {
            from = "wallet";
        }
        header_layout = view.findViewById(R.id.header_layout);
        mywalletlayout = view.findViewById(R.id.mywalletlayout);
        detailswalletLayout = view.findViewById(R.id.detailswalletLayout);
        mywallet_textView = view.findViewById(R.id.mywallet_textView);
        detailswallet_textView = view.findViewById(R.id.detailswallet_textView);
        mywallet_belowview = view.findViewById(R.id.mywallet_belowview);
        detailswallet_belowview = view.findViewById(R.id.detailswallet_belowview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        callingAccessToken();
        mywalletlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callingMyWallet();
            }
        });
        detailswalletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callingWalletDetails();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void callingAccessToken() {
        //Pass username and pwd as object
        VenuetizeIdentityManager.getInstance(getActivity()).getAuthToken(getActivity(), VenueWalletTabFragment.this, VenueWalletManager.accessContext);
       /* VenueWalletManager.getInstance(getActivity()).getAuthToken(getActivity(), new VenueWalletAccessTokenNotifier() {
            @Override
            public void onAccessTokenSuccess() {
                Log.e(TAG, "onAccessTokenSuccess");
                callingConfigData();
            }

            @Override
            public void onAccessTokenFailure() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void getRefreshTokenFailure() {

            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void callingConfigData() {
        VenueWalletManager.getInstance(getActivity()).getWalletConfigData(getActivity(), new VenueWalletConfigNotifier() {
            @Override
            public void onConfigDataSuccess(VenueWalletConfigResponse response) {
                progressBar.setVisibility(View.GONE);
                configResponse = response;
                callingMyWallet();
            }

            @Override
            public void ononfigDataFailure() {
                progressBar.setVisibility(View.GONE);
                callingMyWallet();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void getRefreshTokenFailure() {

                VenuetizeIdentityManager.getInstance(getActivity()).getRefreshToken(getActivity(), VenueWalletTabFragment.this);
            }
        });
    }

    void callingMyWallet() {
        mywallet_textView.setTextColor(getResources().getColor(R.color.venue_wallet_tab_text_selected_color));
        detailswallet_textView.setTextColor(getResources().getColor(R.color.venue_wallet_tab_text_unselected_color));
        mywalletlayout.setBackgroundColor(getResources().getColor(R.color.venue_wallet_tab_selected_color));
        detailswalletLayout.setBackgroundColor(getResources().getColor(R.color.venue_wallet_tab_unselected_color));
        mywallet_belowview.setVisibility(View.GONE);
        detailswallet_belowview.setVisibility(View.VISIBLE);
        VenueWalletManager.getInstance(getActivity()).getFragmentFromContentNonstackreplace(getActivity(), VenueMyWalletFragment.newInstance(configResponse, from));
    }

    void callingWalletDetails() {
        mywallet_textView.setTextColor(getResources().getColor(R.color.venue_wallet_tab_text_unselected_color));
        detailswallet_textView.setTextColor(getResources().getColor(R.color.venue_wallet_tab_text_selected_color));
        mywalletlayout.setBackgroundColor(getResources().getColor(R.color.venue_wallet_tab_unselected_color));
        detailswalletLayout.setBackgroundColor(getResources().getColor(R.color.venue_wallet_tab_selected_color));
        mywallet_belowview.setVisibility(View.VISIBLE);
        detailswallet_belowview.setVisibility(View.GONE);
        VenueWalletManager.getInstance(getActivity()).getFragmentFromContent(getActivity(), VenueWalletDetailsFragment.newInstance(configResponse));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getAccessTokenSuccess(String accessToken) {

        Log.e(TAG, "getAccessTokenSuccess Success " + accessToken);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        // Storing AccesToken in local
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("AcessToken", accessToken);
        editor.commit();
        callingConfigData();

    }

    @Override
    public void getAccessTokenFailure() {
        progressBar.setVisibility(View.GONE);
        Log.e(TAG, "getAccessTokenFailure ");
    }

    @Override
    public void getRefreshTokenFailure() {
        progressBar.setVisibility(View.GONE);
        Log.e(TAG, "getRefreshTokenFailure ");
    }
}
