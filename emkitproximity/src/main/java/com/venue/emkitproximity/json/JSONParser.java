package com.venue.emkitproximity.json;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.venue.emkitproximity.R;
import com.venue.emkitproximity.activity.EmkitNotificationActivity;
import com.venue.emkitproximity.holder.BeaconReceiverDetails;
import com.venue.emkitproximity.holder.CardGimbalDetails;
import com.venue.emkitproximity.holder.IniteMkitDetails;
import com.venue.emkitproximity.utils.Logger;
import com.venue.emkitproximity.utils.StoreDetails;
import com.venue.initv2.EmkitInitialization;
import com.venue.initv2.EmkitInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;

public class JSONParser {

    private final static String TAG = "JSONParser";

//	private static String BASEURL_REST_API = "";

    private static Context mContext;

    public static String USER_AGENT;

    public static boolean inProcess = false;

    public static void setContext(Context context) {
        mContext = context;
    }


    public CardGimbalDetails getSightingsDetails(String url, String emp2UserId, String transmitter_id, String trigger_event, String beacon_name) {

        Logger.d(TAG, "sighting url is::" + url);

        Context applicationContext = JSONParser.mContext;

        Logger.d(TAG, "sighting url is 1::" + applicationContext);
        SharedPreferences pref = applicationContext.getSharedPreferences("emkit_info", 0);

        SharedPreferences beacon_pref = EmkitInstance.getContext()
                .getSharedPreferences("beacon_info", 0);
        String firstSighted = beacon_pref.getString("" + beacon_name + "first_sighted", "0");
        String lastSighted = beacon_pref.getString("" + beacon_name + "last_sighted", "0");
        String batteryLevel = beacon_pref.getString("" + beacon_name + "battery_level", "0");
        Long difference = (Long.parseLong(lastSighted) - Long.parseLong(firstSighted));
        Logger.i(TAG, "dwelltime " + difference);
        //Log.i(TAG,"trigger_event::"+trigger_event);
        //Logger.d(TAG,"APPPPPP"+pref.getString("eMkitAPIKey", null));
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        Logger.d(TAG, "sighting url is 2::" + pref.getString("eMcard_verno", null));
        hashMap.put("receiver_name", transmitter_id);
        hashMap.put("receiver_id", emp2UserId);
        hashMap.put("receiver_owner_id", emp2UserId);
        hashMap.put("transmitter_name", beacon_name);
        hashMap.put("transmitter_id", transmitter_id);
        hashMap.put("transmitter_owner_id", "");
        hashMap.put("triggering_event", trigger_event);
        hashMap.put("dwell_time", "" + difference / 1000);
        hashMap.put("battery_level", batteryLevel);
        hashMap.put("signal_strength", "-1");
        hashMap.put("time", getUTCDateAndTime(applicationContext)); //Utility.getUTCDateAndTime(mContext)
        hashMap.put("eMcard_verno", pref.getString("eMcard_verno", null));
        hashMap.put("eMkitAPIKey", pref.getString("eMkitAPIKey", null));
        hashMap.put("fyxUserId", pref.getString("gimbal_user_id", null));
        Logger.i("JSONParser::transmitter_id ", transmitter_id);
        Logger.i("JSONParser::beacon_name ", beacon_name);
        //Log.i(TAG,"trigger_event::"+trigger_event);
        //Log.i(TAG,"sightings time::"+Utility.getUTCDateAndTime(mContext));
        HTTPPoster httpPoster = new HTTPPoster();
        try {

            String eMkitAPIKey = pref.getString("eMkitAPIKey", null);
            ResponseHolder responseHolder = httpPoster.doPostasBytes(url, hashMap, eMkitAPIKey);
            if (responseHolder != null) {
                String response = responseHolder.getResponseStr();

                Logger.d(TAG, "sightings details " + response);
                if (response != null && response.trim().length() != 0) {
                    JSONObject jsonObjResponse = (JSONObject) new JSONTokener(response).nextValue();
                    if (jsonObjResponse.has("status")) {
                        String status = jsonObjResponse.getString("status");
                        if (status.equalsIgnoreCase("Sighted") || status.equalsIgnoreCase("Departed")) {
                            return new CardGimbalDetails();
                        }
                    } else if (jsonObjResponse.has("gcm")) {//aps
                        CardGimbalDetails gimbalDetails = new CardGimbalDetails();
                        Logger.d(TAG, "sightings details inside gcm ");
                        /*Editor editor=pref.edit();
                        editor.putString("campaign_id", campaign_id);
						editor.putString("campaign_name", campaign_name); 
						editor.commit();
						Logger.d(TAG,"campaign id is::"+campaign_id);
						Logger.d(TAG,"campaign name is::"+campaign_name);*/
                        JSONObject apsjsonObject = jsonObjResponse.getJSONObject("gcm");
                        if (apsjsonObject.has("data")) {
                            JSONObject dataJsonObject = apsjsonObject.getJSONObject("data");


                            if (dataJsonObject.has("type")) {
                                Logger.d(TAG, "sightings details inside type ");

                                if (dataJsonObject.getString("type").equalsIgnoreCase("deeplink")) {
                                    Logger.d(TAG, "sightings details inside deeplink ");

                                    Intent notificationIntent = new Intent(EmkitInstance.getContext(),
                                            EmkitNotificationActivity.class);
                                    // Logger.d(TAG,"1");
                                    Bundle bundle = new Bundle();
                                    // Logger.d(TAG,"2");
                                    // Logger.d(TAG,"notification : final emkit_URL - " +
                                    // emkit_url);
                                    bundle.putString("emkit_url", "");
                                    bundle.putString("campaign_id", "");
                                    bundle.putString("instantpush_id", "");
                                    bundle.putString("finalurl", dataJsonObject.getString("link"));
                                    bundle.putString("linktype", "deeplink");
                                    // Logger.d(TAG,"3");
                                    notificationIntent.putExtras(bundle);
                                    if (!EmkitInstance.getInstance(EmkitInstance.getContext()).isApplicationInBackground()) {
                                        EmkitInitialization emkit = EmkitInstance.getInstance(mContext);
                                        emkit.launchDeeplink(mContext, dataJsonObject.getString("link"));
                                    } else {

                                        {

                                            Logger.d(TAG, "4");
                                            int notificationType = (int) System
                                                    .currentTimeMillis();
                                            // Logger.d(TAG,"5");
                                            notificationIntent
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent pendingIntent = PendingIntent
                                                    .getActivity(EmkitInstance.getContext(), notificationType,
                                                            notificationIntent, 0);
                                            // Logger.d(TAG,"6");
                                            Bitmap icon = null;
                                            /*int app_icon=0;
                                            try {
												PackageManager pm = context.getPackageManager();
												Drawable ico =context. getApplicationInfo().loadIcon(context.getPackageManager()); 
												icon=((BitmapDrawable)ico).getBitmap();
												app_icon=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}*/

                                            SharedPreferences pref1 = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                                            int notificationResource = pref1.getInt("EmkitNotificationResource", 0);


                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                                    EmkitInstance.getContext())
                                                    .setContentTitle(dataJsonObject.getString("title"))
                                                    .setContentText(dataJsonObject.getString("message"))
                                                    .setAutoCancel(true)
                                                    //.setLargeIcon(icon)
                                                    .setSmallIcon(notificationResource)
                                                    .setContentIntent(pendingIntent)
                                                    .setWhen(System.currentTimeMillis())
                                                    // .setTicker("Card Notification")
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(dataJsonObject.getString("message")))
                                                    .setSound(
                                                            RingtoneManager
                                                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                    .setVibrate(new long[]{1000});
                                            // Logger.d(TAG,"7");
                                            NotificationManager notificationManager = (NotificationManager) EmkitInstance.getContext()
                                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                                            // Logger.d(TAG,"8");
                                            notificationManager.notify(notificationType,
                                                    mBuilder.build());
                                            // Logger.d(TAG,"9");

                                        }

                                    }

                                } else if (dataJsonObject.getString("type").equalsIgnoreCase("audio")) {
                                    Logger.d(TAG, "sightings details inside audio ");
                                    EmkitInitialization emkit = EmkitInstance.getInstance(mContext);
                                    emkit.launchAudio(mContext, dataJsonObject.getString("link"));
                                } else if (dataJsonObject.getString("type").equalsIgnoreCase("video")) {
                                    Logger.d(TAG, "sightings details inside video ");
                                    EmkitInitialization emkit = EmkitInstance.getInstance(mContext);
                                    emkit.launchVideo(mContext, dataJsonObject.getString("link"));
                                } else if (dataJsonObject.getString("type").equalsIgnoreCase("link")) {
                                    Logger.d(TAG, "sightings details inside link ");
                                    Intent notificationIntent = new Intent(EmkitInstance.getContext(),
                                            EmkitNotificationActivity.class);
                                    // Logger.d(TAG,"1");
                                    Bundle bundle = new Bundle();
                                    // Logger.d(TAG,"2");
                                    // Logger.d(TAG,"notification : final emkit_URL - " +
                                    // emkit_url);
                                    bundle.putString("emkit_url", "");
                                    bundle.putString("campaign_id", "");
                                    bundle.putString("instantpush_id", "");
                                    bundle.putString("finalurl", dataJsonObject.getString("link"));
                                    bundle.putString("linktype", "link");
                                    // Logger.d(TAG,"3");
                                    notificationIntent.putExtras(bundle);
                                    if (!EmkitInstance.getInstance(EmkitInstance.getContext()).isApplicationInBackground()) {
                                        EmkitInitialization emkit = EmkitInstance.getInstance(mContext);
                                        emkit.launchDeeplink(mContext, dataJsonObject.getString("link"));
                                    } else {

                                        {

                                            Logger.d(TAG, "4");
                                            int notificationType = (int) System
                                                    .currentTimeMillis();
                                            // Logger.d(TAG,"5");
                                            notificationIntent
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent pendingIntent = PendingIntent
                                                    .getActivity(EmkitInstance.getContext(), notificationType,
                                                            notificationIntent, 0);
                                            // Logger.d(TAG,"6");
                                            Bitmap icon = null;
                                            /*int app_icon=0;
                                            try {
												PackageManager pm = context.getPackageManager();
												Drawable ico =context. getApplicationInfo().loadIcon(context.getPackageManager()); 
												icon=((BitmapDrawable)ico).getBitmap();
												app_icon=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}*/

                                            SharedPreferences pref1 = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                                            int notificationResource = pref1.getInt("EmkitNotificationResource", 0);


                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                                    EmkitInstance.getContext())
                                                    .setContentTitle(dataJsonObject.getString("title"))
                                                    .setContentText(dataJsonObject.getString("message"))
                                                    .setAutoCancel(true)
                                                    //.setLargeIcon(icon)
                                                    .setSmallIcon(notificationResource)
                                                    .setContentIntent(pendingIntent)
                                                    .setWhen(System.currentTimeMillis())
                                                    // .setTicker("Card Notification")
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(dataJsonObject.getString("message")))
                                                    .setSound(
                                                            RingtoneManager
                                                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                    .setVibrate(new long[]{1000});
                                            // Logger.d(TAG,"7");
                                            NotificationManager notificationManager = (NotificationManager) EmkitInstance.getContext()
                                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                                            // Logger.d(TAG,"8");
                                            notificationManager.notify(notificationType,
                                                    mBuilder.build());
                                            // Logger.d(TAG,"9");

                                        }

                                    }
                                } else {

                                    Logger.d(TAG, "sightings details inside link else ");
                                    gimbalDetails.setMessage(dataJsonObject.getString("message"));
                                    gimbalDetails.setEmkit_URL(dataJsonObject.getString("emkit_URL"));
                                    gimbalDetails.setTitle(dataJsonObject.getString("title"));
                                    String emkit_url = dataJsonObject.getString("emkit_URL");
                                    if (!emkit_url.contains("extCardDataURL")) {
                                        Editor edit = pref.edit();
                                        edit.putString("extCardDataURL", null);
                                        edit.commit();
                                    }
                                    int index = emkit_url.indexOf("campaign_id");
                                    String campaignTemp = emkit_url.substring(index, emkit_url.length());
                                    String campaign = campaignTemp.substring(campaignTemp.indexOf("=") + 1, campaignTemp.indexOf("&"));
                                    gimbalDetails.setCampaignId(campaign);
                                    Editor edit = pref.edit();
                                    edit.putString("campaignid", campaign);
                                    edit.commit();
                                    /*int i2=emkit_url.indexOf("&");
                                     String s1=emkit_url.substring(0,i2);
								     String s2=emkit_url.substring(i2+1);						    
								     String[] splits1 = s2.split("=");*/
                                     /*String card_Id=splits1[1];
									 Editor editor1=pref.edit();
										editor1.putString(""+card_Id+"campaign_id", campaign_id); 
										editor1.putString(""+card_Id+"campaign_name", campaign_name); 
										editor1.commit();	*/
                                    return gimbalDetails;
                                }
                                return null;

                            } else {
                                Logger.d(TAG, "sightings details inside type else");

                                gimbalDetails.setMessage(dataJsonObject.getString("message"));
                                gimbalDetails.setEmkit_URL(dataJsonObject.getString("emkit_URL"));
                                gimbalDetails.setTitle(dataJsonObject.getString("title"));
                                if (dataJsonObject.has("campaign_id")) {
                                    gimbalDetails.setCampaign_id(dataJsonObject.getString("campaign_id"));
                                }
                                if (dataJsonObject.has("instantpush_id")) {
                                    gimbalDetails.setInstantpush_id(dataJsonObject.getString("instantpush_id"));
                                }
                                String emkit_url = dataJsonObject.getString("emkit_URL");
                                if (!emkit_url.contains("extCardDataURL")) {
                                    Editor edit = pref.edit();
                                    edit.putString("extCardDataURL", null);
                                    edit.commit();
                                }
                                int index = emkit_url.indexOf("campaign_id");
                                String campaignTemp = emkit_url.substring(index, emkit_url.length());
                                String campaign = campaignTemp.substring(campaignTemp.indexOf("=") + 1, campaignTemp.indexOf("&"));
                                gimbalDetails.setCampaignId(campaign);
                                Editor edit = pref.edit();
                                edit.putString("campaignid", campaign);
                                edit.commit();
							/*int i2=emkit_url.indexOf("&");					        
						     String s1=emkit_url.substring(0,i2);
						     String s2=emkit_url.substring(i2+1);						    
						     String[] splits1 = s2.split("=");*/
							 /*String card_Id=splits1[1];
							 Editor editor1=pref.edit();
								editor1.putString(""+card_Id+"campaign_id", campaign_id); 
								editor1.putString(""+card_Id+"campaign_name", campaign_name); 
								editor1.commit();	*/
                                return gimbalDetails;
                            }
                        }
                    } else if (jsonObjResponse.has("aps")) {
                        Intent notificationIntent = new Intent(EmkitInstance.getContext(),
                                EmkitNotificationActivity.class);
                        // Logger.d(TAG,"1");

                        // Logger.d(TAG,"3");
                        CardGimbalDetails gimbalDetails = new CardGimbalDetails();
                        JSONObject apsObject = jsonObjResponse.getJSONObject("aps");
                        if (apsObject.has("data")) {
                            JSONObject dataObject = apsObject.getJSONObject("data");
                            gimbalDetails.setMessage(dataObject.getString("message"));
                            gimbalDetails.setEmkit_URL(dataObject.getString("emkit_URL"));
                            gimbalDetails.setTitle(dataObject.getString("title"));
                            if (dataObject.has("campaign_id")) {
                                gimbalDetails.setCampaign_id(dataObject.getString("campaign_id"));
                            }
                            if (dataObject.has("instantpush_id")) {
                                gimbalDetails.setInstantpush_id(dataObject.getString("instantpush_id"));
                            }
                            String emkit_url = dataObject.getString("emkit_URL");
                            if (!emkit_url.contains("extCardDataURL")) {
                                Editor edit = pref.edit();
                                edit.putString("extCardDataURL", null);
                                edit.commit();
                            }
                            int index = emkit_url.indexOf("campaign_id");
                            String campaignTemp = emkit_url.substring(index, emkit_url.length());
                            String campaign = campaignTemp.substring(campaignTemp.indexOf("=") + 1, campaignTemp.indexOf("&"));
                            gimbalDetails.setCampaignId(campaign);
                            Editor edit = pref.edit();
                            edit.putString("campaignid", campaign);
                            edit.commit();
                            if (!EmkitInstance.getInstance(EmkitInstance.getContext()).isApplicationInBackground()) {
                                return gimbalDetails;
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("emkit_url", emkit_url);
                                if (dataObject.has("campaign_id")) {
                                    bundle.putString("campaign_id", dataObject.getString("campaign_id"));
                                }
                                bundle.putString("instantpush_id", dataObject.getString("instantpush_id"));
                                bundle.putString("finalurl", dataObject.getString("link"));
                                notificationIntent.putExtras(bundle);
                                {

                                    Logger.d(TAG, "4");
                                    int notificationType = (int) System
                                            .currentTimeMillis();
                                    // Logger.d(TAG,"5");
                                    notificationIntent
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    PendingIntent pendingIntent = PendingIntent
                                            .getActivity(EmkitInstance.getContext(), notificationType,
                                                    notificationIntent, 0);
                                    // Logger.d(TAG,"6");
                                    Bitmap icon = null;
									/*int app_icon=0;
									try {
										PackageManager pm = context.getPackageManager();
										Drawable ico =context. getApplicationInfo().loadIcon(context.getPackageManager()); 
										icon=((BitmapDrawable)ico).getBitmap();
										app_icon=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}*/

                                    SharedPreferences pref1 = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
                                    int notificationResource = pref1.getInt("EmkitNotificationResource", 0);


                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                            EmkitInstance.getContext())
                                            .setContentTitle(dataObject.getString("title"))
                                            .setContentText(dataObject.getString("message"))
                                            .setAutoCancel(true)
                                            //.setLargeIcon(icon)
                                            .setSmallIcon(notificationResource)
                                            .setContentIntent(pendingIntent)
                                            .setWhen(System.currentTimeMillis())
                                            // .setTicker("Card Notification")
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(dataObject.getString("message")))
                                            .setSound(
                                                    RingtoneManager
                                                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                            .setVibrate(new long[]{1000});
                                    // Logger.d(TAG,"7");
                                    NotificationManager notificationManager = (NotificationManager) EmkitInstance.getContext()
                                            .getSystemService(Context.NOTIFICATION_SERVICE);
                                    // Logger.d(TAG,"8");
                                    notificationManager.notify(notificationType,
                                            mBuilder.build());
                                    // Logger.d(TAG,"9");

                                }

                            }


                            return null;
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidResponseCode e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String setLocationStatus(Context context, String status) {

        SharedPreferences pref = context.getSharedPreferences("emkit_info", 0);
        String domainname = pref.getString("domainname", null);
        String url = context.getResources().getString(R.string.emkit_server_protocol)
                + domainname
                + context.getResources().getString(R.string.emkit_location_url);
        //String url = context.getResources().getString(R.string.emkit_location_url);
        StoreDetails sd = new StoreDetails();
        IniteMkitDetails initeMkitDetails = sd.getIniteMkitDetails(context);
        url = String.format(url, initeMkitDetails.getEmp2UserId(), status);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        Logger.d(TAG, "setLocationStatus url: " + url);

        String eMkitAPIKey = pref.getString("eMkitAPIKey", null);
        HTTPPoster httpPoster = new HTTPPoster();
        try {

            ResponseHolder responseHolder = httpPoster.doPostasBytes(url, hashMap, eMkitAPIKey);
            if (responseHolder != null) {
                String response = responseHolder.getResponseStr();
                //Logger.d(TAG,"repsonse fromparser is: "+response);
                Logger.d(TAG, "setLocationStatus response: " + response);

                if (response != null && response.trim().length() != 0) {
                    JSONObject jsonObjResponse = (JSONObject) new JSONTokener(response).nextValue();
                    Logger.d(TAG, "jsonObjResponse is" + jsonObjResponse);
                    return "200";
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidResponseCode e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBeaconReceivers(String url) {

        //Logger.d(TAG,"from getBeaconReceivers");
        //Logger.d(TAG,"Beacon Url is"+url);
        final SharedPreferences pref1 = EmkitInstance.getContext().getSharedPreferences("emkit_info", 0);
        String beacon_services_flag = pref1.getString("beacon_services_flag", null);
        if (beacon_services_flag != null && beacon_services_flag.equals("ON")) {

        } else {
            //Do not perform receiver service call, if beacon services flag is OFF
            return true;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();

        HTTPPoster httpPoster = new HTTPPoster();
        try {
            Context applicationContext = JSONParser.mContext;
            SharedPreferences pref = applicationContext.getSharedPreferences("emkit_info", 0);
            String eMkitAPIKey = pref.getString("eMkitAPIKey", null);
            ResponseHolder responseHolder = httpPoster.doGet(url, hashMap, eMkitAPIKey);
            if (responseHolder != null) {
                String response = responseHolder.getResponseStr();
                Logger.d(TAG, "response from beacon " + response);
                HashSet<String> hs = new HashSet<String>();
                SharedPreferences spref = applicationContext.getSharedPreferences("beacon_names", 0);
                Editor spref_edit = spref.edit();
                spref_edit.clear();
                spref_edit.commit();
                if (response != null && response.trim().length() != 0) {
                    JSONArray jsonArrayResponse = (JSONArray) new JSONTokener(response).nextValue();

//     ArrayList<BeaconReceiverDetails> beaconReceiverList = new ArrayList<BeaconReceiverDetails>();

                    //BeaconReceiverListDB dbase = new BeaconReceiverListDB(mContext);

                    for (int idx = 0; idx < jsonArrayResponse.length(); idx++) {
                        JSONObject jsonObject = (JSONObject) jsonArrayResponse.get(idx);

                        BeaconReceiverDetails beaconReceiverHolder = new BeaconReceiverDetails();
                        //Logger.i(TAG, jsonObject.getString("id"));
                        beaconReceiverHolder.setId(jsonObject.getString("id"));
                        beaconReceiverHolder.setType(jsonObject.getString("type"));
                        beaconReceiverHolder.setSub_type(jsonObject.getString("sub_type"));
                        beaconReceiverHolder.setDwell_time(jsonObject.getString("dwell_time"));
                        beaconReceiverHolder.setMin_rssi(jsonObject.getString("min_rssi"));
                        beaconReceiverHolder.setMax_rssi(jsonObject.getString("max_rssi"));
                        beaconReceiverHolder.setRelay_time_interval(jsonObject.getString("relay_time_interval"));
                        beaconReceiverHolder.setMax_sighting_interval(jsonObject.getString("max_sighting_interval"));

                        SharedPreferences beacon_pref = applicationContext.getSharedPreferences("beacon_info", 0);
                        Editor edit = beacon_pref.edit();
                        edit.putString("" + jsonObject.getString("id") + "beacon_id", jsonObject.getString("id"));
                        edit.putString("" + jsonObject.getString("id") + "dwell_time", jsonObject.getString("dwell_time"));
                        edit.putString("" + jsonObject.getString("id") + "min_rssi", jsonObject.getString("min_rssi"));
                        edit.putString("" + jsonObject.getString("id") + "max_rssi", jsonObject.getString("max_rssi"));
                        edit.putString("" + jsonObject.getString("id") + "relay_time_interval", jsonObject.getString("relay_time_interval"));
                        edit.putString("" + jsonObject.getString("id") + "max_sighting_interval", jsonObject.getString("max_sighting_interval"));
                        edit.putString("" + jsonObject.getString("id") + "sub_type", jsonObject.getString("sub_type"));
                        if (jsonObject.has("places")) {
                            JSONArray jsonarray = jsonObject.getJSONArray("places");
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jobj = (JSONObject) jsonarray.get(i);
                                sb.append(jobj.getString("place_name"));
                                if (i != jsonarray.length() - 1) {
                                    sb.append(",");
                                }

                                Logger.d(TAG, "Place names are::" + jobj.getString("place_name"));
                            }
                            edit.putString("" + jsonObject.getString("id")
                                            + "places",
                                    sb.toString());
                        }
                        edit.commit();
                        hs.add(jsonObject.getString("id"));
                    }

                    Logger.i(TAG, hs.toString());
                    spref_edit.putStringSet("beaconnames", hs);
                    spref_edit.commit();

                    Logger.i(TAG, "updating resume interval time");

                    Editor edit = pref.edit();
                    edit.putString("resume_interval_time",
                            "" + System.currentTimeMillis());
                    edit.commit();

                    return true;
                }
            }

        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidResponseCode e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String getUTCDateAndTime(Context context) {
        String format = context.getResources().getString(
                R.string.date_format_yyyyMMdd);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String requestTime = sdf.format(new Date());
        return requestTime;
    }
}