package com.venue.venuewallet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.adapter.VenueWalletPagerAdapter;
import com.venue.venuewallet.holder.VenueWalletAccessTokenNotifier;
import com.venue.venuewallet.holder.VenueWalletCardsListNotifier;
import com.venue.venuewallet.holder.VenueWalletConfigNotifier;
import com.venue.venuewallet.model.EmvPaymentRequest;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.model.VenueWalletConfigResponse;
import com.venue.venuewallet.utils.FontTextView;

import java.util.ArrayList;

/**
 * Created by manager on 05-12-2017.
 */

public class VenueMyWalletFragment extends Fragment implements GetAccessToken, View.OnClickListener {

    public static VenueMyWalletFragment newInstance() {
        return new VenueMyWalletFragment();
    }

    public static VenueMyWalletFragment newInstance(VenueWalletConfigResponse response, String from) {
        VenueMyWalletFragment fragment = new VenueMyWalletFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_DETAILS, response);
        args.putString(FROM, from);
        fragment.setArguments(args);
        return fragment;
    }

    public static String ARG_CONFIG_DETAILS = "config_details_argument";
    public static String FROM = "from";
    TextView nocards_textview;
    LinearLayout dots_layout;
    ViewPager walletpager;
    ArrayList<VenueWalletCardsData> cardsList = new ArrayList<>();
    ArrayList<ImageView> dots = new ArrayList<ImageView>();
    ProgressBar progressBar;
    String TAG = VenueMyWalletFragment.class.getSimpleName();
    VenueWalletConfigResponse configResponse;
    RelativeLayout order_layout;
    Button paybutton;
    String from = "";
    View view;
    EmvPaymentRequest payRequest;
    FontTextView orderprice_textview;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            configResponse = (VenueWalletConfigResponse) getArguments().getSerializable(ARG_CONFIG_DETAILS);
            from = getArguments().getString("from");

        }
        if (from.equals("cart")) {
            view = inflater.inflate(R.layout.venue_wallet_fragment_dialog, container, false);


        } else {
            view = inflater.inflate(R.layout.venue_wallet_fragment, container, false);
        }


        initView(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void initView(View view) {

        nocards_textview = (TextView) view.findViewById(R.id.nocards_textview);
        dots_layout = (LinearLayout) view.findViewById(R.id.dots_layout);
        walletpager = (ViewPager) view.findViewById(R.id.walletpager);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        order_layout = (RelativeLayout) view.findViewById(R.id.order_layout);
        orderprice_textview = (FontTextView) view.findViewById(R.id.orderprice_textview);
        paybutton = (Button) view.findViewById(R.id.paybutton);
        paybutton.setOnClickListener(this);

        if (from.equals("cart")) {
            paybutton.setVisibility(View.VISIBLE);
            order_layout.setVisibility(View.VISIBLE);

            orderprice_textview.setText("" + VenueWalletManager.paymentRequest.getEmvOrderDetails().getTotalAmount());
        } else {
            paybutton.setVisibility(View.GONE);
            order_layout.setVisibility(View.GONE);
        }


        walletpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectWalletDot(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        callingCardsList();
        // callingAccessToken();
    }

    void callingAccessToken() {
        //Pass username and pwd as object
        VenueWalletManager.getInstance(getActivity()).getAuthToken(getActivity(), new VenueWalletAccessTokenNotifier() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void callingConfigData() {
        VenueWalletManager.getInstance(getActivity()).getWalletConfigData(getActivity(), new VenueWalletConfigNotifier() {
            @Override
            public void onConfigDataSuccess(VenueWalletConfigResponse response) {
                configResponse = response;
                callingCardsList();
            }

            @Override
            public void ononfigDataFailure() {
                callingCardsList();
            }

            @Override
            public void getRefreshTokenFailure() {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void callingCardsList() {
        VenueWalletManager.getInstance(getActivity()).getWalletCards(getActivity(), new VenueWalletCardsListNotifier() {
            @Override
            public void onCardsListSuccess(ArrayList<VenueWalletCardsData> list) {
                progressBar.setVisibility(View.GONE);
                cardsList = list;
                nocards_textview.setVisibility(View.GONE);
                VenueWalletPagerAdapter adapter = new VenueWalletPagerAdapter(listner, cardsList, configResponse, getActivity(), from);
                walletpager.setAdapter(adapter);
                if (dots != null) {
                    dots.clear();
                }
                addWalletDots();
                selectWalletDot(0);

            }

            @Override
            public void onCardsListFailure() {
                progressBar.setVisibility(View.GONE);
                nocards_textview.setVisibility(View.VISIBLE);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void getRefreshTokenFailure() {
                VenuetizeIdentityManager.getInstance(getActivity()).getRefreshToken(getActivity(), VenueMyWalletFragment.this);
            }
        });
    }

    VenueWalletPagerAdapter.onSelectedItemListner listner = new VenueWalletPagerAdapter.onSelectedItemListner() {
        @Override
        public void onClick() {

        }
    };


    public void addWalletDots() {

        int profileList = cardsList.size();
        for (int i = 0; i < profileList; i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.venue_wallet_unselecteddot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    25,
                    25
            );
            params.setMargins(7, 0, 7, 0);
            dots_layout.addView(dot, params);
            dots.add(dot);
        }

    }

    public void selectWalletDot(int idx) {
        if (cardsList != null && cardsList.size() > 0) {
            Resources res = getResources();
            int profileList = cardsList.size();
            for (int i = 0; i < profileList; i++) {
                try {
                    int drawableId = (i == idx) ? (R.drawable.venue_wallet_selecteddot) : (R.drawable.venue_wallet_unselecteddot);
                    Drawable drawable = res.getDrawable(drawableId);
                    dots.get(i).setImageDrawable(drawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        // VenueWalletManager.
    }

    @Override
    public void getRefreshTokenFailure() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.paybutton) {

            if (cardsList.size() > 0)
                VenueWalletManager.getInstance(getActivity()).getPreAuthOrder(getActivity(), cardsList.get(walletpager.getCurrentItem()).getId());


        } else {

        }
    }
}
