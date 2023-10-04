package com.satmatgroup.newspindia.model

import java.io.Serializable

data class UserListModel(
    val clbal: String?,
    val rtid: String,
    val mobile: String,
    val cus_name: String
) : Serializable