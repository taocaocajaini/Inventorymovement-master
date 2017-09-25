package com.cdhxqh.inventorymovement.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cdhxqh.inventorymovement.AppManager;
import com.cdhxqh.inventorymovement.application.BaseApplication;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    protected BaseApplication baseApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        baseApplication = (BaseApplication) getApplication();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    public void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    public void showProgressBar(boolean show, String message) {
        initProgressBar();
        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
    }

    public void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    public void colseProgressBar() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     *
     */
    protected BaseApplication getBaseApplication(){
        return baseApplication;
    }

}
