package com.seungjun.randomox.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.UserInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.utils.PreferenceUtils
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_login_dialog.*
import org.jetbrains.anko.toast

class LoginPopup(context: Context, private val callBack: LoginCallBack) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    var mContext: Context? = null
    var disposable = CompositeDisposable()

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


        RetrofitClient.callPostLogin(object : Observer<UserInfo> {
            override fun onError(t: Throwable) {
                failedLogin(context.resources.getString(R.string.error_network_unkonw))
            }

            override fun onComplete() {
                updateFCM()
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(resultData: UserInfo) {

                if(resultData.reqCode == 0){
                    PreferenceUtils.getInstance(context).run {
                        userSindex = resultData.user_sIndex
                        userScore = resultData.user_point
                        userId = nickname
                        userPw = CommonUtils.getAES256(context, password)
                        userKey = resultData.user_key
                        userRank = resultData.rank
                        isLoginSuccess = true
                    }
                }else{
                    failedLogin(resultData.reqMsg)
                }

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
        disposable.clear()
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

                    RetrofitClient.callPostFcmUpdate(object : Observer<HeaderInfo> {
                        override fun onError(t: Throwable) {
                            failedLogin(context.resources.getString(R.string.error_network_unkonw))
                        }

                        override fun onComplete() {
                            mContext!!.toast("로그인 성공! 앞으로 자동 로그인이 됩니다.")
                            callBack.onLogin(true)
                            this@LoginPopup.dismiss()
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable.add(d)
                        }

                        override fun onNext(t: HeaderInfo) {

                        }

                    }, PreferenceUtils.getInstance(context).userKey!!, PreferenceUtils.getInstance(context).userFcmKey!!)
                })

    }
}