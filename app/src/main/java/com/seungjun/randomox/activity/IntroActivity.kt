package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.NoticesInfo
import com.seungjun.randomox.network.data.UserInfo
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.view.NormalPopup
import com.wang.avi.AVLoadingIndicatorView

import java.text.ParseException
import java.text.SimpleDateFormat

import com.seungjun.randomox.network.data.HeaderInfo
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.view_normal_dialog.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import java.util.*

class IntroActivity : BaseActivity() {

    companion object {

        private val TAG = "IntroActivity"
    }

    private var textData = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)

        intro_title.alpha = 0f

        Handler().postDelayed({
            intro_title.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.fadein))
            intro_title.alpha = 1f

            textData = intent.getStringExtra("textData") ?: ""

            RetrofitClient.callGetNotices(object : Observer<NoticesInfo> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(resultData: NoticesInfo) {
                    if (resultData.reqCode == 0 && !TextUtils.isEmpty(resultData.noti_text)) {

                        val transFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        try {
                            val serverDate = transFormat.parse(resultData.noti_date)
                            val localDate = transFormat.parse(preferenceUtils!!.notiDate)

                            // 서버 공지 추가 일이 로컬에 저장된 일 수 보다
                            // 더 나중인 경우
                            // 다시 보지 않기해도 노출시킨다
                            if (serverDate.compareTo(localDate) > 0) {
                                preferenceUtils!!.isNotiNoShow = false
                            }

                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }



                        if (preferenceUtils!!.isNotiNoShow) {
                            // 로그인 되있는 상태
                            // 자동로그인 요청
                            if (preferenceUtils!!.isLoginSuccess) {
                                callLogin()

                            } else {

                                moveMain()
                            }

                        } else {
                            val notiPoup = NormalPopup(this@IntroActivity).apply {
                                setPopupText(resultData.noti_text)
                                setPopupTitle("공지사항")
                            }

                            // 서버점검이나 강제업뎃 같이
                            // 무조건 무언가를 해야하는 경우
                            if (resultData.noti_yn == "y") {

                                notiPoup.setCancelVisible(View.GONE)
                                notiPoup.setOKClick (View.OnClickListener{
                                    // 업데이트라는 문구가 들어간 경우는 강제 업뎃으로 판단
                                    if (resultData.noti_text.contains("업데이트")) {


                                    }

                                    notiPoup.dismiss()
                                    finish()
                                })
                            } else {

                                notiPoup.setCancelVisible(View.VISIBLE)
                                notiPoup.setCancelText("그만 볼래요")
                                notiPoup.setCancelClick(View.OnClickListener {
                                    notiPoup.dismiss()

                                    preferenceUtils!!.isNotiNoShow = true

                                    // 로그인 되있는 상태
                                    // 자동로그인 요청
                                    if (preferenceUtils!!.isLoginSuccess) {
                                        callLogin()

                                    } else {

                                        moveMain()
                                    }
                                })
                                notiPoup.setOKClick(View.OnClickListener {
                                    notiPoup.dismiss()

                                    // 로그인 되있는 상태
                                    // 자동로그인 요청
                                    if (preferenceUtils!!.isLoginSuccess) {
                                        callLogin()

                                    } else {

                                        moveMain()
                                    }
                                })

                            }
                            notiPoup.show()

                            preferenceUtils!!.notiDate = resultData.noti_date
                        }

                    }

                }

                override fun onError(t: Throwable) {

                    // 로그인 되있는 상태
                    // 자동로그인 요청
                    if (preferenceUtils!!.isLoginSuccess) {
                        callLogin()

                    } else {

                        moveMain()
                    }
                }

            })

        }, 700)

    }


    /**
     * 로그인 요청 함수
     */
    fun callLogin() {
        RetrofitClient.callPostLogin(object : RetrofitApiCallback<UserInfo> {
            override fun onError(t: Throwable) {
                setLogOut()
            }

            override fun onSuccess(code: Int, userInfo: UserInfo) {

                if (userInfo.reqCode == 0) {
                    preferenceUtils?.run {
                        isLoginSuccess = true
                        userSindex = userInfo.user_sIndex
                        userKey = userInfo.user_key
                        userRank = userInfo.rank
                    }

                    updateFCM()

                } else {
                    setLogOut()
                }


            }

            override fun onFailed(code: Int) {
                setLogOut()
            }
        }, preferenceUtils!!.userId!!, preferenceUtils!!.userPw!!)
    }


    fun updateFCM() {

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        D.error(TAG, "getInstanceId failed", task.exception as Throwable)

                        moveMain()

                        return@OnCompleteListener
                    }

                    // 보내기전에 비교해보기
                    if (preferenceUtils!!.userFcmKey != task.result!!.token) {
                        preferenceUtils!!.userFcmKey = task.result!!.token
                    }

                    RetrofitClient.callPostFcmUpdate(object : RetrofitApiCallback<HeaderInfo> {
                        override fun onError(t: Throwable) {
                            moveMain()
                        }

                        override fun onSuccess(code: Int, resultData: HeaderInfo) {

                            moveMain()

                        }

                        override fun onFailed(code: Int) {
                            moveMain()
                        }
                    }, preferenceUtils!!.userKey!!, preferenceUtils!!.userFcmKey!!)
                })

    }


    /**
     * 로그아웃 함수
     */
    fun setLogOut() {

        longToast(resources.getString(R.string.auto_login_failed))

        preferenceUtils?.run {
            userPw = ""
            userId = ""
            userScore = 0
            userSindex = 1
            userFcmKey = ""
            userKey = ""
            userRank = -1
            isLoginSuccess = false
        }

        moveMain()
    }


    fun moveMain() {


        Handler().postDelayed(Runnable {
            if (this@IntroActivity.isFinishing)
                return@Runnable

            intro_progress.apply {
                startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.fadeout))
                alpha = 0f
            }

            val intent = Intent(this@IntroActivity, MainActivity::class.java)

            if (!TextUtils.isEmpty(textData))
                intent.putExtra("textData", textData)

            startActivity(intent)
            overridePendingTransition(0, 0)

            finish()
            overridePendingTransition(0, 0)
        }, 1500)

    }

}
