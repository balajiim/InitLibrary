package com.venue.venuewallet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.adapter.VenueWalletExpandableAdapter;
import com.venue.venuewallet.holder.VenueWalletCardsListNotifier;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.model.VenueWalletConfigResponse;

import java.util.ArrayList;

/**
 * Created by manager on 05-12-2017.
 */

public class VenueWalletDetailsFragment extends Fragment implements GetAccessToken {

    ExpandableListView walletexpandablistview;
    String TAG = VenueWalletDetailsFragment.class.getSimpleName();
    ArrayList<VenueWalletCardsData> cardsList;
    ArrayList<Integer> childtotallist = new ArrayList<Integer>();
    ArrayList<String> headerList = new ArrayList<String>();
    ProgressBar progressBar;
    public static String ARG_CONFIG_DETAILS = "config_details_argument";
    VenueWalletConfigResponse configResponse;

    public static VenueWalletDetailsFragment newInstance() {
        return new VenueWalletDetailsFragment();
    }

    public static VenueWalletDetailsFragment newInstance(VenueWalletConfigResponse response) {
        VenueWalletDetailsFragment fragment = new VenueWalletDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_DETAILS, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_wallet_details_fragment, container, false);

        initView(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void initView(View view) {
        if (getArguments() != null) {
            configResponse = (VenueWalletConfigResponse) getArguments().getSerializable(ARG_CONFIG_DETAILS);
        }
        walletexpandablistview = (ExpandableListView) view.findViewById(R.id.walletexpandablistview);
        walletexpandablistview.setGroupIndicator(null);
        walletexpandablistview.setChildIndicator(null);
        walletexpandablistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        callingCardsList();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void callingCardsList() {
        VenueWalletManager.getInstance(getActivity()).getWalletCards(getActivity(), new VenueWalletCardsListNotifier() {
            @Override
            public void onCardsListSuccess(ArrayList<VenueWalletCardsData> list) {
                progressBar.setVisibility(View.GONE);
                cardsList = list;
                if (headerList != null) {
                    headerList.clear();
                    childtotallist.clear();
                }
                headerList.add(getResources().getString(R.string.venue_wallet_debitcard));
                childtotallist.add(cardsList.size());
                VenueWalletExpandableAdapter adapter = new VenueWalletExpandableAdapter(listner, headerList, childtotallist, cardsList, getActivity(), walletexpandablistview, configResponse);
                walletexpandablistview.setAdapter(adapter);
                int count = adapter.getGroupCount();
                for (int i = 0; i < count; i++)
                    walletexpandablistview.expandGroup(i);
            }

            @Override
            public void onCardsListFailure() {
                progressBar.setVisibility(View.GONE);
                if (headerList != null) {
                    headerList.clear();
                    childtotallist.clear();
                }
                headerList.add(getResources().getString(R.string.venue_wallet_debitcard));
                childtotallist.add(1);
                VenueWalletExpandableAdapter adapter = new VenueWalletExpandableAdapter(listner, headerList, childtotallist, cardsList, getActivity(), walletexpandablistview, configResponse);
                walletexpandablistview.setAdapter(adapter);
                int count = adapter.getGroupCount();
                for (int i = 0; i < count; i++)
                    walletexpandablistview.expandGroup(i);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void getRefreshTokenFailure() {
                VenuetizeIdentityManager.getInstance(getActivity()).getRefreshToken(getActivity(), VenueWalletDetailsFragment.this);
            }
        });
    }

    VenueWalletExpandableAdapter.onSelectedItemListner listner = new VenueWalletExpandableAdapter.onSelectedItemListner() {
        @Override
        public void onAddClick() {
            callingAddWallet();
        }

        @Override
        public void onEditClick(String id) {
            callingEditWallet(id);
        }
    };

    void callingAddWallet() {
        // getting terms and condition in local
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);

        boolean flag = sharedpreferences.getBoolean("terms", false);
        if (flag) {
            VenueWalletManager.getInstance(getActivity()).getFragmentFromLibraryadd(getActivity(), VenueWalletAddCardFragment.newInstance());
        } else {
            VenueWalletManager.getInstance(getActivity()).getFragmentFromLibraryadd(getActivity(), VenueWalletTCFragment.newInstance());
        }
    }

    void callingEditWallet(String id) {
        // getting terms and condition in local
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);

        boolean flag = sharedpreferences.getBoolean("terms", false);
        if (flag) {
            VenueWalletManager.getInstance(getActivity()).getFragmentFromLibraryadd(getActivity(), VenueWalletAddCardFragment.newInstance(id));
        } else {
            VenueWalletManager.getInstance(getActivity()).getFragmentFromLibraryadd(getActivity(), VenueWalletTCFragment.newInstance(id));
        }
    }

    @Override
    public void getAccessTokenSuccess(String accessToken) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        // Storing AccesToken in local
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("AcessToken", accessToken);
        editor.commit();

        callingCardsList();
    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }
}
