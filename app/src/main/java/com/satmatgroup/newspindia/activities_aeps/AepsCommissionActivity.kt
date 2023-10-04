package com.satmatgroup.newspindia.activities_aeps

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.satmatgroup.newspindia.R
import com.satmatgroup.newspindia.activities_aeps.aepshistory.AepsHistoryModel
import com.satmatgroup.newspindia.model.UserModel

import com.satmatgroup.newspindia.network_calls.AppApiCalls
import com.satmatgroup.newspindia.utils.AppCommonMethods
import com.satmatgroup.newspindia.utils.AppConstants
import com.satmatgroup.newspindia.utils.AppPrefs
import kotlinx.android.synthetic.main.activity_aeps_commission.*
import kotlinx.android.synthetic.main.activity_aeps_commission.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AepsCommissionActivity : AppCompatActivity(), AppApiCalls.OnAPICallCompleteListener,
    PopupMenu.OnMenuItemClickListener {
    lateinit var aepsHistoryAdapter: AepsCommissionHistoryAdapter
    var aepsHistoryModelArrayList = ArrayList<AepsHistoryModel>()
    private val AEPSHISTORY_REPORT: String = "AEPSHISTORY_REPORT"
    lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.black, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_aeps_commission)

        //Toolbar
        custToolbar.ivBackBtn.setOnClickListener { onBackPressed() }


        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy")
        tvSelectDate.text = dateInString
        tvSelectDate.text = dateInString
        aepsCommissionHistory(
            userModel.rtid, AppCommonMethods.convertDateFormat(
                "dd/MM/yyyy",
                "yyyy-MM-dd", tvSelectDate.text.toString()
            ).toString(),
            AppPrefs.getStringPref("deviceId", this).toString(),
            AppPrefs.getStringPref("deviceName", this).toString(),
            "",
            "",
            userModel.mobile, userModel.logintype
        )
        custToolbar.ivMore.setOnClickListener {
            val popup = PopupMenu(this@AepsCommissionActivity, ivMore)
            popup.setOnMenuItemClickListener(this@AepsCommissionActivity)
            popup.inflate(R.menu.menu_main)
            popup.show()
        }
        rvDisputeHistory.apply {

            layoutManager = LinearLayoutManager(this@AepsCommissionActivity)
            aepsHistoryAdapter = AepsCommissionHistoryAdapter(
                context, aepsHistoryModelArrayList
            )
            rvDisputeHistory.adapter = aepsHistoryAdapter
        }

        rvSelectFromDate.setOnClickListener {
            getDatePicker()
        }
    }

    private fun aepsCommissionHistory(
        rtid: String, date: String, deviceId: String, deviceName: String, pin: String,
        pass: String, mobile: String, logintype: String
    ) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, AEPSHISTORY_REPORT, this)
            mAPIcall.aepsCommissionHistory(
                rtid,
                date,
                deviceId,
                deviceName,
                pin,
                pass,
                mobile,
                logintype
            )
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(AEPSHISTORY_REPORT)) {
            aepsHistoryModelArrayList.clear()
            Log.e("AEPSHISTORY_REPORT", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {

                progress_bar.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val aeps_id = notifyObjJson.getString("aeps_id")
                    Log.e("aeps_id ", aeps_id)
                    val aepsmodel = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            AepsHistoryModel::class.java
                        )


                    aepsHistoryModelArrayList.add(aepsmodel)
                }

                rvDisputeHistory.adapter!!.notifyDataSetChanged()


            } else {
                progress_bar.visibility = View.INVISIBLE


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_refresh -> {
                aepsCommissionHistory(
                    userModel.rtid, AppCommonMethods.convertDateFormat(
                        "dd/MM/yyyy",
                        "yyyy-MM-dd", tvSelectDate.text.toString()
                    ).toString(),
                    AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    "",
                    "",
                    userModel.mobile, userModel.logintype
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getDatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var dpd =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                val date = "$mDay/$mmMonth/$mYear"
                tvSelectDate.text = date
                aepsCommissionHistory(
                    userModel.rtid, AppCommonMethods.convertDateFormat(
                        "dd/MM/yyyy",
                        "yyyy-MM-dd", tvSelectDate.text.toString()
                    ).toString(), AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    "",
                    "",
                    userModel.mobile, userModel.logintype
                )

            }, year, month, day)
        dpd.show()
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}