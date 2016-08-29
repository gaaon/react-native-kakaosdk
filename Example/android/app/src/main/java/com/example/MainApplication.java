package com.example;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.kakao.auth.KakaoSDK;
import com.kakaosdk.KakaoSDKAdapter;
import com.kakaosdk.KakaoSDKPackage;
import com.kakaosdk.TopActivityWrapper;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication, TopActivityWrapper {
    private Activity topActivity;

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new KakaoSDKPackage()
            );
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSDK.init(new KakaoSDKAdapter(this));
    }

    @Override
    public void setTopActivity(Activity topActivity) {
        this.topActivity = topActivity;
    }

    @Override
    public Activity getTopActivity() {
        return this.topActivity;
    }
}
