package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import com.seungjun.randomox.R
import kotlinx.android.synthetic.main.view_letter_dialog.*

class LetterPopup(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_letter_dialog)

        btn_ok.setOnClickListener {
            dismiss()
        }

        mContext = context
    }


    fun setPopupText(text: String) {
        popup_text.text = text
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
