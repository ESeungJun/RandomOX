package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.Toast
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.PreferenceUtils
import kotlinx.android.synthetic.main.view_join_dialog.*

class JoinPopup(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_join_dialog)

        btn_join.setOnClickListener {
            clickJoin()
        }

        mContext = context
    }


    fun clickJoin() {

        val nickname = input_nickname.text.toString()
        val password = input_pw.text.toString()

        if (TextUtils.isEmpty(nickname)) {
            error_text.apply {
                visibility = View.VISIBLE
                text = context.resources.getString(R.string.error_empty_name)
            }
            return
        }


        if (!CommonUtils.isValidName(nickname) || nickname.length < 2 || nickname.length > 12) {
            error_text.apply {
                visibility = View.VISIBLE
                text = context.resources.getString(R.string.error_nonok_name)
            }
            return
        }


        if (TextUtils.isEmpty(password)) {
            error_text.apply {
                visibility = View.VISIBLE
                text = context.resources.getString(R.string.error_empty_pw)
            }
            return
        }


        if (!CommonUtils.isValidPw(password)) {
            error_text.apply {
                visibility = View.VISIBLE
                text = context.resources.getString(R.string.error_nonok_pw)
            }
            return
        }

        input_nickname.apply {
            isEnabled = false
            isFocusable = false
        }

        input_pw.apply {
            isEnabled = false
            isFocusable = false
        }

        btn_join.visibility = View.GONE
        join_progress.visibility = View.VISIBLE

        this.setCancelable(false)


        RetrofitClient.callPostJoin(object : RetrofitApiCallback<HeaderInfo> {
            override fun onError(t: Throwable) {

                failedJoin(context.resources.getString(R.string.error_network_unkonw))
            }

            override fun onSuccess(code: Int, resultData: HeaderInfo) {

                if (resultData.reqCode == 0) {
                    Toast.makeText(context, "가입 성공! 로그인 해주세요!", Toast.LENGTH_SHORT).show()

                    this@JoinPopup.dismiss()
                } else {
                    failedJoin(resultData.reqMsg)
                }
            }

            override fun onFailed(code: Int) {

                failedJoin(context.resources.getString(R.string.error_network_unkonw))

            }
        }, nickname, CommonUtils.getAES256(context, password), PreferenceUtils.getInstance(context).userFcmKey!!)
    }


    fun failedJoin(msg: String) {

        input_nickname.apply{
            isEnabled = true
            isFocusableInTouchMode = true
            isFocusable = true
        }

        input_pw.apply{
            isEnabled = true
            isFocusableInTouchMode = true
            isFocusable = true
        }

        btn_join.visibility = View.VISIBLE
        join_progress.visibility = View.GONE
        this@JoinPopup.setCancelable(true)

        error_text.apply {
            visibility = View.VISIBLE
            text = msg
        }
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
