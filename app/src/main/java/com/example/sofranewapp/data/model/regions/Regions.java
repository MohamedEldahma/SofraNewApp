
package com.example.sofranewapp.data.model.regions;

import com.example.sofranewapp.data.model.Generated.DataPagination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Regions {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataPagination dataPagination;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataPagination getDataPagination() {
        return dataPagination;
    }

    public void setDataPagination(DataPagination dataPagination) {
        this.dataPagination = dataPagination;
    }

}
