
package com.example.sofranewapp.data.model.showReviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowReviews {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DataPaginationComment data;

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

    public DataPaginationComment getData() {
        return data;
    }

    public void setData(DataPaginationComment data) {
        this.data = data;
    }

}
