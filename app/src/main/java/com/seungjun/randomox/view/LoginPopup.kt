package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.seungjun.randomox.R
import com.seungjun.randomox.network.data.UserInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.utils.PreferenceUtils
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.wang.avi.AVLoadingIndicatorView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.seungjun.randomox.network.data.HeaderInfo
import kotlinx.android.synthetic.main.view_login_dialog.*

class LoginPopup(context: Context, private val callBack: LoginCallBack) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null

    interface LoginCallBack {
        fun onLogin(isLogin: Boolean)
    }

    companion object {

        private val TAG = "LoginPopup"
    }


    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_login_dialog)

        btn_login.setOnClickListener {
            clickLogin()
        }

        mContext = context
    }

    fun clickLogin() {

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

        input_nickname.apply{
            isEnabled = false
            isFocusable = false
        }

        input_pw.apply{
            isEnabled = false
            isFocusable = false
        }

        btn_login!!.visibility = View.GONE
        login_progress!!.visibility = View.VISIBLE
        this.setCancelable(false)


        RetrofitClient.callPostLogin(object : RetrofitApiCallback<UserInfo> {
            override fun onError(t: Throwable) {
                failedLogin(context.resources.getString(R.string.error_network_unkonw))
            }

            override fun onSuccess(code: Int, resultData: UserInfo) {

                if (resultData.reqCode == 0) {

                    PreferenceUtils.getInstance(context).apply {
                        userSindex = resultData.user_sIndex
                        userScore = resultData.user_point
                        userId = nickname
                        userPw = CommonUtils.getAES256(context, password)
                        userKey = resultData.user_key
                        userRank = resultData.rank
                        isLoginSuccess = true
                    }


                    updateFCM()

                } else {

                    failedLogin(resultData.reqMsg)
                }


            }

            override fun onFailed(code: Int) {

                failedLogin(context.resources.getString(R.string.error_network_unkonw))

            }
        }, nickname, CommonUtils.getAES256(context, password))
    }


    override fun show() {

        if ((mContext as Activity).isFinishing)
            return

        super.show()
    }

    fun failedLogin(msg: String) {

        input_nickname.apply {
            isEnabled = true
            isFocusableInTouchMode = true
            isFocusable = true
        }

        input_pw.apply {
            isEnabled = true
            isFocusableInTouchMode = true
            isFocusable = true
        }

        btn_login!!.visibility = View.VISIBLE
        login_progress!!.visibility = View.GONE
        this@LoginPopup.setCancelable(true)

        error_text.apply {
            visibility = View.VISIBLE
            text = msg
        }
    }


    override fun dismiss() {
        super.dismiss()
    }


    fun updateFCM() {

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        D.error(TAG, "getInstanceId failed", task.exception!!)

                        failedLogin(context.resources.getString(R.string.error_network_unkonw))

                        return@OnCompleteListener
                    }

                    // 보내기전에 비교해보기
                    if (PreferenceUtils.getInstance(context).userFcmKey != task.result!!.token) {
                        PreferenceUtils.getInstance(context).userFcmKey = task.result!!.token
                    }

                    RetrofitClient.callPostFcmUpdate(object : RetrofitApiCallback<HeaderInfo> {
                        override fun onError(t: Throwable) {
                            failedLogin(context.resources.getString(R.string.error_network_unkonw))
                        }

                        override fun onSuccess(code: Int, resultData: HeaderInfo) {

                            Toast.makeText(context, "로그인 성공! 앞으로 자동 로그인이 됩니다.", Toast.LENGTH_SHORT).show()
                            callBack.onLogin(true)
                            this@LoginPopup.dismiss()

                        }

                        override fun onFailed(code: Int) {
                            failedLogin(context.resources.getString(R.string.error_network_unkonw))
                        }
                    }, PreferenceUtils.getInstance(context).userKey!!, PreferenceUtils.getInstance(context).userFcmKey!!)
                })

    }
}
