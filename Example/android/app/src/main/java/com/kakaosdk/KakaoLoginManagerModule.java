package com.kakaosdk;

import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
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
        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        /*
        if(TalkProtocol.existCapriLoginActivityInTalk(getReactApplicationContext(), Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            //availableAuthTypes.add(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN);
        }
        if(StoryProtocol.existCapriLoginActivityInStory(getReactApplicationContext(), Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }


        final AuthType[] selectedAuthTypes = Session.getCurrentSession().getAuthTypes();
        availableAuthTypes.retainAll(Arrays.asList(selectedAuthTypes));

        // 개발자가 설정한 것과 available 한 타입이 없다면 직접계정 입력이 뜨도록 한다.
        if(availableAuthTypes.size() == 0){
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }*/
        return availableAuthTypes;
    }

    @ReactMethod
    public void login(final Promise promise) {
        if( !(getCurrentActivity().getApplication() instanceof  TopActivityWrapper) ) {
            throw new Error("Application should implments TopActivityWrapper.");
        }

        ((TopActivityWrapper)getCurrentActivity().getApplication()).setTopActivity(getCurrentActivity());

        ISessionCallback sessionCallback = new ISessionCallback() {
            private void clear() {
                Session.getCurrentSession().removeCallback(this);
            }

            @Override
            public void onSessionOpened() {
                clear();

                UserManagement.requestMe(new MeResponseCallback() {

                    @Override
                    public void onSuccess(UserProfile result) {
                        WritableMap map = Arguments.createMap();

                        map.putString("nickname", result.getNickname());
                        map.putString("profileImagePath", result.getProfileImagePath());
                        map.putString("thumbnailImagePath", result.getThumbnailImagePath());
                        map.putString("UUID", result.getUUID());

                        map.putInt("remainingGroupMsgCount", result.getRemainingGroupMsgCount());
                        map.putInt("remainingInviteCount", result.getRemainingInviteCount());

                        map.putString("id", result.getId()+"");
                        map.putString("serviceUserId", result.getServiceUserId()+"");

                        promise.resolve(map);
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        promise.reject(new Error(errorResult.getErrorMessage()));
                    }

                    @Override
                    public void onNotSignedUp() {
                        promise.reject(new Error("Not signed up."));
                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                clear();
                if(exception != null) {
                    promise.reject(exception);
                }

            }

        };

        Session.getCurrentSession().addCallback(sessionCallback);

        final List<AuthType> authTypes = getAuthTypes();
        Log.d("checking", authTypes.size()+"");

        if(authTypes.size() == 1){
            Log.d("checking", authTypes.get(0).name());
            Session.getCurrentSession().open(authTypes.get(0), getCurrentActivity());
        } else {

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}
