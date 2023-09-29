package com.satmatgroup.newspindia.activities_aeps.twofactorAuthentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthRegistrationResponse {

    @SerializedName("response_code")
    @Expose
    private int response_code;

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("errorcode")
    @Expose
    private int errorcode;

    @SerializedName("message")
    @Expose
    private String message;

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
