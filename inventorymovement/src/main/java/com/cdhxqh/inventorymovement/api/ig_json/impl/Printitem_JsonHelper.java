package com.cdhxqh.inventorymovement.api.ig_json.impl;

import android.util.Log;

import com.cdhxqh.inventorymovement.api.ig_json.JsonHelper;
import com.cdhxqh.inventorymovement.model.Printitem;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.instagram.common.json.JsonFactoryHolder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Item解析*
 */
public final class Printitem_JsonHelper
        implements JsonHelper<Printitem> {
    private static final String TAG = "Printitem_JsonHelper";

    /**
     * 解析List*
     */
    public static ArrayList<Printitem> parseFromJsonList(JsonParser jp)
            throws IOException {

        ArrayList<Printitem> results = null;

        // validate that we're on the right token
        if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
            results = new ArrayList<Printitem>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                Printitem parsed = parseFromJson(jp);
                Log.i(TAG, "parsed=" + parsed);
                if (parsed != null) {
                    results.add(parsed);
                }
            }
        }


        return results;
    }


    /**
     * 解析Po
     */
    public static Printitem parseFromJson(JsonParser jp)
            throws IOException {
        Printitem instance = new Printitem();

        // validate that we're on the right token
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            jp.skipChildren();
            return null;
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            jp.nextToken();
            processSingleField(instance, fieldName, jp);
            jp.skipChildren();
        }

        return instance;
    }

    public static boolean processSingleField(Printitem instance, String fieldName, JsonParser jp)
            throws IOException {
        if ("ITEMNUM".equals(fieldName)) {
            instance.itemnum = jp.getValueAsString();
            return true;
        } else if ("DESCRIPTION".equals(fieldName)) {
            instance.description = jp.getValueAsString();
            return true;
        } else if ("IN20".equals(fieldName)) {
            instance.spec = jp.getValueAsString();
            return true;
        }else if ("UDSPEC".equals(fieldName)) {
            instance.udspec = jp.getValueAsString();
            return true;
        }else if ("PRINTQTY".equals(fieldName)) {
            instance.printqty = jp.getValueAsString();
            return true;
        }else if ("ORDERUNIT".equals(fieldName)) {
            instance.orderunit = jp.getValueAsString();
            return true;
        }else if ("STORELOC".equals(fieldName)) {
            instance.storeloc = jp.getValueAsString();
            return true;
        }else if ("COMPANYNAME".equals(fieldName)) {
            instance.companyname = jp.getValueAsString();
            return true;
        }else if ("PONUM".equals(fieldName)) {
            instance.ponum = jp.getValueAsString();
            return true;
        }

        return false;
    }

    /**
     * 解析Po*
     */
    public static ArrayList<Printitem> parseFromJsonList(String inputString)
            throws IOException {
        JsonParser jp = JsonFactoryHolder.APP_FACTORY.createParser(inputString);
        jp.nextToken();
        return parseFromJsonList(jp);
    }

    /**
     * 解析Item*
     */
    public static Printitem parseFromJson(String inputString)
            throws IOException {
        JsonParser jp = JsonFactoryHolder.APP_FACTORY.createParser(inputString);
        jp.nextToken();
        return parseFromJson(jp);
    }


}
