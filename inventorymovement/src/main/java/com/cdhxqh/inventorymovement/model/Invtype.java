package com.cdhxqh.inventorymovement.model;

import java.io.Serializable;

/**
 * Created by think on 2016/6/27.
 */
public class Invtype implements Serializable {
    public String VALUE;
    public String DESCRIPTION;

    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }
}
