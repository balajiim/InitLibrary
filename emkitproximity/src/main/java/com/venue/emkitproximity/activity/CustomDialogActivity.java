/**
 * Copyright Venuetize 2017
 * <CustomDialogActivity>
 * created by Venuetize
 */
package com.venue.emkitproximity.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;

import com.venue.emkitproximity.utils.CustomProgressDialog;

public class CustomDialogActivity extends Activity {

	Context ctx;
	private CustomProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = this;

		if (ringProgressDialog != null) {
			ringProgressDialog.dismiss();
			ringProgressDialog = null;
	    }
		ringProgressDialog = CustomProgressDialog.show(ctx, "", "");
		ringProgressDialog.setCancelable(false);
		ringProgressDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (ringProgressDialog != null) {
			ringProgressDialog.dismiss();
			ringProgressDialog = null;
	    }
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (ringProgressDialog != null) {
			ringProgressDialog.dismiss();
			ringProgressDialog = null;
	    }
		finish();
	}

	protected void onNewIntent(android.content.Intent intent) {
		if (this.getIntent() != null && this.getIntent().getExtras() != null && this.getIntent().getExtras().getInt("kill") == 1) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

}
