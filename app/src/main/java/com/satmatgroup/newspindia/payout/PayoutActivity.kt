package com.satmatgroup.newspindia.payout

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.satmatgroup.newspindia.NewMainActivity
import com.satmatgroup.newspindia.R
import com.satmatgroup.newspindia.model.UserModel
import com.satmatgroup.newspindia.network_calls.AppApiCalls
import com.satmatgroup.newspindia.utils.*
import kotlinx.android.synthetic.main.activity_payout.*
import kotlinx.android.synthetic.main.activity_payout.view.*
import kotlinx.android.synthetic.main.layout_dialog_changbank.*
import kotlinx.android.synthetic.main.layout_list_bottomsheet_banklist.view.*
import kotlinx.android.synthetic.main.layout_list_bottomsheet_users.view.*
import kotlinx.android.synthetic.main.layout_list_bottomsheet_users.view.etSearchMobName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PayoutActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    UserPayoutBankAdapter.ListAdapterListener {
    private val SEND_AEPS_PAYOUT: String = "SEND_AEPS_PAYOUT"
    private val GET_ACCOUNT_DETAILS: String = "GET_ACCOUNT_DETAILS"
    private val GET_AEPS_CHARGE: String = "GET_AEPS_CHARGE"
    private val USER_PAYOUT_BANK: String = "USER_PAYOUT_BANK"

    lateinit var userPayoutBankAdapter: UserPayoutBankAdapter
    var userPayoutBankModelArrayList = ArrayList<UserPayoutBankModel>()
    var bottomSheetDialogUsers: BottomSheetDialog? = null

    lateinit var userModel: UserModel
    var charge = ""
    var payout_bank_id = ""
    lateinit var dialog: Dialog
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payout)
        custToolbar.ivBackBtn.setOnClickListener {
            onBackPressed()
        }

        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)
        getAccountDetails(userModel.cus_id)
       // userPayoutBank(userModel.cus_id)
        callServiceGetAccountDetails(userModel.cus_id)


        tvChangeBank.setOnClickListener {
           ShowBottomSheetBankList()
        }

        ll_payOutToBAnk.visibility = GONE

        rbPayoutToBank.setOnClickListener {

            rbPayoutToBank.isChecked = true
            if (rbPayoutToBank.isChecked) {
                ll_payOutToBAnk.visibility = VISIBLE
                type = "payoutToBank"
                rbPayoutToWallet.setChecked(false)
            }
        }

        rbPayoutToWallet.setOnClickListener {
            rbPayoutToWallet.isChecked = true
            ll_payOutToBAnk.visibility = GONE

            if (rbPayoutToWallet.isChecked) {
                type = "payoutToWallet"

                rbPayoutToBank.setChecked(false)

            }
        }

        btnSendMoneyAeps.setOnClickListener {
            if (type.isNullOrEmpty()) {
                toast("Please Select Transaction Type")
            } else if (type.equals("payoutToBank")) {
                if (etAepsUserName.text.toString().isEmpty()) {
                    toast("Please Reload the page")
                } else if (etAepsUserBank.text.toString().isEmpty()) {
                    toast("Please Reload the page")
                } else if (etAepsAccntNo.text.toString().isEmpty()) {
                    toast("Please Reload the page")
                } else if (etAepsBankIfsc.text.toString().isEmpty()) {
                    toast("Please Reload the page")
                } else if (etAmountPay.text.toString().isEmpty() || etAmountPay.text.toString()
                        .toInt() <= 0
                ) {
                    etAmountPay.requestFocus()
                    etAmountPay.error = "Invalid Amount"

                } else {

                    getCharge(userModel.cus_id, etAmountPay.text.toString())

                }

            } else {
                if (etAmountPay.text.toString().toInt() <= 0) {
                    etAmountPay.requestFocus()
                    etAmountPay.error = "Invalid Amount"
                } else {

                    showConfirmDialog(type, "0")

                }
            }
        }
    }

    private fun getCharge(cus_id: String, amount: String) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, GET_AEPS_CHARGE, this)
            mAPIcall.getAepsCharge(cus_id, amount)
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userPayoutBank(cus_id: String) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, USER_PAYOUT_BANK, this)
            mAPIcall.userPayoutBank(cus_id)
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }


    private fun aepsPayout(
        cus_id: String, bank_name: String, account_number: String,
        ifsc_code: String, account_holder_name: String, amount: String,
        charge: String, type: String, payout_bank_id: String
    ) {

        progress_bar.visibility = View.VISIBLE




        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, SEND_AEPS_PAYOUT, this)
            mAPIcall.aepsPayout(
                cus_id, bank_name, account_number,
                ifsc_code, account_holder_name, amount,
                charge, type,payout_bank_id
            )
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ShowBottomSheetBankList() {
        val view: View = layoutInflater.inflate(R.layout.layout_list_bottomsheet_banklist, null)
        view.etSearchMobName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {

                //filter(s.toString());

            }
        })
        view.rvBankList.apply {
            layoutManager = LinearLayoutManager(this@PayoutActivity)
            userPayoutBankAdapter = UserPayoutBankAdapter(
                context, userPayoutBankModelArrayList, this@PayoutActivity
            )
            view.rvBankList.adapter = userPayoutBankAdapter
        }

        bottomSheetDialogUsers = BottomSheetDialog(this)
        bottomSheetDialogUsers!!.setContentView(view)
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialogUsers!!.show()
    }


    private fun getAccountDetails(
        cus_id: String
    ) {
        progress_bar.visibility = View.VISIBLE
        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, GET_ACCOUNT_DETAILS, this)
            mAPIcall.aepsPayountAccountDetails(cus_id)
        } else {
            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(SEND_AEPS_PAYOUT)) {
            Log.e("SEND_AEPS_PAYOUT", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
                val response = jsonObject.getString("message")
                showSuccessRechargeDialog(response, etAmountPay.text.toString(), charge)
                //walletBalance(cus_id);
            } else {
                val response = jsonObject.getString("message")
                showSuccessRechargeDialog(response, etAmountPay.text.toString(), charge)
                progress_bar.visibility = View.GONE

            }
        }

        if (flag.equals(GET_ACCOUNT_DETAILS)) {
            Log.e("GET_ACCOUNT_DETAILS", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
                // charge = jsonObject.getString("charge")
                val response = jsonObject.getJSONArray("result")

                for (i in 0 until response.length()) {
                    val notifyObjJson = response.getJSONObject(i)
                    val cus_id = notifyObjJson.getString("cus_id")
                    Log.e("cus_id ", cus_id)
                    val aepsmodel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            PayoutModel::class.java
                        )

                    etAepsUserName.setText(aepsmodel.aeps_bankAccountName)
                    etAepsUserBank.setText(aepsmodel.bankName)
                    etAepsAccntNo.setText(aepsmodel.aeps_AccountNumber)
                    etAepsBankIfsc.setText(aepsmodel.aeps_bankIfscCode)

                }
                //walletBalance(cus_id);
            } else {
                val response = jsonObject.getString("message")
                toast(response)

                progress_bar.visibility = View.GONE

            }

        }

        if (flag.equals(USER_PAYOUT_BANK)) {
            Log.e("USER_PAYOUT_BANK", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = View.GONE
                // charge = jsonObject.getString("charge")
                val response = jsonObject.getJSONArray("data")

                for (i in 0 until response.length()) {
                    val notifyObjJson = response.getJSONObject(i)
                    val bankListModel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            UserPayoutBankModel::class.java
                        )
                    userPayoutBankModelArrayList.add(bankListModel)

                }
                //walletBalance(cus_id);
            } else {
                val response = jsonObject.getString("message")
                toast(response)

                progress_bar.visibility = View.GONE

            }

        }

        if (flag.equals(GET_AEPS_CHARGE)) {
            Log.e("GET_AEPS_CHARGE", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                progress_bar.visibility = GONE

                val cast = jsonObject.getString("result")

                charge = cast
                showConfirmDialog(type, charge)


                //walletBalance(cus_id);
            } else {

                val response = jsonObject.getString("result")
                Toast.makeText(this, "" + response, Toast.LENGTH_SHORT).show()

            }
        }

    }


    private fun showSuccessRechargeDialog(response: String, amount: String, charge: String) {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Request Status")


        var message = if (type.equals("payoutToBank")) {
            "Amount : ₹$amount \nCharge :₹$charge \nStatus :$response"
        } else {
            "Amount : ₹$amount \nStatus :$response"
        }
        builder1.setMessage(
            message
        )
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->


            dialog.cancel()
            val intent = Intent(this, PayoutHistoryActivity::class.java)
            startActivity(intent)
            finish()
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

    private fun showConfirmDialog(type: String, charge: String) {
        val builder1 =
            AlertDialog.Builder(this)
        builder1.setTitle("Confirm")

        var message = if (type.equals("payoutToBank")) {
            "Amount : ₹${etAmountPay.text.toString()} \nCharge :₹${charge}"
        } else {
            "Amount : ₹${etAmountPay.text.toString()}"
        }



        builder1.setMessage(
            message
        )
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->

            if (type.equals("payoutToBank")) {
                aepsPayout(
                    userModel.cus_id, etAepsUserBank.text.toString(),
                    etAepsAccntNo.text.toString(), etAepsBankIfsc.text.toString(),
                    etAepsUserName.text.toString(), etAmountPay.text.toString(),
                    charge, type,payout_bank_id
                )
            } else {
                aepsPayout(
                    userModel.cus_id, "",
                    "", "",
                    "", etAmountPay.text.toString(),
                    "0", type, ""
                )
            }


            dialog.cancel()

        }
        builder1.setNegativeButton(
            "Cancel"
        ) { dialog, id ->

            dialog.cancel()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    fun chanegBankDialog() {
        dialog = Dialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_changbank)

        dialog.ivCancelDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        dialog.tvContactNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + tvContactNumber.text.toString())
            startActivity(intent)
        }

        dialog.show()
    }

    override fun onClickAtOKButton(mobileRechargeModal: UserPayoutBankModel?) {
        if(mobileRechargeModal!=null) {
            payout_bank_id = mobileRechargeModal.getBeneid()!!
            etAepsAccntNo.setText(mobileRechargeModal.getAccount())
            etAepsBankIfsc.setText(mobileRechargeModal.getIfsc())
            etAepsUserBank.setText(mobileRechargeModal.getBankname())
            etAepsUserName.setText(mobileRechargeModal.getName())
            etConfirmAccount.setText(mobileRechargeModal.getAccount())
        }
        bottomSheetDialogUsers?.dismiss()
    }


    private fun callServiceGetAccountDetails(cusId: String) {
        progress_bar.visibility = VISIBLE
        System.setProperty("http.keepAlive", "false")
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                //Request request = chain.request().newBuilder().addHeader("parameter", "value").build();
                builder.header("Content-Type", "application/x-www-form-urlencoded")
                val request = builder.method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = httpClient.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(MainIAPI.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        //creating the retrofit api service
        val apiService = retrofit.create(MainIAPI::class.java)

        val rtid= createPartFromString(cusId)
        val callfunctn = createPartFromString("accountlist")


        //Call<ScannerResponse> call = apiService.saveScan(orderId1,vpa1,name1,amount1,mon_no1,member_id1,password1);
        val call = apiService.callUserPayoutAccountList(rtid, callfunctn)


        //making the call to generate checksum
        call.enqueue(object : Callback<BaseUserPayoutBankModel> {
            override fun onResponse(
                call: Call<BaseUserPayoutBankModel>,
                response: Response<BaseUserPayoutBankModel>
            ) {
                progress_bar.visibility = GONE
                if (response.body()!!.getStatus() == true) {
                    progress_bar.visibility = GONE

                    userPayoutBankModelArrayList= response.body()!!.getData()!!
                } else {
                    Toast.makeText(
                        this@PayoutActivity,
                        response.body()!!.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@PayoutActivity, NewMainActivity::class.java)
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)
                }


                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
            }

            override fun onFailure(call: Call<BaseUserPayoutBankModel>, t: Throwable) {
                progress_bar.visibility = GONE
                // callServiceFalse(mobileNo);
                Toast.makeText(this@PayoutActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }

}