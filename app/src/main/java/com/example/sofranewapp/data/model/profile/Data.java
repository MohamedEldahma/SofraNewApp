
package com.example.sofranewapp.data.model.profile;

import com.example.sofranewapp.data.model.Generated.GeneratedDataUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    @Expose
    private GeneratedDataUser user;

    public GeneratedDataUser getUser() {
        return user;
    }

    public void setUser(GeneratedDataUser user) {
        this.user = user;
    }

}
