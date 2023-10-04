package com.satmatgroup.newspindia.recharge_services

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.satmatgroup.newspindia.R
import com.satmatgroup.newspindia.adapters_recyclerview.OperatorListAdapter
import com.satmatgroup.newspindia.model.OperatorsModel
import com.satmatgroup.newspindia.model.UserModel
import com.satmatgroup.newspindia.network_calls.AppApiCalls
import com.satmatgroup.newspindia.network_calls.AppApiUrl
import com.satmatgroup.newspindia.reports.recharge_reports.AllRechargeReportsActivity
import com.satmatgroup.newspindia.utils.AppCommonMethods
import com.satmatgroup.newspindia.utils.AppConstants
import com.satmatgroup.newspindia.utils.AppPrefs
import com.satmatgroup.newspindia.utils.toast
import kotlinx.android.synthetic.main.activity_mobile_postpaid.*
import kotlinx.android.synthetic.main.activity_mobile_postpaid.view.*


import kotlinx.android.synthetic.main.layout_dialog_confirmpin.*
import kotlinx.android.synthetic.main.layout_list_bottomsheet.view.*
import org.json.JSONObject
import java.util.ArrayList

class MobilePostpaidActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    OperatorListAdapter.ListAdapterListener {

    lateinit var operatorAdapter: OperatorListAdapter
    var operatorsModelArrayList = ArrayList<OperatorsModel>()
    var bottomSheetDialog: BottomSheetDialog? = null
    
    lateinit var userModel: UserModel


    var operator_code: String = ""
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_postpaid)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        custToolbar.ivBackBtn.setOnClickListener {
            onBackPressed()
        }

        val gson = Gson()
        val json = AppPrefs.getStringPref(AppConstants.USER_MODEL, this)
        userModel = gson.fromJson(json, UserModel::class.java)

        getBalanceApi(userModel.mobile)

        cvWalletBalancePostpaid.setBackgroundResource(R.drawable.bg_leftcurved)
        cvBrowsePlans.setBackgroundResource(R.drawable.bg_rightcurved)

        getOperatorApi(AppConstants.OPERATOR_POSTPAID)

        tvChooseOperator.setOnClickListener {
            showOperatorsBottomSheet()
        }

        cvRechargePostpaidBtn.setOnClickListener {

            if (!AppCommonMethods.checkForMobile(etMobileNumberPostpaid)) {

                etMobileNumberPostpaid.requestFocus()
               etMobileNumberPostpaid.error =
                    getString(R.string.error_mobile_number)
                return@setOnClickListener
            } else if (etAmountPostpaid.text.isNullOrEmpty()) {

                etAmountPostpaid.requestFocus()
                etAmountPostpaid.error =
                   getString(R.string.error_invalid_amount)
                return@setOnClickListener
            } else {

                checkIfSameRecharge(userModel.mobile,
                    etMobileNumberPostpaid.text.toString(),
                    etAmountPostpaid.text.toString(),operator_code)

            }


        }
    }


    //API CALL FUNCTION DEFINITION
    private fun getOperatorApi(
        operator_type: String
    ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this@MobilePostpaidActivity,
                AppConstants.OPERATOR_API,
                this
            )
            mAPIcall.getOperators(operator_type)

        } else {
            toast(getString(R.string.error_internet))
        }
    }

    private fun getBalanceApi(
        rtid: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this@MobilePostpaidActivity).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this@MobilePostpaidActivity,
                AppConstants.BALANCE_API,
                this
            )
            mAPIcall.getBalance(rtid)

        } else {
            toast(getString(R.string.error_internet))
        }
    }


    private fun checkIfSameRecharge(
        rtid: String,
        rec_mobile: String,
        amount: String,
        operator: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this@MobilePostpaidActivity).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this@MobilePostpaidActivity,
                AppConstants.CHECK_SAME_RECHARGE_API,
                this
            )
            mAPIcall.checkIfSameRecharge(rtid, rec_mobile, amount, operator)

        } else {
            toast(getString(R.string.error_internet))
        }
    }

    private fun verifyPin(
        mobile: String,
        pin: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this@MobilePostpaidActivity).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this@MobilePostpaidActivity,
                AppConstants.VERFY_PIN_API,
                this
            )
            mAPIcall.verifyPin(mobile, pin)

        } else {
           toast(getString(R.string.error_internet))
        }
    }

    private fun rechargeApi(
        rtid: String,
        rec_mobile: String,
        amount: String,
        operator: String,
        logintype: String,
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this@MobilePostpaidActivity).isNetworkAvailable) {
            val mAPIcall = AppApiCalls(
                this@MobilePostpaidActivity,
                AppConstants.RECHARGE_API,
                this
            )
            mAPIcall.rechargeApi(rtid, rec_mobile, amount, operator, logintype)

        } else {
            toast(getString(R.string.error_internet))
        }
    }


    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(AppConstants.OPERATOR_API)) {
            Log.e(AppConstants.OPERATOR_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val operatorname = notifyObjJson.getString("operatorname")
                    Log.e("operator_name ", operatorname)
                    val operatorsModel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            OperatorsModel::class.java
                        )

                    operatorsModelArrayList.add(operatorsModel)
                }


            } else {

                progress_bar.visibility = View.GONE


            }
        }
        if (flag.equals(AppConstants.BALANCE_API)) {
            progress_bar.visibility = View.GONE
            Log.e(AppConstants.BALANCE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val messageCode = jsonObject.getString(AppConstants.MESSAGE)

            //   val token = jsonObject.getString(AppConstants.TOKEN)
            Log.e(AppConstants.STATUS, status)
            Log.e(AppConstants.MESSAGE, messageCode)
            if (status.contains(AppConstants.TRUE)) {


                tvWalletBalance.text =
                    "${getString(R.string.Rupee)} ${jsonObject.getString(AppConstants.WALLETBALANCE)}"
                /* tvAepsBalance.text =
                     "${getString(R.string.Rupee)} ${jsonObject.getString(AEPSBALANCE)}"*/

            } else {
                progress_bar.visibility = View.GONE

                if (messageCode.equals(getString(R.string.error_expired_token))) {
                    AppCommonMethods.logoutOnExpiredDialog(this@MobilePostpaidActivity)
                } else {
                    toast(messageCode.trim())
                }
            }
        }
        if (flag.equals(AppConstants.CHECK_SAME_RECHARGE_API)) {
            Log.e(AppConstants.CHECK_SAME_RECHARGE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val message = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
                verifyPin(userModel.mobile, AppPrefs.getStringPref("AppPassword",this@MobilePostpaidActivity).toString())
                //  confirmPinDialog()

            } else {

                progress_bar.visibility = View.GONE
                showMessageDialog(getString(R.string.error_attention), message)

            }
        }
        if (flag.equals(AppConstants.RECHARGE_API)) {
            Log.e(AppConstants.RECHARGE_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains(AppConstants.TRUE)) {
                progress_bar.visibility = View.GONE

                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)

                showSuccessOrFailedDialog(getString(R.string.status), message)


            } else {
                val resultObject = jsonObject.getJSONObject("result")
                val message = resultObject.getString(AppConstants.MESS)
                progress_bar.visibility = View.GONE
                showSuccessOrFailedDialog(getString(R.string.status), message)


            }
        }
        if (flag.equals(AppConstants.VERFY_PIN_API)) {
            Log.e(AppConstants.VERFY_PIN_API, result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            val message = jsonObject.getString(AppConstants.MESSAGE)
            Log.e(AppConstants.STATUS, status)
            if (status.contains(AppConstants.TRUE)) {
                progress_bar.visibility = View.GONE
                rechargeApi(
                    userModel.rtid, etMobileNumberPostpaid.text.toString(),
                    etAmountPostpaid.text.toString(), operator_code, userModel.logintype
                )

            } else {

                progress_bar.visibility = View.GONE
                showMessageDialog(getString(R.string.error_attention), message)

            }
        }
    }

    private fun showOperatorsBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet, null)
        view.rvspinner.apply {

            layoutManager = LinearLayoutManager(this@MobilePostpaidActivity)
            view.rvspinner.addItemDecoration(DividerItemDecoration(this@MobilePostpaidActivity, LinearLayoutManager.VERTICAL))

            operatorAdapter = OperatorListAdapter(
                context, operatorsModelArrayList, this@MobilePostpaidActivity
            )
            view.rvspinner.adapter = operatorAdapter
        }

        bottomSheetDialog = BottomSheetDialog(this@MobilePostpaidActivity, R.style.SheetDialog)
        bottomSheetDialog!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = 600
        bottomSheetDialog!!.show()
    }

    override fun onClickAtOKButton(operatorsModel: OperatorsModel?) {
        if (operatorsModel != null) {
            tvChooseOperator.text = operatorsModel.operatorname


            operator_code = operatorsModel.opcodenew!!.trim()
//            opName = mobileRechargeModal.operatorname!!.trim()
            Glide.with(this)
                .load(AppApiUrl.IMAGE_URL + operatorsModel.operator_image)
                .into(ivOperatorImagePostpaid)
            bottomSheetDialog!!.dismiss()
        }
    }


    private fun clearData() {
        etMobileNumberPostpaid.setText("")
        tvChooseOperator.setText("")
        ivOperatorImagePostpaid.setImageDrawable(resources.getDrawable(R.drawable.icons_cellulartower))
        etAmountPostpaid.setText("")
    }

    override fun onResume() {
        super.onResume()
        clearData()
    }

    private fun showMessageDialog(title: String, message: String) {
        val builder1 =
            AlertDialog.Builder(this@MobilePostpaidActivity)
        builder1.setTitle("" + title)
        builder1.setMessage("" + message)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->

            dialog.dismiss()
        }
        /*
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun showSuccessOrFailedDialog(title: String, message: String) {
        val builder1 =
            AlertDialog.Builder(this@MobilePostpaidActivity)
        builder1.setTitle("" + title)
        builder1.setMessage("" + message)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->

            clearData()
            val intent = Intent(this@MobilePostpaidActivity, AllRechargeReportsActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        /*
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        val alert11 = builder1.create()
        alert11.show()
    }

    fun confirmPinDialog() {
        dialog = Dialog(this@MobilePostpaidActivity, R.style.ThemeOverlay_MaterialComponents_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_confirmpin)

        dialog.etPin.requestFocus()
        dialog.tvDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.etPin.setText(AppPrefs.getStringPref("AppPassword",this@MobilePostpaidActivity).toString())
        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        dialog.tvConfirmPin.setOnClickListener {
            if (dialog.etPin.text.toString().isEmpty()) {
                dialog.etPin.requestFocus()
                dialog.etPin.setError("Please Enter Pin")
            } else {
                verifyPin(userModel.mobile, AppPrefs.getStringPref("AppPassword",this@MobilePostpaidActivity).toString())
                dialog.dismiss()
            }

        }

        dialog.show()
    }
}