package com.wz.cartlib;

import android.util.Log;

public class L {
	private static final String TAG = "购物车";
	private static final String INFO = "INFO";

	public static void i(String info) {
		if (AppAddShopCartManager.isDebug) {
			Log.i(TAG, INFO + info);
		}
	}

	public static void e(String info) {
		if (AppAddShopCartManager.isDebug) {
			Log.e(TAG, INFO + info);
		}
	}

	public static void d(String info) {
		if (AppAddShopCartManager.isDebug) {
			Log.d(TAG, INFO + info);
		}
	}
}
