package com.venue.venuetizeecommerce;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.venue.emkitmanager.EmkitMaster;
import com.venue.loyaltymanager.EmkitLoyaltyMaster;
import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.EmkitResponseNotifier;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venueidentity.model.UserData;
import com.venue.venuetizeecommerce.utils.DataModel;
import com.venue.venuetizeecommerce.utils.DrawerItemCustomAdapter;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.holder.PayWalletNotifier;
import com.venue.venuewallet.model.EmvOrderDetails;
import com.venue.venuewallet.model.EmvPaymentItem;
import com.venue.venuewallet.model.EmvPaymentRequest;
import com.venue.venuewallet.model.EmvPaymentSummary;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EmkitResponseNotifier, GetAccessToken {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Fetching device token by registering with GCM/FCM
        initFirebase();
        EmkitMaster.getInstance(getApplicationContext()).init(null);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.drawable.emkit_header_logo, "Authorize Token");
        drawerItem[1] = new DataModel(R.drawable.emkit_header_logo, "Wallet");
        drawerItem[2] = new DataModel(R.drawable.emkit_header_logo, "Pay");
        drawerItem[3] = new DataModel(R.drawable.emkit_header_logo, "Skidata");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

    }

    @Override
    public void eMKitLoginSuccess() {

    }

    @Override
    public void eMKitLoginFailure() {

    }

    @Override
    public void getAccessTokenSuccess(String accessToken) {

    }

    @Override
    public void getAccessTokenFailure() {
        Log.e("Main App", "getAccessTokenFailure From Main App :: ");
    }

    @Override
    public void getRefreshTokenFailure() {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void selectItem(int position) {

        if (position == 0) {
            UserData user = new UserData("android@example.com", "android54321");
            getIdentityManager().initeMWallet(user, new GetAccessToken() {
                @Override
                public void getAccessTokenSuccess(String accessToken) {
                    Log.d("", "getAccessTokenSuccess: " + accessToken);
                }

                @Override
                public void getAccessTokenFailure() {

                }

                @Override
                public void getRefreshTokenFailure() {
                    //Show the signin screen to the user and initiate the auth cal by passing userdata
                }
            });
        } else if (position == 1) {
            getWalletManager().callingPayWallet(MainActivity.this);
        } else if (position == 2) {
            //Follow the document to prepare EmvPaymentRequest object and pass in the sdk api
            ArrayList<EmvPaymentItem> paymentItems = new ArrayList<EmvPaymentItem>();
            EmvPaymentItem paymentItem = new EmvPaymentItem();
            paymentItem.setItemTitle("Chicken");
            paymentItem.setAmount("8.00");
            paymentItems.add(paymentItem);
            EmvPaymentSummary paymentSummary = new EmvPaymentSummary(paymentItems);
            //paymentSummary.setPaymentItems(paymentItems);
            String placeId = "" + System.currentTimeMillis();
            EmvOrderDetails orderDetails = new EmvOrderDetails("" + System.currentTimeMillis(), "", placeId, 0, (float) 45, paymentItems);
            EmvPaymentRequest emvPayRequest = new EmvPaymentRequest(orderDetails, paymentSummary);

            getWalletManager().payWitheMWallet(MainActivity.this, emvPayRequest, new PayWalletNotifier() {

                @Override
                public void onPayChargeSuccess() {

                }

                @Override
                public void onPayChargeFailure() {

                }

                @Override
                public void onPreAuthSuccess(String Id, String HMacToken) {
                    //process hmac token and upon order success with te2, call the below statement to venuetize sdk
                    getWalletManager().getChargeOrder(Id);
                }

                @Override
                public void onPreAuthFailure() {

                }
            });
        }else if (position == 3) {
            getLoyaltyMaster().callSkidata(MainActivity.this);
        }

        mDrawerList.setItemChecked(position, true);

        mDrawerList.setSelection(position);
        //setTitle(mNavigationDrawerItemTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


    private void initFirebase() {
        FirebaseApp.initializeApp(this);
    }

    private VenuetizeIdentityManager getIdentityManager() {
        return VenuetizeIdentityManager.getInstance(getApplicationContext());
    }

    private VenueWalletManager getWalletManager() {
        return VenueWalletManager.getInstance(getApplicationContext());
    }

    private EmkitLoyaltyMaster getLoyaltyMaster() {
        return EmkitLoyaltyMaster.getInstance(getApplicationContext());
    }


}
