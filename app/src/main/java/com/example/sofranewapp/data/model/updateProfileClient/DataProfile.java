
package com.example.sofranewapp.data.model.updateProfileClient;

import com.example.sofranewapp.data.model.Generated.GeneratedDataUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataProfile {

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
