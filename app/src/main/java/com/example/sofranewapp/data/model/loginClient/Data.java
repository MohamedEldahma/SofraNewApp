
package com.example.sofranewapp.data.model.loginClient;

import com.example.sofranewapp.data.model.Generated.GeneratedDataUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("user")
    @Expose
    private GeneratedDataUser user;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public GeneratedDataUser getUser() {
        return user;
    }

    public void setUser(GeneratedDataUser user) {
        this.user = user;
    }

}
