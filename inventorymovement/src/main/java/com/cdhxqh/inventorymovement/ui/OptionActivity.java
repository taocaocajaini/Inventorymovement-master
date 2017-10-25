package com.cdhxqh.inventorymovement.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.OptionAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.ig_json.Ig_Json_Model;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.constants.Constants;
import com.cdhxqh.inventorymovement.model.Invtype;
import com.cdhxqh.inventorymovement.model.Option;
import com.cdhxqh.inventorymovement.wight.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by think on 2016/5/16.
 * 通用选项查询界面
 */
public class OptionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {
    private static final String TAG = "OptionActivity";
    /**
     * 标题*
     */
    private TextView titlename;
    /**
     * 返回*
     */
    private ImageView backImage;
    /**
     * 清空
     */
    private Button clear;

    LinearLayoutManager layoutManager;
    public RecyclerView recyclerView;
    /**
     * 暂无数据*
     */
    private LinearLayout nodatalayout;
    /**
     * 同步数据*
     */
//    private Button freshbtn;
    private OptionAdapter optionAdapter;
    private EditText search;
    private String searchText = "";
    public int optiontype;
    public String parent;

    private String CraftSearch;

    private SwipeRefreshLayout refresh_layout = null;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getIntentData();
        findViewById();
        initView();
    }

    private void getIntentData() {
        optiontype = getIntent().getIntExtra("optiontype",0);
    }

    protected void findViewById() {
        titlename = (TextView) findViewById(R.id.drawer_text);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);
        clear = (Button) findViewById(R.id.submit_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_id);
        nodatalayout = (LinearLayout) findViewById(R.id.have_not_data_id);
        search = (EditText) findViewById(R.id.search_edit);
        refresh_layout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_container);
    }

    protected void initView() {
        titlename.setText("选项列表");

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clear.setVisibility(View.VISIBLE);
        clear.setText("清空");
        clear.setOnClickListener(clearOnClickListener);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        optionAdapter = new OptionAdapter(this);
        recyclerView.setAdapter(optionAdapter);

        refresh_layout.setColor(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh_layout.setRefreshing(false);
        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnLoadListener(this);
        setSearchEdit();
        getData(searchText);

//        freshbtn.setOnClickListener(freshbtnbtnOnClickListener);
    }

    /**
     * 同步数据*
     */
    private View.OnClickListener freshbtnbtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Intent intent = new Intent();
//            intent.setClass(OptionActivity.this, DownloadActivity.class);
//            startActivityForResult(intent, 1000);
        }
    };

    private View.OnClickListener clearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClearResponseData();
        }
    };


    @Override
    protected void onActivityResult(int requestCode1, int resultCode, Intent data) {
        super.onActivityResult(requestCode1, resultCode, data);
        switch (requestCode1) {
            case 1000:
                Log.i(TAG, "requestCode=" + requestCode1);
                getData(searchText);
                break;
        }
    }

    private void setSearchEdit() {
        SpannableString msp = new SpannableString("XX搜索");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_search);
        msp.setSpan(new ImageSpan(drawable), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        search.setHint(msp);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    OptionActivity.this.getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    nodatalayout.setVisibility(View.GONE);
                    searchText = search.getText().toString();
                    optionAdapter = new OptionAdapter(OptionActivity.this);
                    recyclerView.setAdapter(optionAdapter);
                    getData(searchText);
                    return true;
                }
                return false;
            }
        });
    }

    private String getUrl(String searchText) {
        if (optiontype == Constants.INVTYPECODE) {
            return ImManager.setInvtypeUrl(searchText, page, 20);
        }
        return "";
    }

    private void getData(String searchText) {
            ImManager.getDataPagingInfo(this, getUrl(searchText), new HttpRequestHandler<Results>() {
                @Override
                public void onSuccess(Results results) {
                    Log.i(TAG, "data=" + results);
                }

                @Override
                public void onSuccess(Results results, int totalPages, int currentPage) {
                    if (results.getResultlist() != null) {
                        refresh_layout.setRefreshing(false);
                        refresh_layout.setLoading(false);
                        if (page == 1) {
                            optionAdapter = new OptionAdapter(OptionActivity.this);
                            recyclerView.setAdapter(optionAdapter);
                        }
                        if (optiontype == Constants.INVTYPECODE) {//
                            ArrayList<Invtype> items = null;
                            try {
                                items = Ig_Json_Model.parseInvtypeFromString(results.getResultlist());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                optionAdapter.addPersonDate(items);
                        }
                        if (optionAdapter.getItemCount() == 0) {
                            nodatalayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        refresh_layout.setRefreshing(false);
                        nodatalayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(String error) {
                    refresh_layout.setRefreshing(false);
                    nodatalayout.setVisibility(View.VISIBLE);
                }
            });
    }

    public void responseData(Option option) {
        Intent intent = getIntent();
        intent.putExtra("option", option);
        Log.i(TAG, "optiontype=" + optiontype);
        OptionActivity.this.setResult(optiontype, intent);
        finish();
    }

    public void ClearResponseData() {
        Intent intent = getIntent();
        Option option = new Option();
        option.setName("");
        option.setDesc("");
        option.setValue1("");
        option.setValue2("");
        option.setValue3("");
        option.setValue4("");
        option.setValue5("");
        option.setValue6("");
        intent.putExtra("option", option);
        OptionActivity.this.setResult(optiontype, intent);
        finish();
    }

    //下拉刷新触发事件
    @Override
    public void onRefresh() {
        page = 1;
        getData(searchText);
    }

    //上拉加载
    @Override
    public void onLoad() {
        page++;
        getData(searchText);
        refresh_layout.setLoading(false);
    }
}
