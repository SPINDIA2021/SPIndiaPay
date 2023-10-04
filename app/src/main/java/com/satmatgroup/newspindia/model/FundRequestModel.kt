package com.satmatgroup.newspindia.model

data class FundRequestModel(
    val rtid: String,
    val mobile: String,
    val cus_name: String,
    val logintype: String,
    val pay_amount: String,
    val pay_bank: String,
    val ref_no: String,
    val req_date: String,
    val req_id: String,
    val req_status: String,
    val request_from: String,
    val res_date: String
)