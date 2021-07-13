package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author david
 * @date 2021/7/4 - 12:17
 */
public class CaseInfo2 {
    //CaseId Name Url Type
    @Excel(name="CaseId")
    private int id;
    @Excel(name="Name")
    private String name;
    @Excel(name="Url")
    private String url;
    @Excel(name="Type")
    private String method;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "CaseInfo2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
