
package com.example.sofranewapp.data.model.cities;

import com.example.sofranewapp.data.model.Generated.DataPagination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cities {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataPagination data;

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

    public DataPagination getData() {
        return data;
    }

    public void setData(DataPagination data) {
        this.data = data;
    }

}
