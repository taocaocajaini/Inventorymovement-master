package com.cdhxqh.inventorymovement.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.cdhxqh.inventorymovement.model.MemberModel;
import com.cdhxqh.inventorymovement.ui.BaseActivity;
import com.cdhxqh.inventorymovement.utils.AccountUtils;


/**
 * Created by yw on 2015/5/3.
 */
public class BaseFragment extends Fragment implements AccountUtils.OnAccountListener {

    protected boolean mIsLogin;
    protected MemberModel mLoginProfile;
    protected BackHandledInterface mBackHandledInterface;

//    protected FootUpdate mFootUpdate = new FootUpdate();

    public static interface BackHandledInterface {
        public abstract void setSelectedFragment(BaseFragment selectedFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsLogin = AccountUtils.isLogined(getActivity());
        if (mIsLogin)
            mLoginProfile = AccountUtils.readLoginMember(getActivity());
        AccountUtils.registerAccountListener(this);

        mBackHandledInterface = (BackHandledInterface) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        AccountUtils.unregisterAccountListener(this);
        super.onDestroy();
    }

    @Override
    public void onLogout() {
        mIsLogin = false;
    }

    @Override
    public void onLogin(MemberModel member) {
        mIsLogin = true;
        mLoginProfile = member;
    }

    public boolean onBackPressed() {
        return false;
    }


    final public BaseActivity getBaseActivity() {
        return ((BaseActivity) super.getActivity());
    }

    final public void showProgress(int messageId) {
        getBaseActivity().showProgressBar(messageId);
    }

    final public void showProgress(boolean show) {
        getBaseActivity().showProgressBar(show);
    }
}
