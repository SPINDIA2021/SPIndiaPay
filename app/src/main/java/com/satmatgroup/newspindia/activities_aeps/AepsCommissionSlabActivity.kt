package com.satmatgroup.newspindia.activities_aeps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.satmatgroup.newspindia.R
import com.satmatgroup.newspindia.model.UserModel
import com.satmatgroup.newspindia.network_calls.AppApiCalls
import com.satmatgroup.newspindia.utils.AppCommonMethods
import com.satmatgroup.newspindia.utils.AppConstants
import com.satmatgroup.newspindia.utils.AppPrefs
import kotlinx.android.synthetic.main.activity_commision_report.view.*
import kotlinx.android.synthetic.main.activity_commission_slab.*
import org.json.JSONObject

class AepsCommissionSlabActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener,
    AppApiCalls.OnAPICallCompleteListener {
    lateinit var aepsCommissionSlabAdapter: AepsCommisionSlabadapter
    var commisionSlabModelArrayList = ArrayList<AepsCommissionSlabModel>()
    private val COMMISION_SLAB: String = "COMMISION_SLAB"
    lateinit var userModel: UserModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aeps_commission_slab)

        //Toolbar
        custToolbar.ivBackBtn.setOnClickListener { onBackPressed() }

        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", this)
        userModel = gson.fromJson(json, UserModel::class.java)

        commisionApi(
            userModel.rtid,
            AppPrefs.getStringPref("deviceId", this).toString(),
            AppPrefs.getStringPref("deviceName", this).toString(),
            "",
            "",
            userModel.mobile, userModel.logintype


        )
        custToolbar.ivMore.setOnClickListener {
            val popup = PopupMenu(this@AepsCommissionSlabActivity, ivMore)
            popup.setOnMenuItemClickListener(this@AepsCommissionSlabActivity)
            popup.inflate(R.menu.menu_main)
            popup.show()
        }
        rvCommisionSlabReport.apply {

            layoutManager = LinearLayoutManager(this@AepsCommissionSlabActivity)
            aepsCommissionSlabAdapter = AepsCommisionSlabadapter(
                context, commisionSlabModelArrayList
            )
            rvCommisionSlabReport.adapter = aepsCommissionSlabAdapter
        }
    }

    private fun commisionApi(rtid: String, deviceId : String, deviceName : String,pin : String,
                             pass : String, mobile : String, logintype : String) {
        progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(this).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(this, COMMISION_SLAB, this)
            mAPIcall.aepscommisionSlab(rtid)
        } else {

            Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(COMMISION_SLAB)) {
            Log.e("COMMISION_SLAB", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            if (status.contains("true")) {

                progress_bar.visibility = View.INVISIBLE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val aeps_comm_id = notifyObjJson.getString("aeps_comm_id")
                    Log.e("aeps_comm_id ", aeps_comm_id)
                    val rechargeHistoryModal = Gson()
                        .fromJson(
                            notifyObjJson.toString(),
                            AepsCommissionSlabModel::class.java
                        )


                    commisionSlabModelArrayList.add(rechargeHistoryModal)
                }

                rvCommisionSlabReport.adapter!!.notifyDataSetChanged()


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
                commisionApi(userModel.rtid, AppPrefs.getStringPref("deviceId", this).toString(),
                    AppPrefs.getStringPref("deviceName", this).toString(),
                    "",
                    "",
                    userModel.mobile, userModel.logintype)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}