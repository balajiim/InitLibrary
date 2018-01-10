package com.venue.venuewallet.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class VenueWalletPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<VenueWalletCardsData> cardsList;
    VenueWalletConfigResponse configResponse;
    onSelectedItemListner notifier;
    String from;

    public VenueWalletPagerAdapter(onSelectedItemListner notifier, ArrayList<VenueWalletCardsData> cardsList, VenueWalletConfigResponse configResponse, Context context, String from) {
        this.context = context;
        this.cardsList = cardsList;
        this.configResponse = configResponse;
        this.notifier = notifier;
        this.from = from;
    }

    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View pagerView;

        if (from.equalsIgnoreCase("cart")) {
            pagerView = inflater.inflate(R.layout.venue_wallet_pager_item_dialog, container, false);
        } else {
            pagerView = inflater.inflate(R.layout.venue_wallet_pager_item, container, false);
        }

        TextView cardname_textview = (TextView) pagerView.findViewById(R.id.cardname_textview);
        TextView cardnumber_textview = (TextView) pagerView.findViewById(R.id.cardnumber_textview);
        TextView carddesc_textview = (TextView) pagerView.findViewById(R.id.carddesc_textview);
        ImageView wallet_card_imageview = (ImageView) pagerView.findViewById(R.id.wallet_card_imageview);

        VenueWalletCardsData cardsData = cardsList.get(position);

        cardnumber_textview.setText(context.getString(R.string.venue_wallet_card_mask_default) + "-" + cardsData.getLast_4());

        wallet_card_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifier.onClick();
            }
        });

        //getting cards data from configdata
        if (configResponse != null && configResponse.getConfig_dict() != null && configResponse.getConfig_dict().getCreditCards() != null && configResponse.getConfig_dict().getCreditCards().size() > 0) {
            for (int i = 0; i < configResponse.getConfig_dict().getCreditCards().size(); i++) {
                if (configResponse.getConfig_dict().getCreditCards().get(i).getOperator() != null) {
                    if (configResponse.getConfig_dict().getCreditCards().get(i).getOperator().equals(cardsData.getOperator())) {
                        cardname_textview.setText(configResponse.getConfig_dict().getCreditCards().get(i).getCardName());
                        carddesc_textview.setText(configResponse.getConfig_dict().getCreditCards().get(i).getCardDescription());
                        int resourceID1 = Utility.getResourceId(configResponse.getConfig_dict().getCreditCards().get(i).getCardImage().toLowerCase(), context);
                        if (resourceID1 != 0)
                            Picasso.with(context).load(resourceID1).resize(519, 340).centerInside().into(wallet_card_imageview);
                    }
                }
            }
        }

        ((ViewPager) container).addView(pagerView);
        return pagerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }

    public interface onSelectedItemListner {
        void onClick();
    }
}
