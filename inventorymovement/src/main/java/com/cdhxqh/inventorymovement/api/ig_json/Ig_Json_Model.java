// Copyright 2004-present Facebook. All Rights Reserved.

package com.cdhxqh.inventorymovement.api.ig_json;

import android.util.Log;


import com.cdhxqh.inventorymovement.api.ig_json.impl.Invbalances_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Inventory_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Invreserve_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Invtype_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Item_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Itemreq_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Itemreqline_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Locations_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Printitem_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.WorkOrder_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Po_JsonHelper;
import com.cdhxqh.inventorymovement.api.ig_json.impl.Poline_JsonHelper;
import com.cdhxqh.inventorymovement.model.Invbalances;
import com.cdhxqh.inventorymovement.model.Inventory;
import com.cdhxqh.inventorymovement.model.Invreserve;
import com.cdhxqh.inventorymovement.model.Invtype;
import com.cdhxqh.inventorymovement.model.Item;
import com.cdhxqh.inventorymovement.model.Itemreq;
import com.cdhxqh.inventorymovement.model.Itemreqline;
import com.cdhxqh.inventorymovement.model.Locations;
import com.cdhxqh.inventorymovement.model.Printitem;
import com.cdhxqh.inventorymovement.model.WorkOrder;
import com.cdhxqh.inventorymovement.model.Po;
import com.cdhxqh.inventorymovement.model.Poline;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Helper class to parse the model.
 */
public class Ig_Json_Model {

    private static final String TAG = "Ig_Json_Model";

    /**
     * 主项目*
     */
    public static ArrayList<Item> parseItemFromString(String input) throws IOException {
        return Item_JsonHelper.parseFromJsonList(input);
    }

    /**
     * 主项目*
     */
    public static ArrayList<Invtype> parseInvtypeFromString(String input) throws IOException {
        return Invtype_JsonHelper.parseFromJsonList(input);
    }

    /**
     * 出库管理
     */
    public static ArrayList<WorkOrder> parseWorkOrderFromString(String input) throws IOException {
        return WorkOrder_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 出库管理Invreserve
     */
    public static ArrayList<Invreserve> parseInvreserveFromString(String input) throws IOException {
        return Invreserve_JsonHelper.parseFromJsonList(input);
    }
    /**
     *入库管理采购单*
     */
    public static ArrayList<Po> parsePoFromString(String input) throws IOException {
        return Po_JsonHelper.parseFromJsonList(input);
    }
    /**
     *入库管理物料单*
     */
    public static ArrayList<Poline> parsePolineFromString(String input) throws IOException {
        return Poline_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 库存使用情况
     */
    public static ArrayList<Inventory> parseInventoryFromString(String input) throws IOException {
        return Inventory_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 物资编码申请行
     */
    public static ArrayList<Itemreqline> parseItemreqlineFromString(String input) throws IOException {
        return Itemreqline_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 物资编码申请
     */
    public static ArrayList<Itemreq> parseItemreqFromString(String input) throws IOException {
        return Itemreq_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 解析库存转移
     */
    public static ArrayList<Locations> parseLocationsFromString(String input) throws IOException {
        return Locations_JsonHelper.parseFromJsonList(input);
    }
    /**
     * 解析库存项目
     */
    public static ArrayList<Invbalances> parseInvbalancesFromString(String input) throws IOException {
        return Invbalances_JsonHelper.parseFromJsonList(input);
    }

    /**
     *打印项目*
     */
    public static ArrayList<Printitem> parsePrintitemFromString(String input) throws IOException {
        return Printitem_JsonHelper.parseFromJsonList(input);
    }

}
