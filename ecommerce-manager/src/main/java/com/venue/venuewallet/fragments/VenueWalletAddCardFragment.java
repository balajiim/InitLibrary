package com.venue.venuewallet.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.venue.venueidentity.VenuetizeIdentityManager;
import com.venue.venueidentity.holder.GetAccessToken;
import com.venue.venuewallet.R;
import com.venue.venuewallet.VenueWalletManager;
import com.venue.venuewallet.holder.VenueWalletAddCardNotifier;
import com.venue.venuewallet.holder.VenueWalletEditCardNotifier;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.utils.Utility;
import com.venue.venuewallet.utils.VenueWalletMonthYearPicker;

import java.util.HashMap;

/**
 * Created by manager on 05-12-2017.
 */

public class VenueWalletAddCardFragment extends Fragment implements GetAccessToken {

    String cardId = null;

    public static VenueWalletAddCardFragment newInstance() {
        return new VenueWalletAddCardFragment();
    }

    public static VenueWalletAddCardFragment newInstance(String id) {
        Bundle arg = new Bundle();
        arg.putString("cardId", id);
        VenueWalletAddCardFragment f = new VenueWalletAddCardFragment();
        f.setArguments(arg);
        return f;
    }

    EditText wallet_card_holder_last_name, wallet_card_number, wallet_card_holder_name, wallet_card_cvv, wallet_card_expiry;
    EditText wallet_card_address, wallet_card_city, wallet_card_state, wallet_card_zipcode;
    LinearLayout addresslayout, citylayout, sub_state_layout, sub_stateedit_layout;
    View bottomview;
    Button savebtn, removebtn;
    VenueWalletMonthYearPicker myp;
    int year, month;
    ProgressBar progressBar;
    VenueWalletCardsData walletCardsData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_wallet_add_fragment, container, false);

        initView(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void initView(View view) {
        wallet_card_number = (EditText) view.findViewById(R.id.wallet_card_number);
        wallet_card_holder_name = (EditText) view.findViewById(R.id.wallet_card_holder_name);
        wallet_card_holder_last_name = (EditText) view.findViewById(R.id.wallet_card_holder_last_name);
        wallet_card_expiry = (EditText) view.findViewById(R.id.wallet_card_expiry);
        wallet_card_cvv = (EditText) view.findViewById(R.id.wallet_card_cvv);
        wallet_card_address = (EditText) view.findViewById(R.id.wallet_card_address);
        wallet_card_city = (EditText) view.findViewById(R.id.wallet_card_city);
        wallet_card_state = (EditText) view.findViewById(R.id.wallet_card_state);
        wallet_card_zipcode = (EditText) view.findViewById(R.id.wallet_card_zipcode);
        addresslayout = (LinearLayout) view.findViewById(R.id.addresslayout);
        citylayout = (LinearLayout) view.findViewById(R.id.citylayout);
        sub_state_layout = (LinearLayout) view.findViewById(R.id.sub_state_layout);
        sub_stateedit_layout = (LinearLayout) view.findViewById(R.id.sub_stateedit_layout);
        bottomview = (View) view.findViewById(R.id.bottomview);
        savebtn = (Button) view.findViewById(R.id.savebtn);
        removebtn = (Button) view.findViewById(R.id.removebtn);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        myp = new VenueWalletMonthYearPicker(getActivity());
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedmonth = myp.getSelectedMonthName();
                year = myp.getSelectedYear();
                month = Integer.parseInt(myp.getSelectedMonthName());
                if (selectedmonth != null && selectedmonth.trim().length() == 1)
                    selectedmonth = "0" + selectedmonth;
                wallet_card_expiry.setText(selectedmonth + "/" + myp.getSelectedYear());
            }
        }, null);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (savebtn.getText().toString().equals(getString(R.string.venue_wallet_add)))
                    addcardProcess();
                else
                    editCardProcess();
            }
        });
        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (walletCardsData != null)
                    deleteCardProcess();
            }
        });

        wallet_card_holder_last_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == 0 || actionId == EditorInfo.IME_ACTION_NEXT) {
                    wallet_card_expiry.requestFocus();
                    myp.show();
                    return true;
                }
                return false;
            }
        });
        wallet_card_expiry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == 0 || actionId == EditorInfo.IME_ACTION_NEXT) {
                    wallet_card_cvv.requestFocus();
                    myp.show();
                    return true;
                }
                return false;
            }
        });
        wallet_card_expiry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myp.show();
                    return true;
                }
                return false;
            }
        });

        if (getArguments() != null) {
            cardId = getArguments().getString("cardId", null);
            if (cardId != null) {
                //calling card deatils...
                progressBar.setVisibility(View.VISIBLE);
                getCardDetials(cardId);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void getCardDetials(String cardId) {
        VenueWalletManager.getInstance(getActivity()).getCardDetails(getActivity(), cardId, new VenueWalletEditCardNotifier() {
            @Override
            public void onEditCardSuccess(VenueWalletCardsData cardsData) {
                walletCardsData = cardsData;
                progressBar.setVisibility(View.GONE);
                wallet_card_number.setText(cardsData.getLast_4());
                wallet_card_holder_name.setText(cardsData.getFirst_name());
                wallet_card_holder_last_name.setText(cardsData.getLast_name());
                wallet_card_expiry.setText(cardsData.getExp_month() + "/" + cardsData.getExp_year());
                wallet_card_address.setText(cardsData.getAddress());
                wallet_card_city.setText(cardsData.getCity());
                wallet_card_state.setText(cardsData.getState());
                wallet_card_zipcode.setText(cardsData.getZip());
                wallet_card_number.setEnabled(false);
                savebtn.setText(getString(R.string.venue_wallet_save));
                removebtn.setVisibility(View.VISIBLE);
                addresslayout.setVisibility(View.GONE);
                citylayout.setVisibility(View.GONE);
                sub_state_layout.setVisibility(View.GONE);
                sub_stateedit_layout.setVisibility(View.GONE);
                bottomview.setVisibility(View.GONE);
                if (cardsData.getExp_year() != null && !cardsData.getExp_year().equals(""))
                    year = Integer.parseInt(cardsData.getExp_year());
                if (cardsData.getExp_month() != null && !cardsData.getExp_month().equals(""))
                    month = Integer.parseInt(cardsData.getExp_month());
            }

            @Override
            public void onEditCardsFailure() {
                progressBar.setVisibility(View.GONE);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void getRefreshTokenFailure() {
                VenuetizeIdentityManager.getInstance(getActivity()).getRefreshToken(getActivity(), VenueWalletAddCardFragment.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void addcardProcess() {
        Utility utility = new Utility();
        if (wallet_card_number.getText().toString().equals("") || wallet_card_number.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_cardnumber), getActivity());
        } else if (wallet_card_holder_name.getText().toString().equals("") || wallet_card_holder_name.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_firstname), getActivity());
        } else if (wallet_card_holder_last_name.getText().toString().equals("") || wallet_card_holder_last_name.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_lastname), getActivity());
        } else if (wallet_card_expiry.getText().toString().equals("") || wallet_card_expiry.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_expirydate), getActivity());
        } else if (wallet_card_cvv.getText().toString().equals("") || wallet_card_cvv.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_cvvcode), getActivity());
        } else if (wallet_card_address.getText().toString().equals("") || wallet_card_address.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_address), getActivity());
        } else if (wallet_card_city.getText().toString().equals("") || wallet_card_city.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_city), getActivity());
        } else if (wallet_card_state.getText().toString().equals("") || wallet_card_state.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_state), getActivity());
        } else if (wallet_card_zipcode.getText().toString().equals("") || wallet_card_zipcode.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_zipcode), getActivity());
        } else {
            utility.emkitButtonLogEvent(getActivity(), getResources().getString(R.string.venue_wallet_log_cat_wallet), savebtn.getText().toString());
            //Adding values into array map
            HashMap<String, Object> map = new HashMap<>();
            map.put("card_number", wallet_card_number.getText().toString());
            map.put("cvv", wallet_card_cvv.getText().toString());
            map.put("first_name", wallet_card_holder_name.getText().toString());
            map.put("last_name", wallet_card_holder_last_name.getText().toString());
            map.put("address", wallet_card_address.getText().toString());
            map.put("city", wallet_card_city.getText().toString());
            map.put("state", wallet_card_state.getText().toString());
            map.put("zip", wallet_card_zipcode.getText().toString());
            map.put("exp_month", month);
            map.put("exp_year", year);
            progressBar.setVisibility(View.VISIBLE);

            VenueWalletManager.getInstance(getActivity()).getWalletAddCardCard(getActivity(), map, new VenueWalletAddCardNotifier() {
                @Override
                public void onAddCardSuccess() {
                    progressBar.setVisibility(View.GONE);
                    showInfoDialog(getString(R.string.venue_wallet_add_card_successfull_msg), "exit");
                }

                @Override
                public void onAddCardsFailure() {
                    progressBar.setVisibility(View.GONE);
                    showInfoDialog(getString(R.string.venue_wallet_add_card_successfull_msg), "no");
                }

                @Override
                public void getRefreshTokenFailure() {

                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void editCardProcess() {
        Utility utility = new Utility();
        if (wallet_card_holder_name.getText().toString().equals("") || wallet_card_holder_name.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_firstname), getActivity());
        } else if (wallet_card_holder_last_name.getText().toString().equals("") || wallet_card_holder_last_name.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_lastname), getActivity());
        } else if (wallet_card_expiry.getText().toString().equals("") || wallet_card_expiry.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_expirydate), getActivity());
        } else if (wallet_card_cvv.getText().toString().equals("") || wallet_card_cvv.getText().toString().length() == 0) {
            utility.showInfoDialog(getResources().getString(R.string.venue_wallet_add_alert_cvvcode), getActivity());
        } else {
            utility.emkitButtonLogEvent(getActivity(), getResources().getString(R.string.venue_wallet_log_cat_wallet), savebtn.getText().toString());
            //Adding values into array map
            HashMap<String, Object> map = new HashMap<>();
            map.put("cvv", wallet_card_cvv.getText().toString());
            map.put("first_name", wallet_card_holder_name.getText().toString());
            map.put("last_name", wallet_card_holder_last_name.getText().toString());
            map.put("address", wallet_card_address.getText().toString());
            map.put("city", wallet_card_city.getText().toString());
            map.put("state", wallet_card_state.getText().toString());
            map.put("zip", wallet_card_zipcode.getText().toString());
            map.put("exp_month", month);
            map.put("exp_year", year);
            progressBar.setVisibility(View.VISIBLE);

            VenueWalletManager.getInstance(getActivity()).getWalletUpdateCard(getActivity(), walletCardsData.getId(), map, new VenueWalletAddCardNotifier() {
                @Override
                public void onAddCardSuccess() {
                    progressBar.setVisibility(View.GONE);
                    showInfoDialog(getString(R.string.venue_wallet_edit_card_successfull_msg), "exit");

                }

                @Override
                public void onAddCardsFailure() {
                    progressBar.setVisibility(View.GONE);
                    showInfoDialog(getString(R.string.venue_wallet_edit_card_unsuccessfull_msg), "no");
                }

                @Override
                public void getRefreshTokenFailure() {

                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void deleteCardProcess() {
        progressBar.setVisibility(View.VISIBLE);
        new Utility().emkitButtonLogEvent(getActivity(), getResources().getString(R.string.venue_wallet_log_cat_wallet), removebtn.getText().toString());
        VenueWalletManager.getInstance(getActivity()).getWalletDeleteCard(getActivity(), walletCardsData.getId(), new VenueWalletAddCardNotifier() {
            @Override
            public void onAddCardSuccess() {
                progressBar.setVisibility(View.GONE);
                showInfoDialog(getString(R.string.venue_wallet_delete_card_successfull_msg), "exit");
            }

            @Override
            public void onAddCardsFailure() {
                progressBar.setVisibility(View.GONE);
                showInfoDialog(getString(R.string.venue_wallet_delete_card_unsuccessfull_msg), "no");
            }

            @Override
            public void getRefreshTokenFailure() {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getAccessTokenSuccess(String accessToken) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(VenueWalletManager.WALLETPREFERENCES, Context.MODE_PRIVATE);
        // Storing AccesToken in local
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("AcessToken", accessToken);
        editor.commit();

        if (getArguments() != null) {
            cardId = getArguments().getString("cardId", null);
            if (cardId != null) {
                //calling card deatils...
                progressBar.setVisibility(View.VISIBLE);
                getCardDetials(cardId);
            }
        }

    }

    @Override
    public void getAccessTokenFailure() {

    }

    @Override
    public void getRefreshTokenFailure() {

    }

    public void showInfoDialog(String msg, final String exit) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(),
                        R.style.AboutDialog));

        alertDialog.setTitle("");
        alertDialog.setPositiveButton(getResources().getString(R.string.venue_wallet_ok_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (exit.equals("exit")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        alertDialog.setMessage(msg);
        AlertDialog alertDialog1 = alertDialog.create();

        alertDialog1.show();
    }
}
