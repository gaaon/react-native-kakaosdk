package com.kakaosdk;

import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.StoryProtocol;
import com.kakao.util.helper.TalkProtocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KakaoLoginManagerModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    public KakaoLoginManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);

        reactContext.addActivityEventListener(this);

        // if (!Session.getCurrentSession().checkAndImplicitOpen()) {
        //     //setContentView(R.layout.layout_common_kakao_login);
        //     // startActivity
        // }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            return;
//        }
    }

    @Override
    public String getName() {
        return "KakaoLoginManager";
    }

    private List<AuthType> getAuthTypes() {
        final List<AuthType> availableAuthTypes = new ArrayList<AuthType>();
        if(TalkProtocol.existCapriLoginActivityInTalk(getReactApplicationContext(), Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            availableAuthTypes.add(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN);
        }
        if(StoryProtocol.existCapriLoginActivityInStory(getReactApplicationContext(), Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }
        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);

        final AuthType[] selectedAuthTypes = Session.getCurrentSession().getAuthTypes();
        availableAuthTypes.retainAll(Arrays.asList(selectedAuthTypes));

        // 개발자가 설정한 것과 available 한 타입이 없다면 직접계정 입력이 뜨도록 한다.
        if(availableAuthTypes.size() == 0){
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }
        return availableAuthTypes;
    }

    @ReactMethod
    public void login(final Promise promise) {
        if( !(getCurrentActivity().getApplication() instanceof  TopActivityWrapper) ) {
            throw new Error("Application should implments TopActivityWrapper.");
        }

        ((TopActivityWrapper)getCurrentActivity().getApplication()).setTopActivity(getCurrentActivity());

        ISessionCallback sessionCallback = new ISessionCallback() {
            //Promise promise;

            private void clear() {
                Session.getCurrentSession().removeCallback(this);
            }

            @Override
            public void onSessionOpened() {
                Boolean obj = true;

                clear();
                promise.resolve(obj);
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                if(exception != null) {
                    //Logger.e(exception);
                }
                clear();
                promise.reject(exception);
                //
                // setContentView(R.layout.layout_common_kakao_login);
            }

        };

        Session.getCurrentSession().addCallback(sessionCallback);

        final List<AuthType> authTypes = getAuthTypes();
        if(authTypes.size() == 1){
            Session.getCurrentSession().open(authTypes.get(0), getCurrentActivity());
        } else {

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}
