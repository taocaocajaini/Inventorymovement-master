package com.cdhxqh.inventorymovement.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.constants.Constants;
import com.cdhxqh.inventorymovement.model.Invbalances;
import com.cdhxqh.inventorymovement.model.Option;
import com.cdhxqh.inventorymovement.model.Poline;
import com.cdhxqh.inventorymovement.utils.MessageUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 入库管理物料详情*
 */
public class PolineDetailActivity extends BaseActivity {

    private static final String TAG="PolineDetailActivity";
    private TextView titleTextView; // 标题

    private ImageView backImage; //返回

    private Poline poline;
    private String ponum;
    private int mark;

    /**
     * 界面说明*
     */

    private TextView itemnumText; //项目
    private TextView polinenumText;//行号
    private TextView descriptionText; //描述
    private TextView qty_title;//接收/退货
    private EditText qtyText; //接收/退货数量
    private TextView orderqtyText; //总数量
    private TextView orderunitText; //单位
    private TextView storelocText; //仓库
    private EditText binnumText;//货位号
    private TextView invtypeText;//库存类别
    private TextView tolotText;//批次

    private Button input;//提交


    private String type; //选择类型
    private String tolot; //批次


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poline_detail);

        initData();
        findViewById();
        initView();
    }

    private void initData() {
        poline = (Poline) getIntent().getSerializableExtra("poline");
        ponum = getIntent().getStringExtra("ponum");
        mark = getIntent().getIntExtra("mark", 0);
    }


    /**
     * 初始化界面控件*
     */
    private void findViewById() {
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);

        itemnumText = (TextView) findViewById(R.id.poline_itemnum);
        polinenumText = (TextView) findViewById(R.id.poline_polinenum);
        descriptionText = (TextView) findViewById(R.id.poline_desc);
        qty_title = (TextView) findViewById(R.id.item_recorde_title);
        qtyText = (EditText) findViewById(R.id.poline_recorde_num);
        orderqtyText = (TextView) findViewById(R.id.poline_orderqty);
        orderunitText = (TextView) findViewById(R.id.poline_orderunit);
        storelocText = (TextView) findViewById(R.id.poline_storeloc);
        binnumText = (EditText) findViewById(R.id.poline_binnum);
        invtypeText = (TextView) findViewById(R.id.poline_invtype);
        tolotText = (TextView) findViewById(R.id.lotnum_text);

        input = (Button) findViewById(R.id.input_button_id);
    }


    /**
     * 设置事件监听*
     */
    private void initView() {
        titleTextView.setText(getString(R.string.title_activity_invbalance_detail));
        backImage.setOnClickListener(backOnClickListener);

        if (mark == 1000) {
            qty_title.setText(R.string.poline_recorde_num);
            type = Constants.RECEIPT;
        } else if (mark == 1001) {
            qty_title.setText(R.string.poline_return_num);
            type = Constants.RETURN;
        }
        itemnumText.setText(poline.itemnum);
        polinenumText.setText(poline.polinenum);
        descriptionText.setText(poline.description);
        qtyText.setText(poline.orderqty);
        orderqtyText.setText(poline.orderqty);
        orderunitText.setText(poline.orderunit);
        storelocText.setText(poline.storeloc);
        binnumText.setText(poline.tobin);
        tolotText.setText(poline.tolot);

        invtypeText.setOnClickListener(new LayoutOnClickListener(1,Constants.INVTYPECODE));
        input.setOnClickListener(inputOnClickListener);
    }

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private class LayoutOnClickListener implements View.OnClickListener {
        int requestCode;
        int optiontype;

        private LayoutOnClickListener(int requestCode, int optiontype) {
            this.requestCode = requestCode;
            this.optiontype = optiontype;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PolineDetailActivity.this, OptionActivity.class);
            intent.putExtra("optiontype", optiontype);
            startActivityForResult(intent, requestCode);
        }
    }

    private View.OnClickListener inputOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOK()) {
                showProgressBar(R.string.submit_process_ing);
                final String  binnum = binnumText.getText().toString();
                final String  invtype = invtypeText.getText().toString();
                final int number = Integer.parseInt(qtyText.getText().toString());
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {


                        String data = getBaseApplication().getWsService().INV02RecByPOLine(type, getBaseApplication().getUsername(),
                                ponum, poline.polinenum, mark == 1000 ? number : -number, binnum, poline.tolot,invtype);
                        Log.i(TAG,"data="+data);
                        if (data == null) {
                            return "";
                        }
                        return data;
                    }

                    @Override
                    protected void onPostExecute(String o) {
                        super.onPostExecute(o);
                        colseProgressBar();

                        if (o.equals("")) {
                            MessageUtils.showMiddleToast(PolineDetailActivity.this, "操作失败");
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(o);
                            String result = jsonObject.getString("msg");
                            MessageUtils.showMiddleToast(PolineDetailActivity.this, result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MessageUtils.showMiddleToast(PolineDetailActivity.this, "操作失败");
                            PolineDetailActivity.this.finish();
                        }
                        PolineDetailActivity.this.finish();
                    }
                }.execute();
            }
        }
    };

    private boolean isOK() {
        if (qtyText == null || qtyText.getText().toString().equals("")
                || binnumText == null || binnumText.getText().toString().equals("")
                || invtypeText == null || invtypeText.getText().toString().equals("")) {
            Toast.makeText(PolineDetailActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(qtyText.getText().toString()) > Integer.parseInt(poline.orderqty)) {
            Toast.makeText(PolineDetailActivity.this, "请输入正确数量", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Option option;
        if (data != null) {
            switch (requestCode) {
                case 1:
                    option = (Option) data.getSerializableExtra("option");
                    invtypeText.setText(option.getName());
                    break;
            }
        }
    }
}
