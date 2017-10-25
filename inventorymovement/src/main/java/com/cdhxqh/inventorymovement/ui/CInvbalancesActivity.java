package com.cdhxqh.inventorymovement.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.CInvbalancesAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.ig_json.Ig_Json_Model;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.model.Invbalances;
import com.cdhxqh.inventorymovement.model.Inventory;
import com.cdhxqh.inventorymovement.utils.MessageUtils;
import com.cdhxqh.inventorymovement.wight.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 库存盘点*
 */
public class CInvbalancesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {
    private static final String TAG = "CInvbalancesActivity";

    private TextView titleTextView; // 标题

    private LinearLayout titlelayout;//标题布局
    private RelativeLayout title_searchlayout;//标题搜索布局

    private ImageView backImage; //返回

    private ImageView searchimg; //搜索

    private EditText editText; // 搜索

    /**
     * 搜索值*
     */
    private String search = "";


    private ScrollView scrollView;
    /**
     * RecyclerView*
     */
    RecyclerView mRecyclerView;

    RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeLayout;

    /**
     * 暂无数据*
     */
    LinearLayout notLinearLayout;

    CInvbalancesAdapter cInvbalancesAdapter;

    private int page = 1;

    /**
     * Inventory*
     */
    private Inventory inventory;

    private int mark;
    /**
     * 进度条*
     */
    private ProgressDialog mProgressDialog;

    private int currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinvbalances);

        initData();

        findViewById();

        initView();
    }


    /**
     * 获取上个界面的数据*
     */
    private void initData() {

        inventory = (Inventory) getIntent().getSerializableExtra("inventory");
    }


    /**
     * 初始化界面组件*
     */
    private void findViewById() {
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        titlelayout = (LinearLayout) findViewById(R.id.title_contains);
        title_searchlayout = (RelativeLayout) findViewById(R.id.title_search_layout);
        editText = (EditText) findViewById(R.id.search_edittext_id);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);
        searchimg = (ImageView) findViewById(R.id.menu_imageview_id);

        scrollView = (ScrollView) findViewById(R.id.scrollView_id);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_topics);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        notLinearLayout = (LinearLayout) findViewById(R.id.have_not_data_id);


    }


    /**
     * 设置事件监听*
     */
    private void initView() {

        titleTextView.setText(inventory.location + "," + inventory.itemnum);
        backImage.setOnClickListener(backImageOnClickListener);
        searchimg.setBackgroundResource(R.drawable.ic_search);
        searchimg.setVisibility(View.VISIBLE);
        searchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_searchlayout.setVisibility(View.VISIBLE);
            }
        });
        editText.setHint(getString(R.string.invbalances_pici_hint));
        editText.setOnEditorActionListener(editTextOnEditorActionListener);


        mLayoutManager = new LinearLayoutManager(CInvbalancesActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColor(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setLoading(false);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);
        initAdapter();

        getItemList(inventory.itemnum, inventory.location, "");
    }


    private void initAdapter() {
        cInvbalancesAdapter = new CInvbalancesAdapter(CInvbalancesActivity.this, inventory.location);
        mRecyclerView.setAdapter(cInvbalancesAdapter);
        cInvbalancesAdapter.setcOnClickListener(new CInvbalancesAdapter.cOnClickListener() {
            @Override
            public void cOnClickListener(Invbalances invbalances) {
                mProgressDialog = ProgressDialog.show(CInvbalancesActivity.this, null,
                        "正在提交中...", true, true);
                confirmData(invbalances);
            }
        });
    }

    /**
     * 软键盘*
     */
    private TextView.OnEditorActionListener editTextOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                // 先隐藏键盘
                ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                CInvbalancesActivity.this.getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                search = editText.getText().toString();
                mSwipeLayout.setRefreshing(true);
                cInvbalancesAdapter.removeAllData();
//                mSwipeLayout.setLoading(true);
                notLinearLayout.setVisibility(View.GONE);
                getItemList(inventory.itemnum, inventory.location, search);
                return true;

            }
            return false;
        }
    };

    private View.OnClickListener backImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (title_searchlayout.getVisibility() == View.VISIBLE) {
                title_searchlayout.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
    };


    /**
     * 获取库存项目信息*
     */

    private void getItemList(String itemnum, String location, String seach) {
        ImManager.getDataPagingInfo(CInvbalancesActivity.this, ImManager.sercInvbalancesUrl(itemnum, location, seach, page, 20), new HttpRequestHandler<Results>() {
            @Override
            public void onSuccess(Results results) {
                Log.i(TAG, "data=" + results);
            }

            @Override
            public void onSuccess(Results results, int totalPages, int currentPage) {
                currentpage = currentPage;
                ArrayList<Invbalances> items = null;
                try {
                    items = Ig_Json_Model.parseInvbalancesFromString(results.getResultlist());
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setLoading(false);
                    if (items == null || items.isEmpty()) {
                        cInvbalancesAdapter.removeAllData();
                        notLinearLayout.setVisibility(View.VISIBLE);
                    } else {

                        cInvbalancesAdapter.adddate(items);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setLoading(false);
                cInvbalancesAdapter.removeAllData();
                notLinearLayout.setVisibility(View.VISIBLE);

            }
        });
    }


    @Override
    public void onLoad() {
        if (currentpage == page) {
            MessageUtils.showMiddleToast(CInvbalancesActivity.this, "已加载出全部数据");
            mSwipeLayout.setLoading(false);
        } else {
            page++;
            getItemList(inventory.itemnum, inventory.location, search);
        }

    }

    @Override
    public void onRefresh() {
        cInvbalancesAdapter.removeAllData();
        page = 1;
        getItemList(inventory.itemnum, inventory.location, search);
        mSwipeLayout.setRefreshing(false);
    }


    /**
     * 提交数据方法*
     */
    private void confirmData(final Invbalances invbalances) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String data = getBaseApplication().getWsService().INV04Invadj(getBaseApplication().getUsername(), invbalances.location,
                        invbalances.itemnum, invbalances.binnum, invbalances.lotnum, invbalances.curbal);

                return data;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgressDialog.cancel();
                try {
                    if (!s.equals("")) {
                        JSONObject jsonObject = new JSONObject(s);
                        s = jsonObject.getString("msg");
                        MessageUtils.showMiddleToast(CInvbalancesActivity.this, s);

                    }
//                    finish();
                } catch (JSONException e) {
                    MessageUtils.showMiddleToast(CInvbalancesActivity.this, "提交失败");
                }


            }
        }.execute();
    }


}
