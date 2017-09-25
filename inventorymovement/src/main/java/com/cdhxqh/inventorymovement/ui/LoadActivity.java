package com.cdhxqh.inventorymovement.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.utils.PermissionsChecker;

public class LoadActivity extends BaseActivity {

    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;


    private static final int REQUEST_CODE = 0; // 请求码


    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
//            Manifest.permission.INTERNET,
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mPermissionsChecker = new PermissionsChecker(this);


    }



    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Handler x = new Handler();
            x.postDelayed(new splashhandler(), 2000);
        }
    }


    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }






    class splashhandler implements Runnable {

        public void run() {
            jumpLoginActivity();
        }

    }


    /**
     * 跳转至登录界面*
     */
    private void jumpLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }


}
