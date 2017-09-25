package com.cdhxqh.inventorymovement.application;

import android.app.Application;

import com.cdhxqh.inventorymovement.constants.Constants;
import com.cdhxqh.inventorymovement.webserviceclient.AndroidClientService;


/**
 * Created by think on 2015/12/11.
 */
public class BaseApplication extends Application {
    private String username = "";
    private static BaseApplication mContext;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static BaseApplication getInstance(){
        return mContext;
    }

    public AndroidClientService getWsService() {
        return new AndroidClientService(Constants.getWsUrl(this));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }


}
