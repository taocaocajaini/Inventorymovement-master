package com.cdhxqh.inventorymovement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.adapter.ImageLoadAdapter;
import com.cdhxqh.inventorymovement.api.HttpRequestHandler;
import com.cdhxqh.inventorymovement.api.ImManager;
import com.cdhxqh.inventorymovement.api.JsonUtils;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.constants.Constants;
import com.cdhxqh.inventorymovement.utils.AccountUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import java.util.ArrayList;

/**
 * 图片选择*
 */
public class PhotoActivity extends BaseActivity implements ImageLoadAdapter.OnRecyclerViewItemClickListener {

    private static final String TAG = "PhotoActivity";

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;


    private ImageLoadAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数


    private static final String URL_STRING = "/share/doclinks/";
    /**
     * 测试环境的url地址
     **/
    private static final String CESHI_URL = "http://qaseam.mywind.com.cn/";
    /**
     * 正式环境的图片URL地址
     **/
    private static final String ZHENGSHI_URL = "http://eam.mywind.com.cn/";

    private static final int PICK_PHOTO = 1;

    /**
     * 返回按钮
     */
    private ImageView backImageView;
    /**
     * 标题
     */
    private TextView titleTextView;
    /**
     * text_hint*
     */
    private TextView textView;

    /**
     * 上传附件
     **/
    private Button uploadImgeView;


    /**
     * 表名*
     */
    private String ownertable;
    /**
     * 主键ID*
     */
    private String ownerid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo1);
        getInitData();

        findViewById();
        initView();
        initWidget();
//        showProgressBar(true, "获取图片");
//        getData();


    }

    private void getInitData() {
//        ownertable = getIntent().getExtras().getString("ownertable");
//        ownerid = getIntent().getExtras().getString("ownerid");
//        Log.i(TAG, "ownertable=" + ownertable + ",ownerid=" + ownerid);
    }

    protected void findViewById() {
        backImageView = (ImageView) findViewById(R.id.drawer_indicator);
        titleTextView = (TextView) findViewById(R.id.drawer_text);
        textView = (TextView) findViewById(R.id.text_id);
        uploadImgeView = (Button) findViewById(R.id.btn_transfer_btn);

    }

    protected void initView() {
        backImageView.setOnClickListener(backImageViewOnClickListener);
        titleTextView.setText(getString(R.string.attachment_details));
        uploadImgeView.setVisibility(View.VISIBLE);

        uploadImgeView.setOnClickListener(uploadBtnOnClickListener);
    }


    private View.OnClickListener uploadBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            intent.setClass(PhotoActivity.this, WxDemoActivity.class);
            intent.putExtra("isCamera", 1);
            startActivityForResult(intent, 0);
        }
    };


    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImageLoadAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    private View.OnClickListener backImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private void showResult(ArrayList<ImageItem> paths) {

        adapter.setImages(paths);
        if (paths == null || paths.size() == 0) {
            textView.setVisibility(View.VISIBLE);

        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
//            case IMAGE_ITEM_ADD:
//                //打开选择,本次允许选择的数量
//                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
//                Intent intent = new Intent(this, ImageGridActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SELECT);
//                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra("results", ImagePicker.SERVER_IMAGE_ITEMS);
//                intentPreview.putExtra("IMAGE_URL", Constants.HTTP_API_URL + Constants.WORK_FLOW_URL);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "resultCode=" + resultCode + ",requestCode=" + requestCode);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);
                if (adapter.getItemCount() != 0) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }

            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);

                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }
    }


//    private void getData() {
//        ImManager.getData(this, ImManager.getDoclinks(ownertable, ownerid), new HttpRequestHandler<Results>() {
//            @Override
//            public void onSuccess(Results results) {
//                colseProgressBar();
//                ArrayList<Doclinks> item = JsonUtils.parsingDoclinks(PhotoActivity.this, results.getResultlist());
//                if (item == null || item.isEmpty()) {
//                    textView.setVisibility(View.VISIBLE);
//                } else {
//                    if (item != null || item.size() != 0) {
//                        textView.setVisibility(View.GONE);
//                        for (int i = 0; i < item.size(); i++) {
//                            String url = item.get(i).URL;
//                            if (url != null) {
//                                //拼接图片的URL
//                                ImageItem imageItem = new ImageItem();
//                                imageItem.path = getUrls(url);
//                                imageItem.name = item.get(i).getDOCINFOID();
//                                selImageList.add(imageItem);
//                            }
//                        }
//                        if (selImageList != null || selImageList.size() != 0) {
//                            showResult(selImageList);
//                        } else {
//                            textView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onSuccess(Results results, int totalPages, int currentPage) {
//                colseProgressBar();
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//                colseProgressBar();
//                ;
//            }
//        });
//    }


    /**
     * 拼接图片的URl
     **/
    private String getUrls(String url) {
        //测试环境的附件
        String ip_url = Constants.HTTP_API_URL;
        String imagesUrl = null;
        if (ip_url.equals("http://eamapp.mywind.com.cn:9080")) { //正式
            imagesUrl = ZHENGSHI_URL;
        } else if (ip_url.equals("http://qaseamapp.mywind.com.cn:9080")) { //测试
            imagesUrl = CESHI_URL;


        }
        url = url.replace(URL_STRING, "");


        return imagesUrl + url;
    }


}