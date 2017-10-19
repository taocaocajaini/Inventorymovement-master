package com.cdhxqh.inventorymovement.api;


import android.content.Context;
import android.util.Log;

import com.cdhxqh.inventorymovement.Application;
import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.bean.Results;
import com.cdhxqh.inventorymovement.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;


/**
 * Created by apple on 15/5/27.
 */
public class ImManager {

    private static Application mApp = Application.getInstance();
    private static AsyncHttpClient sClient = null;
    private static final String TAG = "ImManager";


    /**
     * 设置主项目接口*
     */
    public static String serItemUrl(String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.ITEM_APPID + "','objectname':'" + Constants.ITEM_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";
        } else {
            return "{'appid':'" + Constants.ITEM_APPID + "','objectname':'" + Constants.ITEM_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ITEMNUM':'" + search + "'}}";
        }

    }

    /**
     * 设置库存管理接口*
     */
    public static String setInvtypeUrl(String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.ALNDOMAIN_APPID + "','objectname':'" + Constants.ALNDOMAIN_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'DOMAINID':'KCTYPE'}}";
        } else {
            return "{'appid':'" + Constants.ALNDOMAIN_APPID + "','objectname':'" + Constants.ALNDOMAIN_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'DOMAINID':'KCTYPE','DESCRIPTION':'" + search + "'}}";
        }
    }


    /**
     * 出库管理
     **/
    public static String serWorkorderUrl(String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.WORKORDER_APPID + "','objectname':'" + Constants.WORKORDER_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";
        } else {
            return "{'appid':'" + Constants.WORKORDER_APPID + "','objectname':'" + Constants.WORKORDER_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'WONUM':'" + search + "'}}";
        }

    }

    /**
     * WPITEM
     **/
    public static String serInvreserveUrl(String wonum, String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.WORKORDER_APPID + "','objectname':'" + Constants.INVRESERVE_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'WONUM':'" + wonum + "'}}";
        } else {
            return "{'appid':'" + Constants.WORKORDER_APPID + "','objectname':'" + Constants.INVRESERVE_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'WONUM':'" + wonum + "','ITEMNUM':'" + search + "'}}";
        }

    }


    /**
     * 设置库存余量接口*
     */
    public static String sercInvbalancesUrl(String itemnum, String loaction, String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.INVBALANCES_APPID + "','objectname':'" + Constants.INVBALANCESS_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ITEMNUM':'=" + itemnum + "','LOCATION':'=" + loaction + "'}}";
        } else {
            return "{'appid':'" + Constants.INVBALANCES_APPID + "','objectname':'" + Constants.INVBALANCESS_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ITEMNUM':'=" + itemnum + "','LOTNUM':'" + search + "'}}";
        }

    }

    /**
     * 设置入库管理*
     */
    public static String setPoUrl(String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.RECEIPT_APPID + "','objectname':'" + Constants.PO_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";
        } else {
            return "{'appid':'" + Constants.RECEIPT_APPID + "','objectname':'" + Constants.PO_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'PONUM':'" + search + "'}}";
        }
    }

    /**
     * 设置入库物料管理*
     */
    public static String setPolineUrl(String ponum, int curpage, int showcount) {
        return "{'appid':'" + Constants.RECEIPT_APPID + "','objectname':'" + Constants.POLINE_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'PONUM':'" + ponum + "'}}";
    }

    /**
     * 库存使用情况搜索接口*
     */
    public static String searchInventoryUrl(String search, int curpage, int showcount) {
        return "{'appid':'" + Constants.INV_APPID + "','objectname':'" + Constants.INVENTORY_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ITEMNUM':'" + search + "'}}";
    }

    /**
     * 设置库存使用情况接口*
     */
    public static String serInventoryUrl(int curpage, int showcount) {
        return "{'appid':'" + Constants.INV_APPID + "','objectname':'" + Constants.INVENTORY_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read'}";
    }

    /**
     * 设置物资编码申请接口*
     */
    public static String serItemreqUrl(int curpage, int showcount) {
        return "{'appid':'" + Constants.ITEMREQ_APPID + "','objectname':'" + Constants.ITEMREQ_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','orderby':'RECORDERDATE DESC'}";
    }

    public static String serItemreqUrl(String search, int curpage, int showcount) {
        return "{'appid':'" + Constants.ITEMREQ_APPID + "','objectname':'" + Constants.ITEMREQ_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'ITEMREQNUM':'" + search + "'}}";
    }


    /**
     * 设置物资编码申请行接口*
     */
    public static String serItemreqLineUrl(int curpage, int showcount, String itemreqnum) {
        return "{'appid':'" + Constants.ITEMREQ_APPID + "','objectname':'" + Constants.ITEMREQLINE_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'itemreqnum':'" + itemreqnum + "'}}";
    }


    /**
     * 设置仓库接口*
     */
    public static String serLocationsUrl(String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.LOCATIONS_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'TYPE':'STOREROOM'}}";
        } else {
            return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.LOCATIONS_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'TYPE':'STOREROOM','LOCATION':'" + search + "'}}";
        }
    }

    /**
     * 设置库存转移接口*
     */
    public static String serInvbalancesUrl(String location, String search, int curpage, int showcount) {
        if (search.equals("")) {
            return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.INVBALANCES_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'LOCATION':'" + location + "','CURBAL':'>0'}}";
        } else {
            return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.INVBALANCES_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'LOCATION':'" + location + "','CURBAL':'>0" + "','ITEMNUM':'" + search + "'}}";
        }
    }

    /**
     * 设置库存仓库库位接口*
     */
    public static String serFrombinUrl(String location, String itemnum, int curpage, int showcount) {
        return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.INVBALANCES_NAME + "','curpage':" + curpage + ",'showcount':" + showcount + ",'option':'read','condition':{'LOCATION':'" + location + "','ITEMNUM':'" + itemnum + "'}}";

    }


    public static String getInvbalances() {
        return "{'appid':'" + Constants.LOCATIONS_APPID + "','objectname':'" + Constants.INVBALANCES_NAME + "','option':'read'}";
    }


    /**
     * 使用用户名密码登录
     *
     * @param cxt
     * @param username 用户名
     * @param password 密码
     * @param imei     密码
     * @param handler  返回结果处理
     */
    public static void loginWithUsername(final Context cxt, final String username, final String password, String imei,
                                         final HttpRequestHandler<String> handler) {
        Log.e(TAG, "url=" + Constants.SIGN_IN_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginid", username);
        params.put("password", password);
        params.put("imei", imei);
        client.post(Constants.SIGN_IN_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "SstatusCode=" + statusCode + "responseString=" + responseString);
                SafeHandler.onFailure(handler, IMErrorType.errorMessage(cxt, IMErrorType.ErrorLoginFailure));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i(TAG, "SstatusCode=" + statusCode + "responseString=" + responseString);
                if (statusCode == 200) {
                    String errmsg = JsonUtils.parsingAuthStr(cxt, responseString);
                    SafeHandler.onSuccess(handler, errmsg);
                }
            }


        });


    }


    /**
     * 不分页获取信息方法*
     */
    public static void getData(final Context cxt, String data, final HttpRequestHandler<Results> handler) {
        Log.e(TAG, "data=" + data);
        Log.e(TAG, "url=" + Constants.BASE_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("data", data);
        client.get(Constants.BASE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, cxt.getString(R.string.get_data_info_fail));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Results result = JsonUtils.parsingResults1(cxt, responseString);
                Log.i(TAG, "getCurpage()=" + result.getCurpage() + " result.getShowcount()=" + result.getShowcount());
                SafeHandler.onSuccess(handler, result, result.getCurpage(), result.getShowcount());
            }


        });
    }


    /**
     * 解析返回的结果--分页*
     */
    public static void getDataPagingInfo(final Context cxt, String data, final HttpRequestHandler<Results> handler) {
        Log.e(TAG, "data=" + data);
        Log.e(TAG, "url=" + Constants.BASE_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("data", data);
        client.get(Constants.BASE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, cxt.getString(R.string.get_data_info_fail));
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e(TAG, "responseString=" + responseString);
                Results result = JsonUtils.parsingResults(cxt, responseString);
                if (null == result) {
                    SafeHandler.onFailure(handler, cxt.getString(R.string.get_data_info_fail));
                } else {
                    SafeHandler.onSuccess(handler, result, result.getShowcount(), result.getCurpage());
                }
            }

        });
        client.setConnectTimeout(5 * 1000);
    }


    /**
     * 生成物资编码*
     *
     * @ cxt 上下问
     * useruid 用户唯一ID
     * itemreqid 编码申请单唯一标识
     */
    public static void setItemNumber(final Context cxt, final String useruid, final String itemreqid,
                                     final HttpRequestHandler<String> handler) {

        Log.i(TAG, "itemreqid=" + itemreqid);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("useruid", useruid);
        params.put("itemreqid", itemreqid);
        client.post(Constants.ITEM_GENERATE_URL, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, IMErrorType.errorMessage(cxt, IMErrorType.ErrorLoginFailure));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i(TAG, "SstatusCode=" + statusCode + "responseString=" + responseString);
            }

        });


    }


    /**
     * 发送工作流
     *
     * @cxt 上下文
     * @ownertable 工作流对应的主表名称
     * @ownerid 工作流对应的主表主键
     * @processname 工作流名称
     * @useruid 当前登录人的唯一标识
     */
    public static void startFlow(final Context cxt, final String ownertable, final String ownerid, final String processname, final String useruid,
                                 final HttpRequestHandler<String> handler) {


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ownertable", ownertable);
        params.put("ownerid", ownerid);
        params.put("processname", processname);
        params.put("useruid", useruid);
        client.post(Constants.START_FLOW_URL, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, IMErrorType.errorMessage(cxt, IMErrorType.ErrorLoginFailure));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i(TAG, "SstatusCode=" + statusCode + "responseString=" + responseString);

            }

        });


    }

    /**
     * 审批工作流
     *
     * @ownertable 工作流对应的主表名称
     * @ownerid 工作流对应的主表主键
     * @memo 审批意见
     * @selectWhat 是否接受：true/false
     * useruid 当前登录人的唯一标识
     */
    public static void approvalFlow(final Context cxt, final String ownertable, final String ownerid, final String memo, final String selectWhat, final String useruid,
                                    final HttpRequestHandler<String> handler) {


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ownertable", ownertable);
        params.put("ownerid", ownerid);
        params.put("memo", memo);
        params.put("selectWhat", selectWhat);
        params.put("useruid", useruid);
        client.post(Constants.APPROVAL_FLOW_URL, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                SafeHandler.onFailure(handler, IMErrorType.errorMessage(cxt, IMErrorType.ErrorLoginFailure));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i(TAG, "SstatusCode=" + statusCode + "responseString=" + responseString);
            }

        });


    }


}
