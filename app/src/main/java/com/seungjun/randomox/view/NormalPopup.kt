package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.TextView

import com.seungjun.randomox.R

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.view_normal_dialog.*

class NormalPopup(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_normal_dialog)

        mContext = context

        this.setCancelable(false)
    }


    fun setPopupText(text: String) {
        popup_text.text = text
    }


    fun setPopupTitle(title: String) {
        popup_title.text = title
    }

    fun setOKClick(listener: View.OnClickListener) {
        btn_ok.setOnClickListener(listener)
    }

    fun setOkText(text: String) {
        btn_ok.text = text
    }

    fun setCancelClick(listener: View.OnClickListener) {
        btn_cancel.setOnClickListener(listener)
    }

    fun setCancelVisible(visible: Int) {
        btn_cancel.visibility = visible
    }

    fun setCancelText(text: String) {
        btn_cancel.text = text
    }

    override fun show() {

        if ((mContext as Activity).isFinishing)
            return

        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }
}
