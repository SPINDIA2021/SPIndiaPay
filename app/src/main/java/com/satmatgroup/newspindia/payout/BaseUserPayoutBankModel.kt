package com.satmatgroup.newspindia.payout

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class BaseUserPayoutBankModel {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("data")
    @Expose
    private var data: ArrayList<UserPayoutBankModel>? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getData(): ArrayList<UserPayoutBankModel>? {
        return data
    }

    fun setData(data: ArrayList<UserPayoutBankModel>?) {
        this.data = data
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}