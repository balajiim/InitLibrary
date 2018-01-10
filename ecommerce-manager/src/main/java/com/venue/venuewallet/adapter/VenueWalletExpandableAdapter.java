package com.venue.venuewallet.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.venue.venuewallet.R;
import com.venue.venuewallet.model.VenueWalletCardsData;
import com.venue.venuewallet.model.VenueWalletConfigResponse;
import com.venue.venuewallet.utils.Utility;

import java.util.ArrayList;

/**
 * Created by manager on 07-12-2017.
 */

public class VenueWalletExpandableAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<VenueWalletCardsData> cardsList;
    ArrayList<Integer> childtotallist;
    ArrayList<String> headerList;
    ExpandableListView expandableListView;
    onSelectedItemListner notifier;
    VenueWalletConfigResponse configResponse;

    public VenueWalletExpandableAdapter(onSelectedItemListner notifier, ArrayList<String> headerList, ArrayList<Integer> childtotallist, ArrayList<VenueWalletCardsData> cardsList, Context context, ExpandableListView expandableListView, VenueWalletConfigResponse configResponse) {
        this.notifier = notifier;
        this.headerList = headerList;
        this.childtotallist = childtotallist;
        this.cardsList = cardsList;
        this.context = context;
        this.expandableListView = expandableListView;
        this.configResponse = configResponse;
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childtotallist.get(i);
    }

    @Override
    public Object getGroup(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return i1;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.venue_wallet_cards_header_items, null);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title_textView);
            ImageView info_imageView = (ImageView) convertView.findViewById(R.id.info_imageView);
            TextView add_button = (TextView) convertView.findViewById(R.id.add_button);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            txtTitle.setText(headerList.get(position));

            if (expandableListView.isGroupExpanded(position)) {
                imageView
                        .setBackgroundResource(R.drawable.venue_wallet_collapse_arrow);
            } else {
                imageView
                        .setBackgroundResource(R.drawable.venue_wallet_normalstate_arrow);
            }

            if (headerList.size() > 0 && headerList.get(position).equals(context.getResources().getString(R.string.venue_wallet_debitcard))) {
                add_button.setVisibility(View.VISIBLE);
            } else {
                add_button.setVisibility(View.GONE);
            }

            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifier.onAddClick();
                }
            });

        }

        return convertView;
    }

    @Override
    public View getChildView(int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.venue_wallet_cards_child_items, null);
        }
        ImageView master_imageView = (ImageView) convertView.findViewById(R.id.master_imageView);
        final TextView txtTitle = (TextView) convertView.findViewById(R.id.title_textView);
        final TextView blance_textView = (TextView) convertView.findViewById(R.id.blance_textView);
        TextView walletnocard = (TextView) convertView.findViewById(R.id.walletnocard);
        ImageView edit_imageView = (ImageView) convertView.findViewById(R.id.edit_imageView);

        if (headerList.size() > 0 && headerList.get(i).equals(context.getResources().getString(R.string.venue_wallet_debitcard))) {
            if (cardsList != null && cardsList.size() > 0) {
                txtTitle.setText(context.getString(R.string.venue_wallet_card_mask_default) + "-" + cardsList.get(i1).getLast_4());
            } else {
                walletnocard.setVisibility(View.VISIBLE);
                edit_imageView.setVisibility(View.GONE);
            }
        }

        edit_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardsList != null && cardsList.size() > 0) {
                    notifier.onEditClick(cardsList.get(i1).getId());
                }
            }
        });

        //getting cards data from configdata
        if (cardsList != null && cardsList.size() > 0) {
            if (configResponse != null && configResponse.getConfig_dict() != null && configResponse.getConfig_dict().getCreditCards() != null && configResponse.getConfig_dict().getCreditCards().size() > 0) {
                for (int k = 0; k < configResponse.getConfig_dict().getCreditCards().size(); k++) {
                    if (configResponse.getConfig_dict().getCreditCards().get(k).getOperator() != null) {
                        if (configResponse.getConfig_dict().getCreditCards().get(k).getOperator().equals(cardsList.get(i1).getOperator())) {
                            int resourceID1 = Utility.getResourceId(configResponse.getConfig_dict().getCreditCards().get(k).getManagewallletcardImage().toLowerCase(), context);
                            if (resourceID1 != 0)
                                Picasso.with(context).load(resourceID1).resize(519, 340).centerInside().into(master_imageView);

                            break;
                        }
                    } else {
                        int resourceID1 = Utility.getResourceId("", context);
                        if (resourceID1 != 0)
                            Picasso.with(context).load(resourceID1).resize(519, 340).centerInside().into(master_imageView);
                        break;
                    }
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public interface onSelectedItemListner {
        void onAddClick();

        void onEditClick(String id);
    }
}
