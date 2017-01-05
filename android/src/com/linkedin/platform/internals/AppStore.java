package com.linkedin.platform.internals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class AppStore {

	public static void goAppStore(final Activity activity, boolean showDialog,
			String[] goappstoreTexts) {
		if (!showDialog) {
			goToAppStore(activity);
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(goappstoreTexts[0]).setTitle(goappstoreTexts[1]);
		builder.setPositiveButton(goappstoreTexts[2],
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						goToAppStore(activity);
						dialogInterface.dismiss();
					}
				});
		builder.setNegativeButton(goappstoreTexts[3],
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
		builder.create().show();
	}

	private static void goToAppStore(final Activity activity) {
		SupportedAppStore appStore = SupportedAppStore.fromDeviceManufacturer();
		String appStoreUri = appStore.getAppStoreUri();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appStoreUri));
		try {
			activity.startActivity(intent);
		} catch (android.content.ActivityNotFoundException e) {
			// should not happen
		}
	}

	private static enum SupportedAppStore {
		amazonAppstore("amazon", "amzn://apps/android?p=com.linkedin.android"), googlePlay(
				"google", "market://details?id=com.linkedin.android"), samsungApps(
				"samsung", "samsungapps://ProductDetail/com.linkedin.android");

		private final String deviceManufacturer;
		private final String appStoreUri;

		private SupportedAppStore(String deviceManufacturer, String appStoreUri) {
			this.deviceManufacturer = deviceManufacturer;
			this.appStoreUri = appStoreUri;
		}

		public String getDeviceManufacturer() {
			return deviceManufacturer;
		}

		public String getAppStoreUri() {
			return appStoreUri;
		}

		public static SupportedAppStore fromDeviceManufacturer() {
			for (SupportedAppStore appStore : values()) {
				if (appStore.getDeviceManufacturer().equalsIgnoreCase(
						Build.MANUFACTURER)) {
					return appStore;
				}
			}
			// return google play by default
			return googlePlay;
		}
	};

}
