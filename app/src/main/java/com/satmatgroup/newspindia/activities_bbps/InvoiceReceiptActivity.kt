package com.satmatgroup.newspindia.activities_bbps

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.satmatgroup.newspindia.R
import kotlinx.android.synthetic.main.activity_invoice_receipt.*
import kotlinx.android.synthetic.main.activity_invoice_receipt.view.*

class InvoiceReceiptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setContentView(R.layout.activity_invoice_receipt)

        custToolbar.ivBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
}