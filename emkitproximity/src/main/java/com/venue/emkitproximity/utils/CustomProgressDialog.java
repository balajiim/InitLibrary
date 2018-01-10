/**
 * Copyright Venuetize 2017
 * <CustomProgressDialog>
 * created by Venuetize
 */
package com.venue.emkitproximity.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.venue.emkitproximity.R;


@SuppressLint("InflateParams")
public class CustomProgressDialog extends Dialog {
	private static CustomProgressDialog dialog;
	public static CustomProgressDialog show(Context context,
                                            CharSequence title, CharSequence message) {
		return show(context, title, message, false);
	}

	public static CustomProgressDialog show(Context context,
                                            CharSequence title, CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static CustomProgressDialog show(Context context,
                                            CharSequence title, CharSequence message, boolean indeterminate,
                                            boolean cancelable, OnCancelListener cancelListener) {
		//System.out.println("from CustomProgressDialog show::");
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.em_vendor_dialog_progress, null);

		 dialog = new CustomProgressDialog(context){
            @Override
            public void onBackPressed() {
            	dialog.dismiss();
            }};
		dialog.setTitle(title);
		dialog.setCancelable(true);
		/*dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);*/


		/* The next line will add the ProgressBar to the dialog. */
		/*dialog.addContentView(new ProgressBar(context), new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/
		dialog.addContentView(view, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.show();

		return dialog;
	}

	public CustomProgressDialog(Context context) {
		super(context, R.style.ProgressDialog);
	}
}
