package com.cdhxqh.inventorymovement.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.constants.Constants;
import com.cdhxqh.inventorymovement.model.Option;
import com.cdhxqh.inventorymovement.model.Poline;
import com.cdhxqh.inventorymovement.model.Printitem;
import com.cdhxqh.inventorymovement.utils.AccountUtils;
import com.cdhxqh.inventorymovement.utils.MessageUtils;
import com.cdhxqh.inventorymovement.utils.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 打印poline行详情*
 */
public class PrintPolineDetailActivity extends BaseActivity {

    private static final String TAG="PrintPolineDetailActivity";
    private TextView titleTextView; // 标题

    private ImageView backImage; //返回

    private Printitem printitem;
    private int mark;

    /**
     * 界面说明*
     */

    private TextView itemnum; //物资编码
    private TextView description;//描述
    private TextView spec; //规格
    private TextView udspec;//专业
    private EditText printqty; //输入数量
    private TextView orderunit; //订购单位
    private TextView storeloc; //发放单位
    private TextView companyname; //公司名称
    private TextView ponum;//PO

    private Button print;//打印

    private Handler mMainHandler, mChildHandler;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printpoline_detail);

        initData();
        findViewById();
        mMainHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(PrintPolineDetailActivity.this,"正在打印",Toast.LENGTH_SHORT).show();
            }
        };
        initView();
    }

    private void initData() {
        printitem = (Printitem) getIntent().getSerializableExtra("printitem");
    }


    /**
     * 初始化界面控件*
     */
    private void findViewById() {
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        backImage = (ImageView) findViewById(R.id.drawer_indicator);

        itemnum = (TextView) findViewById(R.id.itemnum);
        description = (TextView) findViewById(R.id.description);
        spec = (TextView) findViewById(R.id.spec);
        udspec = (TextView) findViewById(R.id.udspec);
        printqty = (EditText) findViewById(R.id.printqty);
        orderunit = (TextView) findViewById(R.id.orderunit);
        storeloc = (TextView) findViewById(R.id.storeloc);
        companyname = (TextView) findViewById(R.id.companyname);
        ponum = (TextView) findViewById(R.id.ponum);

        print = (Button) findViewById(R.id.print);
    }


    /**
     * 设置事件监听*
     */
    private void initView() {
        titleTextView.setText(getString(R.string.title_printitem_detail));
        backImage.setOnClickListener(backOnClickListener);

        itemnum.setText(printitem.itemnum);
        description.setText(printitem.description);
        spec.setText(printitem.spec);
        udspec.setText(printitem.udspec);
        printqty.setText(printitem.printqty);
        orderunit.setText(printitem.orderunit);
        storeloc.setText(printitem.storeloc);
        companyname.setText(printitem.companyname);
        ponum.setText(printitem.ponum);

        print.setOnClickListener(printOnClickListener);
    }

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener printOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PrintPolineDetailActivity.this);
//            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("请选择打印机名");
            builder.setSingleChoiceItems(R.array.print_item, -1, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    switch (which){
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

    private void sendData(String printer){
        final String data = printer + "|" + itemnum.getText().toString() + "|"
                + getText(description.getText().toString()) + "|" + getText(spec.getText().toString()) + "|"
                + getText(udspec.getText().toString()) + "|" + printqty.getText().toString() + "|"
                + orderunit.getText().toString() + "|" + storeloc.getText().toString() + "|"
                + getText(companyname.getText().toString()) + "|" + ponum.getText().toString() + "|";
        MessageUtils.showMiddleToast(PrintPolineDetailActivity.this, "正在打印");
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
                MessageUtils.showMiddleToast(PrintPolineDetailActivity.this, s);
            }
        }.execute();
    }

    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(PrintPolineDetailActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
    }

    private String getText(String text){
        if (text==null||text.equals("")){
            return "";
        }else {
            return text.contains("|")?text.replace("|", "/"):text;
        }
    }
}
