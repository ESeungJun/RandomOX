package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import com.seungjun.randomox.R
import kotlinx.android.synthetic.main.view_webview_dialog.*

class WebViewPopup(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_webview_dialog)

        btn_ok.setOnClickListener {
            dismiss()
        }
    }


    fun setPopupText(url: String) {
        popup_webview.loadUrl(url)
    }

    override fun show() {

        if ((context as Activity).isFinishing)
            return

        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }
}
