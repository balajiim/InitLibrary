package com.venue.emkitproximity.notifier;

/**
 * Created by dilip on 6/30/2016.
 */
public interface EmkitReportUserLocationNotifier {

    public void onEmkitReportUserLocationSuccess(String value);
    public void onEmkitReportUserLocationFailure();
}
