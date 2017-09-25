package com.cdhxqh.inventorymovement.model;

/**
 * Created by Administrator on 2016/12/9.
 * 打印项目
 */
public class Printitem extends Entity{
    public String itemnum;//物资编码
    public String description;//描述
    public String spec;//规格
    public String udspec;//专业
    public String printqty;//输入数量
    public String orderunit;//订购单位
    public String storeloc;//发放单位
    public String companyname;//公司名称
    public String ponum;//PO

    public boolean ischeck;//是否被选中

    public String getItemnum() {
        return itemnum;
    }

    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUdspec() {
        return udspec;
    }

    public void setUdspec(String udspec) {
        this.udspec = udspec;
    }

    public String getPrintqty() {
        return printqty;
    }

    public void setPrintqty(String printqty) {
        this.printqty = printqty;
    }

    public String getOrderunit() {
        return orderunit;
    }

    public void setOrderunit(String orderunit) {
        this.orderunit = orderunit;
    }

    public String getStoreloc() {
        return storeloc;
    }

    public void setStoreloc(String storeloc) {
        this.storeloc = storeloc;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getPonum() {
        return ponum;
    }

    public void setPonum(String ponum) {
        this.ponum = ponum;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}
