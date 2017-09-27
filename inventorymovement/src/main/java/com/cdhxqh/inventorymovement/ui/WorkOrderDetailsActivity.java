package com.cdhxqh.inventorymovement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.InvreserveAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.ig_json.Ig_Json_Model;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.model.Invreserve;
import com.cdhxqh.inventorymovement.model.WorkOrder;
import com.cdhxqh.inventorymovement.wight.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 出库管理详情
 */
public class WorkOrderDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private static final String TAG = "WorkOrderDetailsActivity";

    private TextView titleTextView; // 标题

    private ImageView backImage; //返回按钮


    private TextView genTextView;


    /**
     * --界面显示的textView--**
     */

    private TextView wonumTextView; //工单编号

    private TextView descriptionTextView; //描述

    private TextView onbehalfofTextView; //领用人

    private TextView statusTextView; //状态

    /**
     * WorkOrder*
     */
    private WorkOrder workOrder;


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


    private InvreserveAdapter invreserveAdapter;


    private int page = 1;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_details_items);
        geiIntentData();
        initView();
        setEvent();
    }

    /**
     * 获取上个界面的数据*
     */
    private void geiIntentData() {
        workOrder = (WorkOrder) getIntent().getSerializableExtra("workOrder");

    }


    /**
     * 初始化界面组件
     */
    private void initView() {
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);

        wonumTextView = (TextView) findViewById(R.id.workorder_wonum_text);
        descriptionTextView = (TextView) findViewById(R.id.workorder_desction_text);
        onbehalfofTextView = (TextView) findViewById(R.id.workorder_onbehalfof_text);
        statusTextView = (TextView) findViewById(R.id.workorder_status_text);


        mRecyclerView = (RecyclerView) findViewById(R.id.list_topics);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColor(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mSwipeLayout.setRefreshing(true);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);

        notLinearLayout = (LinearLayout) findViewById(R.id.have_not_data_id);
        initAdapter();
        getInvreserveList(workOrder.wonum);
    }

    private void initAdapter() {
        invreserveAdapter = new InvreserveAdapter(WorkOrderDetailsActivity.this);
        mRecyclerView.setAdapter(invreserveAdapter);
        invreserveAdapter.setcOnClickListener(new InvreserveAdapter.COnClickListener() {
            @Override
            public void cOnClickListener(Invreserve item) {
                Intent intent = new Intent(WorkOrderDetailsActivity.this, InvreserveDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("invreserve", item);
                bundle.putString("wonum", workOrder.wonum);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }


    /**
     * 设置事件监听
     */
    private void setEvent() {
        titleTextView.setText(getString(R.string.workorder_detail_title));
        backImage.setOnClickListener(backOnClickListener);

        if (workOrder != null) {
            wonumTextView.setText(workOrder.wonum);
            descriptionTextView.setText(workOrder.description);
            onbehalfofTextView.setText(workOrder.onbehalfof);
            statusTextView.setText(workOrder.status);
        }


    }

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    /**
     * 生成物资编码*
     */
    private View.OnClickListener genTextViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    /**
     * 获取gInvreserve信息*
     */

    private void getInvreserveList(final String wonum) {
        ImManager.getDataPagingInfo(this, ImManager.serInvreserveUrl(wonum, "", page, 20), new HttpRequestHandler<Results>() {
            @Override
            public void onSuccess(Results results) {
            }

            @Override
            public void onSuccess(Results results, int totalPages, int currentPage) {
                ArrayList<Invreserve> items = null;
                try {
                    items = Ig_Json_Model.parseInvreserveFromString(results.getResultlist());
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setLoading(false);
                    if (items == null || items.isEmpty()) {
                        notLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        if (page == 1) {
                            initAdapter();
                        }
                        if (totalPages == page) {
                            invreserveAdapter.adddate(items);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                mSwipeLayout.setRefreshing(false);
                notLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onLoad() {
        page = 1;
        getInvreserveList(workOrder.wonum);
    }

    @Override
    public void onRefresh() {
        page++;
        getInvreserveList(workOrder.wonum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1000: //提交刷新
                mSwipeLayout.setRefreshing(true);
                invreserveAdapter.removeAllData();
                invreserveAdapter.notifyDataSetChanged();
                getInvreserveList(workOrder.wonum);
                break;
        }
    }
}
