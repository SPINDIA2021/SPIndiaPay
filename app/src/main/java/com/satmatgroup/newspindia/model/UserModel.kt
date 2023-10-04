package com.satmatgroup.newspindia.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class UserModel(
   val name: String,
   val rtid: String,
    val mobile: String,
   val email: String,
    val id: Int,
    val onboarding: Boolean,
    val balance: Int,
    val aadhar: String,
    val pancard: String,
    val logintype: String)

{

   /* @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("rtid")
    @Expose
    private var rtid: String? = null

    @SerializedName("mobile")
    @Expose
    private var mobile: Long? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("onboarding")
    @Expose
    private var onboarding: Boolean? = null

    @SerializedName("balance")
    @Expose
    private var balance: Int? = null

    @SerializedName("aadhar")
    @Expose
    private var aadhar: String? = null

    @SerializedName("pancard")
    @Expose
    private var pancard: String? = null

    @SerializedName("logintype")
    @Expose
    private var logintype: String? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getRtid(): String? {
        return rtid
    }

    fun setRtid(rtid: String?) {
        this.rtid = rtid
    }

    fun getMobile(): Long? {
        return mobile
    }

    fun setMobile(mobile: Long?) {
        this.mobile = mobile
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getOnboarding(): Boolean? {
        return onboarding
    }

    fun setOnboarding(onboarding: Boolean?) {
        this.onboarding = onboarding
    }

    fun getBalance(): Int? {
        return balance
    }

    fun setBalance(balance: Int?) {
        this.balance = balance
    }

    fun getAadhar(): String? {
        return aadhar
    }

    fun setAadhar(aadhar: String?) {
        this.aadhar = aadhar
    }

    fun getPancard(): String? {
        return pancard
    }

    fun setPancard(pancard: String?) {
        this.pancard = pancard
    }

    fun getLogintype(): String? {
        return logintype
    }

    fun setLogintype(logintype: String?) {
        this.logintype = logintype
    }
*/
}