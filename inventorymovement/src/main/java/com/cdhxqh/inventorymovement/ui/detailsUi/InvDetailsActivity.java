package com.cdhxqh.inventorymovement.ui.detailsUi;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.KcckInvbalancesAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.ig_json.Ig_Json_Model;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.model.Invbalances;
import com.cdhxqh.inventorymovement.model.Inventory;
import com.cdhxqh.inventorymovement.ui.BaseActivity;
import com.cdhxqh.inventorymovement.utils.MessageUtils;
import com.cdhxqh.inventorymovement.wight.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 库存使用情况详情
 */
public class InvDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private static final String TAG = "InvDetailsActivity";

    private TextView titleTextView; // 标题

    private ImageView backImage; //返回按钮

    /**
     * --界面显示的textView--**
     */

    private TextView numTextView; //项目编号

    private TextView descTextView; //项目描述

    private TextView binnumTextView; //库位号

    private TextView curbaltotalTextView; //当前余量

    private TextView issueunitTextView; //发放单位

    private TextView locationTextView; //仓库编号

    private TextView locationdescTextView; //仓库描述

    private TextView lotnumTextView; //批次


    private Inventory inventory;

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

    KcckInvbalancesAdapter kcckInvbalancesAdapter;

    private int page = 1;

    private int currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_details);
        geiIntentData();
        initView();
        setEvent();
    }

    /**
     * 获取上个界面的数据*
     */
    private void geiIntentData() {
        inventory = (Inventory) getIntent().getSerializableExtra("inventory");

    }


    /**
     * 初始化界面组件
     */
    private void initView() {
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);


        numTextView = (TextView) findViewById(R.id.item_num_text_id);
        descTextView = (TextView) findViewById(R.id.item_desc_text);
        binnumTextView = (TextView) findViewById(R.id.item_binnum_text);
        curbaltotalTextView = (TextView) findViewById(R.id.inv_curbaltotal_text);
        issueunitTextView = (TextView) findViewById(R.id.inv_issueunit_text);
        locationTextView = (TextView) findViewById(R.id.inv_location_text);
        locationdescTextView = (TextView) findViewById(R.id.inv_locationdesc_text);
        lotnumTextView = (TextView) findViewById(R.id.inv_lotnum_text);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_topics);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        notLinearLayout = (LinearLayout) findViewById(R.id.have_not_data_id);


    }


    /**
     * 设置事件监听
     */
    private void setEvent() {
        titleTextView.setText(getString(R.string.inv_text_title));
        backImage.setOnClickListener(backOnClickListener);

        if (inventory != null) {
            numTextView.setText(inventory.itemnum);
            descTextView.setText(inventory.itemdesc);
            binnumTextView.setText(inventory.binnum);
            curbaltotalTextView.setText(inventory.curbaltotal);
            issueunitTextView.setText(inventory.issueunit);
            locationTextView.setText(inventory.location);
            locationdescTextView.setText(inventory.locationdesc);
            lotnumTextView.setText(inventory.lotnum);
        }

        mLayoutManager = new LinearLayoutManager(InvDetailsActivity.this);
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

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    @Override
    public void onRefresh() {
        kcckInvbalancesAdapter.removeAllData();
        page = 1;
        getItemList(inventory.itemnum, inventory.location, "");
        mSwipeLayout.setRefreshing(false);

    }

    @Override
    public void onLoad() {
        if (currentpage == page) {
            MessageUtils.showMiddleToast(InvDetailsActivity.this, "已加载出全部数据");
            mSwipeLayout.setLoading(false);
        } else {
            page++;
            getItemList(inventory.itemnum, inventory.location, "");
        }
    }

    private void initAdapter() {
        kcckInvbalancesAdapter = new KcckInvbalancesAdapter(InvDetailsActivity.this, inventory.location);
        mRecyclerView.setAdapter(kcckInvbalancesAdapter);

    }


    /**
     * 获取库存项目信息*
     */

    private void getItemList(String itemnum, String location, String seach) {
        ImManager.getDataPagingInfo(InvDetailsActivity.this, ImManager.sercInvbalancesUrl(itemnum, location, seach, page, 20), new HttpRequestHandler<Results>() {
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
                        kcckInvbalancesAdapter.removeAllData();
                        notLinearLayout.setVisibility(View.VISIBLE);

                    } else {
                        kcckInvbalancesAdapter.adddate(items);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setLoading(false);
                kcckInvbalancesAdapter.removeAllData();
                notLinearLayout.setVisibility(View.VISIBLE);

            }
        });
    }
}
