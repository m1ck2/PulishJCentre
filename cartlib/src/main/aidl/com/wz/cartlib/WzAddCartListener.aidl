// WzAddCartListener.aidl
package com.wz.cartlib;

// Declare any non-default types here with import statements

import com.wz.cartlib.WzAddCartSucessListener;

interface WzAddCartListener {

        void addCart(String shopId);//添加商品到购物车ﳵ

        void register(WzAddCartSucessListener listener); //注册监听

        void unregister(WzAddCartSucessListener listener); //解除监听
}
