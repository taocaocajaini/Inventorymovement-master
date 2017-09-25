package com.cdhxqh.inventorymovement.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private ImageView menuImage;//菜单

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


//    private String wonum="965361";

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
        menuImage = (ImageView) findViewById(R.id.menu_imageview_id);

        wonumTextView = (TextView) findViewById(R.id.workorder_wonum_text);
        descriptionTextView = (TextView) findViewById(R.id.workorder_desction_text);
        onbehalfofTextView = (TextView) findViewById(R.id.workorder_onbehalfof_text);
        statusTextView = (TextView) findViewById(R.id.workorder_status_text);


        mRecyclerView = (RecyclerView) findViewById(R.id.list_topics);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        invreserveAdapter = new InvreserveAdapter(WorkOrderDetailsActivity.this,workOrder.wonum);
        mRecyclerView.setAdapter(invreserveAdapter);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColor(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mSwipeLayout.setRefreshing(true);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);

        notLinearLayout = (LinearLayout) findViewById(R.id.have_not_data_id);

        getInvreserveList(workOrder.wonum);
//        getInvreserveList(wonum);
    }


    /**
     * 设置事件监听
     */
    private void setEvent() {
        titleTextView.setText(getString(R.string.workorder_detail_title));
        backImage.setOnClickListener(backOnClickListener);
        menuImage.setVisibility(View.VISIBLE);
        menuImage.setOnClickListener(menuImageOnClickListener);

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

    private View.OnClickListener menuImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupWindow(menuImage);
        }
    };

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(WorkOrderDetailsActivity.this).inflate(
                R.layout.pop_window, null);


        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        genTextView = (TextView) contentView.findViewById(R.id.item_generate_id);
        genTextView.setOnClickListener(genTextViewOnClickListener);
        popupWindow.setTouchable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.popup_background_mtrl_mult));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

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
//                Log.i(TAG, "data=" + results);
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
                            invreserveAdapter = new InvreserveAdapter(WorkOrderDetailsActivity.this,wonum);
                            mRecyclerView.setAdapter(invreserveAdapter);
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
}
