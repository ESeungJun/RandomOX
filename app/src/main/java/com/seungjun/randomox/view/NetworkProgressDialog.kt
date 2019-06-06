package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import com.seungjun.randomox.R
import kotlinx.android.synthetic.main.network_progress_dialog.*


/**
 * 네트워크 요청시 노출되는 애니메이션 프로그래스바
 * Created by SeungJun on 2016-09-30.
 */

class NetworkProgressDialog(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.network_progress_dialog)

        this.setCancelable(false)

        mContext = context
    }

    fun setProgressText(text: String) {
        progress_text.text = text
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
