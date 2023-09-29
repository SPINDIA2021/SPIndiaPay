package com.satmatgroup.newspindia.activities_aeps.twofactorAuthentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseAUthRegistrationResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AuthRegistrationResponse authRegistrationResponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthRegistrationResponse getAthRegistrationResponse() {
        return authRegistrationResponse;
    }

    public void setData(AuthRegistrationResponse panResponse) {
        this.authRegistrationResponse = panResponse;
    }



}
