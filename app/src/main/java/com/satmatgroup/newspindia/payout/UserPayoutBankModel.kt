package com.satmatgroup.newspindia.payout

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UserPayoutBankModel (
   /* val payout_bank_id: String,
    val cus_id: String,
    val bankName: String,
    val bankAccount: String,
    val bankIFSC: String,
    val accountHolderName: String*/
        ){
    @SerializedName("beneid")
    @Expose
    private var beneid: String? = null

    @SerializedName("merchantcode")
    @Expose
    private var merchantcode: String? = null

    @SerializedName("bankname")
    @Expose
    private var bankname: String? = null

    @SerializedName("account")
    @Expose
    private var account: String? = null

    @SerializedName("ifsc")
    @Expose
    private var ifsc: String? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("account_type")
    @Expose
    private var accountType: String? = null

    @SerializedName("verified")
    @Expose
    private var verified: String? = null

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("remarks")
    @Expose
    private var remarks: Any? = null

    fun getBeneid(): String? {
        return beneid
    }

    fun setBeneid(beneid: String?) {
        this.beneid = beneid
    }

    fun getMerchantcode(): String? {
        return merchantcode
    }

    fun setMerchantcode(merchantcode: String?) {
        this.merchantcode = merchantcode
    }

    fun getBankname(): String? {
        return bankname
    }

    fun setBankname(bankname: String?) {
        this.bankname = bankname
    }

    fun getAccount(): String? {
        return account
    }

    fun setAccount(account: String?) {
        this.account = account
    }

    fun getIfsc(): String? {
        return ifsc
    }

    fun setIfsc(ifsc: String?) {
        this.ifsc = ifsc
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getAccountType(): String? {
        return accountType
    }

    fun setAccountType(accountType: String?) {
        this.accountType = accountType
    }

    fun getVerified(): String? {
        return verified
    }

    fun setVerified(verified: String?) {
        this.verified = verified
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getRemarks(): Any? {
        return remarks
    }

    fun setRemarks(remarks: Any?) {
        this.remarks = remarks
    }

}