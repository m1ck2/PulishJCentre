package com.wz.cartlib;

import android.content.Context;

public class AppAddShopCartManager {

	private static WzAddShopCartServiceConnection mWzAddShopCartServiceConnection;
	private static Context mContext;
	public static boolean isDebug = true;

	public static void addShopCart(Context context, String content, WzAddCartSucessListener listener) {
		mContext = context;
		if (mWzAddShopCartServiceConnection == null) {
			mWzAddShopCartServiceConnection = new WzAddShopCartServiceConnection(mContext, content, listener);
			L.i("new WzAddShopCartServiceConnection");
		}
		if (0 == mWzAddShopCartServiceConnection.connectAndAddCart(content)) {
			try {
				throw new Exception(ExceptionMessage.MESSAGE_CONNECTION_FAILED);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				L.e(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
