package com.wz.cartlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class WzAddShopCartServiceConnection implements ServiceConnection {

	private static final String TAG = "WzTestServiceConnection";
	private WzAddCartListener mWzAddCartListener;
	private String mGoodId;
	private WzAddCartSucessListener mWzAddCartSucessListener;
	private Context mContext;

	public WzAddShopCartServiceConnection(Context context, String content, WzAddCartSucessListener listener) {
		super();
		this.mContext = context;
		this.mGoodId = content;
		this.mWzAddCartSucessListener = listener;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		mWzAddCartListener = WzAddCartListener.Stub.asInterface(service);
		try {
			mWzAddCartListener.register(mWzAddCartSucessListener);
			mWzAddCartListener.addCart(mGoodId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		mWzAddCartListener = null;
	}


	public int connectAndAddCart(String goodId) {
		mGoodId = goodId;
		if (mWzAddCartListener == null) {
			Intent service = new Intent("com.wz.shopcart.add");
			boolean isSucess = mContext.bindService(service, this, Context.BIND_AUTO_CREATE);
			if (isSucess) {
				return 1;
			} else {
				return 0;
			}
		} else {
			try {
				Log.i(TAG, "Connection AddShopCartService");
				mWzAddCartListener.register(mWzAddCartSucessListener);
				mWzAddCartListener.addCart(mGoodId);
			} catch (RemoteException e) {
				L.e("RemoteException" + e.getMessage());
				e.printStackTrace();
				return 0;
			}
			return 1;
		}
	}

}
