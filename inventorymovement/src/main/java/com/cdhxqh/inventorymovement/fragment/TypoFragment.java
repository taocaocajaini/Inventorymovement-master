package com.cdhxqh.inventorymovement.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.PrintitemAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.ig_json.Ig_Json_Model;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.model.Printitem;
import com.cdhxqh.inventorymovement.utils.MessageUtils;
import com.cdhxqh.inventorymovement.utils.SocketClient;
import com.cdhxqh.inventorymovement.wight.SwipeRefreshLayout;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 条码打印*
 */
public class TypoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {
    private static final String TAG = "TypoFragment";
    private static final int RESULT_ADD_TOPIC = 100;

    private RadioButton poRadio;//查询po
    private RadioButton itemRadio;//查询主项目
    private EditText searchnumText;//PO值
    private ImageView searchImg;//搜索

    RecyclerView mRecyclerView;

    RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeLayout;

    PrintitemAdapter polineAdapter;
    /**
     * 暂无数据*
     */
    LinearLayout notLinearLayout;

    public String ponum;

    private Button button;

    private ArrayList<Printitem> items = null;

    private Handler mMainHandler, mChildHandler;
    private ProgressDialog mProgressDialog;

    private int page = 1;

//    private static final int mark=0; //库存转移标识

    private int curPage; //当前页

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_typo, container,
                false);

        findByIdView(view);

        mMainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                initProgressBar();
                mProgressDialog.setMessage("正在打印第" + msg.arg1 + "行");
                mProgressDialog.show();
                if (msg.arg1 == items.size()) {
                    mProgressDialog.dismiss();
                }
            }
        };

        return view;
    }

    /**
     * 初始化界面组件*
     */
    private void findByIdView(View view) {
        poRadio = (RadioButton) view.findViewById(R.id.po_check_title);
        itemRadio = (RadioButton) view.findViewById(R.id.item_check_title);
        searchnumText = (EditText) view.findViewById(R.id.ponum_edittext_id);
        searchImg = (ImageView) view.findViewById(R.id.search_imageview_id);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_topics);
        button = (Button) view.findViewById(R.id.printall);

        notLinearLayout = (LinearLayout) view.findViewById(R.id.have_not_data_id);

        poRadio.setChecked(true);
        poRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchnumText.setHint(getString(R.string.po_hint));
            }
        });
        itemRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchnumText.setHint(getString(R.string.item_hint));
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColor(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);


        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);

        polineAdapter = new PrintitemAdapter(getActivity(), ponum);
        mRecyclerView.setAdapter(polineAdapter);


        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(getActivity(), null,
                        getString(R.string.hint_loading_text), true, true);
                polineAdapter.removeAllData();
                getPoLineList();
            }
        });

        button.setOnClickListener(printOnClickListener);
        searchnumText.setOnEditorActionListener(editTextOnEditorActionListener);
//        searchnumText.setText("dh-16922");//101686
    }


    /**
     * 软键盘*
     */
    private TextView.OnEditorActionListener editTextOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                // 先隐藏键盘
                ((InputMethodManager) searchnumText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                getActivity().getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                mProgressDialog = ProgressDialog.show(getActivity(), null,
                        getString(R.string.hint_loading_text), true, true);
                polineAdapter.removeAllData();
                ponum = searchnumText.getText().toString();
                notLinearLayout.setVisibility(View.GONE);
                getPoLineList();
                return true;

            }
            return false;
        }
    };


    /**
     * 获取库存项目信息*
     */

    private void getPoLineList() {

        ponum = searchnumText.getText().toString();
        String url = null;
        if (poRadio.isChecked()) {
            url = ImManager.setPolineUrl(ponum, page, 20);
        } else if (itemRadio.isChecked()) {
            url = ImManager.serItemUrl(ponum, page, 20);
        }
        ImManager.getDataPagingInfo(getActivity(), url, new HttpRequestHandler<Results>() {
            @Override
            public void onSuccess(Results results) {
                Log.i(TAG, "data=" + results);
            }

            @Override
            public void onSuccess(Results results, int totalPages, int currentPage) {
                Log.e(TAG, "currentPage=" + currentPage);
                curPage = currentPage;
                mProgressDialog.dismiss();
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setLoading(false);
                notLinearLayout.setVisibility(View.GONE);
                try {
                    items = Ig_Json_Model.parsePrintitemFromString(results.getResultlist());
                    if (items == null || items.isEmpty()) {
                        notLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        polineAdapter.adddate(items);
                    }

                } catch (IOException e) {
                    notLinearLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(String error) {
                if (polineAdapter.getItemCount() != 0) {
                    MessageUtils.showMiddleToast(getActivity(), getString(R.string.loading_data_fail));
                } else {
                    notLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private View.OnClickListener printOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("请选择打印机名");
            builder.setSingleChoiceItems(R.array.print_item, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            sendData("PRINTER1");
                            break;
                        case 1:
                            sendData("PRINTER2");
                            break;
                        case 2:
                            sendData("PRINTER3");
                            break;
                        case 3:
                            sendData("PRINTER7");
                            break;
//                        case 4:
//                            sendData("PRINTER5");
//                            break;
//                        case 5:
//                            sendData("PRINTER6");
//                            break;
//                        case 6:
//                            sendData("PRINTER7");
//                            break;
//                        case 7:
//                            sendData("PRINTER8");
//                            break;
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    };

    private void sendData(String printer) {
        final ArrayList<Printitem> items = polineAdapter.getChecked();
        if (items != null && items.size() != 0) {
            Printitem poline = new Printitem();
            for (int i = 0; i < items.size(); i++) {
                poline = items.get(i);
                final String data = printer + "|" + poline.itemnum + "|"
                        + getText(poline.description) + "|"
                        + getText(poline.spec) + "|"
                        + getText(poline.udspec) + "|"
                        + poline.printqty + "|"
                        + poline.orderunit + "|"
                        + poline.storeloc + "|"
                        + getText(poline.companyname) + "|"
                        + poline.ponum + "|";
                MessageUtils.showMiddleToast(getActivity(), "正在打印第" + (i + 1) + "个");
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String result = null;
                        try {
                            result = SocketClient.sendAndGetReply("10.28.5.240", 9000, 120000, data.getBytes("GB2312"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        MessageUtils.showMiddleToast(getActivity(), s);
                    }
                }.execute();
            }
        } else {
            MessageUtils.showMiddleToast(getActivity(), "请选择打印项");
        }
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        polineAdapter.removeAllData();
        page = 1;
        getPoLineList();
    }

    @Override
    public void onLoad() {
        mSwipeLayout.setLoading(false);
        if (curPage == page) {
            MessageUtils.showMiddleToast(getActivity(), "已加载出全部数据");
        } else {
            page++;
            getPoLineList();
        }
    }

    class ChildThread extends Thread {
        public void run() {
            //初始化消息循环队列，需要在Handler创建之前
            Looper.prepare();
            mChildHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Message toMain = mMainHandler.obtainMessage();
                    SocketClient.sendAndGetReply("10.28.5.240", 9000, 120000, msg.obj.toString().getBytes());
                    mMainHandler.sendMessage(toMain);
                }

            };
            //启动子线程消息循环队列
            Looper.loop();
        }
    }

    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
    }

    private String getText(String text) {
        if (text == null || text.equals("")) {
            return "";
        } else {
            return text.contains("|") ? text.replace("|", "/") : text;
        }
    }
}
