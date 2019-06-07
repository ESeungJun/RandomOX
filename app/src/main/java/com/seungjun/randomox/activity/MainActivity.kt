package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.OxContentInfo
import com.seungjun.randomox.network.data.UserInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.view.JoinPopup
import com.seungjun.randomox.view.LoginPopup
import com.seungjun.randomox.view.NormalPopup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), LoginPopup.LoginCallBack {

    private var isLogin = false

    private var textData = ""
    private var isPause = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        main_login.setOnClickListener {
            clickLogin()
        }

        main_join.setOnClickListener {
            clickJoin()
        }

        main_rank.setOnClickListener {
            clickRank()
        }

        hide_postbox.setOnClickListener{
            clickPostbox()
        }

        main_title.setOnClickListener {
            clickMainTitle()
        }

        main_start.setOnClickListener {
            clickStart()
        }

        main_app_info.setOnClickListener {
            clickAppInfo()
        }

        isLogin = preferenceUtils!!.isLoginSuccess

        if (isLogin) {
            main_login.text = "로그아웃"
            main_join.text = "탈퇴하기"
            main_rank.visibility = View.VISIBLE

            main_myInfoView.visibility = View.VISIBLE
            main_myInfoView.alpha = 0f

            main_myScore.text = String.format("${preferenceUtils!!.userId} 님의 점수 : ${preferenceUtils!!.userScore} 점")
            main_myRank.text = String.format("( ${preferenceUtils!!.userRank} 위 )")
        } else {
            main_login.text = "로그인"
            main_join.text = "가입하기"

            main_rank.visibility = View.GONE

            main_myInfoView.visibility = View.INVISIBLE
        }


        if (intent != null && intent.extras != null) {
            textData = intent.getStringExtra("textData")

            hide_view1.visibility = View.INVISIBLE
            hide_view2.visibility = View.INVISIBLE
            hide_view3.visibility = View.INVISIBLE

            hide_postbox.setImageDrawable(resources.getDrawable(R.drawable.mail))
        }

        main_start_view.alpha = 0f

        Handler().postDelayed({
            main_start_view.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.fadein))
            main_start_view.alpha = 1f

            if (isLogin) {
                main_myInfoView.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.fadein))
                main_myInfoView.alpha = 1f
            }
        }, 700)

    }


    override fun onResume() {
        super.onResume()

        isLogin = preferenceUtils!!.isLoginSuccess

        if (isLogin) {
            main_login.text = "로그아웃"
            main_join.text = "탈퇴하기"
            main_rank.visibility = View.VISIBLE

            main_myInfoView.visibility = View.VISIBLE

            if (isPause) {

                isPause = false

                callMyInfo()

            } else {

                main_myScore.text = String.format("${preferenceUtils!!.userId} 님의 점수 : ${preferenceUtils!!.userScore} 점")
                main_myRank.text = String.format("( ${preferenceUtils!!.userRank} 위 )")
            }


        } else {
            setLogOut()
        }
    }


    override fun onPause() {
        super.onPause()

        isPause = true
    }

    override fun onStop() {
        super.onStop()
        baseDisPosable.clear()
    }

    fun callMyInfo() {

        netProgress.show()
        RetrofitClient.callMyInfo(object : Observer<UserInfo> {
            override fun onError(t: Throwable) {

                netProgress.dismiss()

                toast("내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요")

                main_myScore.text = String.format("${preferenceUtils!!.userId} 님의 점수 : ${preferenceUtils!!.userScore} 점")
                main_myRank.text = String.format("( ${preferenceUtils!!.userRank} 위 )")
            }

            override fun onComplete() {
                netProgress.dismiss()
            }

            override fun onSubscribe(d: Disposable) {
                baseDisPosable.add(d)
            }

            override fun onNext(userInfo: UserInfo) {

                preferenceUtils?.run {
                    userRank = userInfo.rank
                    userScore = userInfo.user_point
                }

                main_myScore.text = String.format("${preferenceUtils!!.userId} 님의 점수 : ${preferenceUtils!!.userScore} 점")
                main_myRank.text = String.format("( ${preferenceUtils!!.userRank} 위 )")
            }
        }, preferenceUtils!!.userId!!)
    }

    fun clickLogin() {

        // 로그인 되있는 상태 -> 로그아웃이 눌림 (로그아웃 시키기)
        if (isLogin) {

            setLogOut()

            toast("로그아웃 되었어요.")

        } else {
            val loginPopup = LoginPopup(this, this)
            loginPopup.show()
        }// 로그아웃 되어있는 상태 -> 로그인이 눌림 (로그인 팝업)

    }


    fun clickJoin() {

        // 로그인 되있는 상태 -> 탈퇴하기 눌림 (탈퇴 시키기)
        if (isLogin) {
            NormalPopup(this).apply {
                setPopupTitle("탈퇴하기")
                setPopupText("탈퇴 하시겠어요?\n탈퇴 시, 받았던 편지와 모든 데이터가 삭제되며 복구는 불가능해요.")
                setCancelVisible(View.VISIBLE)
                setCancelClick(View.OnClickListener { dismiss() })
                setOKClick(View.OnClickListener{
                    dismiss()

                    netProgress.setProgressText("탈퇴 요청 중")
                    netProgress.show()

                    RetrofitClient.callPostDeleteInfo(object : Observer<HeaderInfo> {
                        override fun onError(t: Throwable) {

                            netProgress.dismiss()
                            CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
                        }

                        override fun onComplete() {
                            netProgress.dismiss()

                            toast("즐거웠어요! 또 놀러오세요 ! :)")

                            setLogOut()
                        }

                        override fun onSubscribe(d: Disposable) {
                            baseDisPosable.add(d)
                        }

                        override fun onNext(t: HeaderInfo) {

                        }

                    }, preferenceUtils!!.userKey!!, preferenceUtils!!.userId!!, preferenceUtils!!.userPw!!)
                })

                show()
            }
            

        } else {

            NormalPopup(this).apply {
                setCancelable(true)
                setCancelText("싫은데요?")
                setCancelVisible(View.VISIBLE)
                setCancelClick (View.OnClickListener { dismiss() })
                setPopupTitle("가입 주의사항")
                setOkText("인정합니다")
                setPopupText(resources.getString(R.string.join_info))
                setOKClick(View.OnClickListener {
                    dismiss()

                    val joinPopup = JoinPopup(this@MainActivity)
                    joinPopup.show()
                })
                show()
            }
            
        }
    }


    fun clickStart() {

        if (preferenceUtils!!.isLoginSuccess) {
            callOxData()
        } else {
            toast("로그인을 해주세요.")
        }

    }

    fun clickRank() {
        startActivity<RankActivity>()
    }


    fun clickMainTitle() {

        if (hide_view3.visibility == View.VISIBLE) {
            hide_view3.visibility = View.INVISIBLE

            return
        }

        if (hide_view2.visibility == View.VISIBLE) {
            hide_view2.visibility = View.INVISIBLE

            return
        }

        if (hide_view1.visibility == View.VISIBLE) {
            hide_view1.visibility = View.INVISIBLE

            return
        }

    }

    fun clickPostbox() {

        if (hide_view1.visibility == View.VISIBLE ||
                hide_view2.visibility == View.VISIBLE ||
                hide_view3.visibility == View.VISIBLE)
            return

        if (!isLogin) {
            toast("로그인을 하면 이용 가능합니다.")
            return
        }

        var intent: Intent? = null

        if (!TextUtils.isEmpty(textData)) {
            intent = Intent(this, ReqMailActivity::class.java)
            intent.putExtra("textData", textData)
        } else {
            intent = Intent(this, PostMailOXActivity::class.java)
        }

        startActivity(intent)
    }

    fun clickAppInfo() {
        startActivity(Intent(this, AppInfoActivity::class.java))
    }


    /**
     * 문제 요청 후 화면 진입하는 함수
     */
    fun callOxData() {
        netProgress.show()

        RetrofitClient.callPostGetOX(object : Observer<OxContentInfo> {

            override fun onComplete() {
            }


            override fun onSubscribe(d: Disposable) {
                baseDisPosable.add(d)
            }

            override fun onNext(resultData: OxContentInfo) {

                netProgress.dismiss()

                if (resultData.reqCode == 0) {

                    startActivity<OXActivity>("oxList" to resultData.oxList)

                } else {

                    CommonUtils.showErrorPopup(this@MainActivity, resultData.reqMsg, false)
                }
            }

            override fun onError(t: Throwable) {

                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
            }
        }, preferenceUtils!!.userSindex)
    }

    /**
     * 로그아웃 함수
     */
    fun setLogOut() {

        // 데이터 초기화
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

        isLogin = false

        main_login.text = "로그인"
        main_join.text = "가입하기"
        main_rank.visibility = View.GONE

        main_myInfoView.visibility = View.INVISIBLE
    }


    override fun onLogin(isLogin: Boolean) {

        this.isLogin = isLogin

        if (this.isLogin) {
            main_login.text = "로그아웃"
            main_join.text = "탈퇴하기"
            main_rank.visibility = View.VISIBLE

            main_myInfoView.visibility = View.VISIBLE

            main_myScore.text = String.format("${preferenceUtils!!.userId} 님의 점수 : ${preferenceUtils!!.userScore} 점")
            main_myRank.text = String.format("( ${preferenceUtils!!.userRank} 위 )")
        } else {
            setLogOut()
        }

    }
}
