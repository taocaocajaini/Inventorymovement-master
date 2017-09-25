package com.cdhxqh.inventorymovement.api.ig_json.impl;

import android.util.Log;

import com.cdhxqh.inventorymovement.api.ig_json.JsonHelper;
import com.cdhxqh.inventorymovement.model.Invtype;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.instagram.common.json.JsonFactoryHolder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Invtype解析*
 */
public final class Invtype_JsonHelper
        implements JsonHelper<Invtype> {
    private static final String TAG = "Invtype_JsonHelper";

    /**
     * 解析List*
     */
    public static ArrayList<Invtype> parseFromJsonList(JsonParser jp)
            throws IOException {

        ArrayList<Invtype> results = null;

        // validate that we're on the right token
        if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
            results = new ArrayList<Invtype>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                Invtype parsed = parseFromJson(jp);
                Log.i(TAG, "parsed=" + parsed);
                if (parsed != null) {
                    results.add(parsed);
                }
            }
        }


        return results;
    }


    /**
     * 解析Item
     */
    public static Invtype parseFromJson(JsonParser jp)
            throws IOException {
        Invtype instance = new Invtype();

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

    public static boolean processSingleField(Invtype instance, String fieldName, JsonParser jp)
            throws IOException {
        if ("VALUE".equals(fieldName)) {
            instance.VALUE = jp.getValueAsString();
            return true;
        } else if ("DESCRIPTION".equals(fieldName)) {
            instance.DESCRIPTION = jp.getValueAsString();
            return true;
        }

        return false;
    }

    /**
     * 解析Item*
     */
    public static ArrayList<Invtype> parseFromJsonList(String inputString)
            throws IOException {
        JsonParser jp = JsonFactoryHolder.APP_FACTORY.createParser(inputString);
        jp.nextToken();
        return parseFromJsonList(jp);
    }

    /**
     * 解析Item*
     */
    public static Invtype parseFromJson(String inputString)
            throws IOException {
        JsonParser jp = JsonFactoryHolder.APP_FACTORY.createParser(inputString);
        jp.nextToken();
        return parseFromJson(jp);
    }


}
