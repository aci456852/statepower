package com.example.statepower.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.File;
import java.util.Date;

public class Record {
    private int rid;
    private int rnumber;//序号
    private String ritem;//检查项目
    private String rseverity;//严重程度
    private String rspecific;//具体问题展现
    private String rproblem;//检查问题
    private byte[] rimg;//图片

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date rdate;//时间 YYYY-MM-DD

    public Record(){}

    public Record(int rnumber, String ritem, String rseverity, String rspecific, String rproblem, byte[] rimg) {
        this.rnumber = rnumber;
        this.ritem = ritem;
        this.rseverity = rseverity;
        this.rspecific = rspecific;
        this.rproblem = rproblem;
        this.rimg = rimg;
    }

    public Record(int rid, int rnumber, String ritem, String rseverity, String rspecific, String rproblem, byte[] rimg, Date rdate) {
        this.rid = rid;
        this.rnumber = rnumber;
        this.ritem = ritem;
        this.rseverity = rseverity;
        this.rspecific = rspecific;
        this.rproblem = rproblem;
        this.rimg = rimg;
        this.rdate = rdate;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRnumber() {
        return rnumber;
    }

    public void setRnumber(int rnumber) {
        this.rnumber = rnumber;
    }

    public String getRitem() {
        return ritem;
    }

    public void setRitem(String ritem) {
        this.ritem = ritem;
    }

    public String getRseverity() {
        return rseverity;
    }

    public void setRseverity(String rseverity) {
        this.rseverity = rseverity;
    }

    public String getRspecific() {
        return rspecific;
    }

    public void setRspecific(String rspecific) {
        this.rspecific = rspecific;
    }

    public String getRproblem() {
        return rproblem;
    }

    public void setRproblem(String rproblem) {
        this.rproblem = rproblem;
    }

    public byte[] getRimg() {
        return rimg;
    }

    public void setRimg(byte[] rimg) {
        this.rimg = rimg;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }
}
