/**
 * Copyright Venuetize 2017
 * <SightingsNotifier>
 * created by Venuetize
 */
package com.venue.emkitproximity.notifier;

import com.venue.emkitproximity.holder.CardGimbalDetails;

public interface SightingsNotifier {
	
	public void onSightingsSuccess(CardGimbalDetails cardDetails);
	
	public void onSightingsError();

}