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
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.OxContentInfo
import com.seungjun.randomox.network.data.UserInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.view.JoinPopup
import com.seungjun.randomox.view.LoginPopup
import com.seungjun.randomox.view.NormalPopup

import kotlinx.android.synthetic.main.activity_main.*

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

        isLogin = BaseActivity.preferenceUtils.isLoginSuccess

        if (isLogin) {
            main_login.text = "로그아웃"
            main_join.text = "탈퇴하기"
            main_rank.visibility = View.VISIBLE

            main_myInfoView.visibility = View.VISIBLE
            main_myInfoView.alpha = 0f

            main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
            main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
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

        isLogin = BaseActivity.preferenceUtils.isLoginSuccess

        if (isLogin) {
            main_login.text = "로그아웃"
            main_join.text = "탈퇴하기"
            main_rank.visibility = View.VISIBLE

            main_myInfoView.visibility = View.VISIBLE

            if (isPause) {

                isPause = false

                callMyInfo()

            } else {

                main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
                main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
            }


        } else {
            setLogOut()
        }
    }


    override fun onPause() {
        super.onPause()

        isPause = true
    }


    fun callMyInfo() {

        netProgress.show()
        networkClient.callMyInfo(object : RetrofitApiCallback<UserInfo> {
            override fun onError(t: Throwable) {

                netProgress.dismiss()

                Toast.makeText(this@MainActivity, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()

                main_myScore.text = BaseActivity.preferenceUtils.userId + "님의 점수 : " + BaseActivity.preferenceUtils.userScore + "점"
                main_myRank.text = "( " + BaseActivity.preferenceUtils.userRank + "위 )"
            }

            override fun onSuccess(code: Int, resultData: UserInfo) {
                netProgress.dismiss()

                val userInfo = resultData as UserInfo

                if (userInfo.reqCode == 0) {

                    BaseActivity.preferenceUtils.userRank = userInfo.rank
                    BaseActivity.preferenceUtils.userScore = userInfo.user_point

                    main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
                    main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
                } else {

                    Toast.makeText(this@MainActivity, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()

                    main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
                    main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
                }

            }

            override fun onFailed(code: Int) {

                netProgress.dismiss()

                Toast.makeText(this@MainActivity, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()

                main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
                main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
            }
        }, BaseActivity.preferenceUtils.userId)
    }

    fun clickLogin() {

        // 로그인 되있는 상태 -> 로그아웃이 눌림 (로그아웃 시키기)
        if (isLogin) {

            setLogOut()

            Toast.makeText(this, "로그아웃 되었어요.", Toast.LENGTH_SHORT).show()

        } else {
            val loginPopup = LoginPopup(this, this)
            loginPopup.show()
        }// 로그아웃 되어있는 상태 -> 로그인이 눌림 (로그인 팝업)

    }


    fun clickJoin() {

        // 로그인 되있는 상태 -> 탈퇴하기 눌림 (탈퇴 시키기)
        if (isLogin) {
            val exitPopup = NormalPopup(this)
            exitPopup.setPopupTitle("탈퇴하기")
            exitPopup.setPopupText("탈퇴 하시겠어요?\n탈퇴 시, 받았던 편지와 모든 데이터가 삭제되며 복구는 불가능해요.")
            exitPopup.setCancelVisible(View.VISIBLE)
            exitPopup.setCancelClick { exitPopup.dismiss() }
            exitPopup.setOKClick {
                exitPopup.dismiss()

                netProgress.setProgressText("탈퇴 요청 중")
                netProgress.show()

                networkClient.callPostDeleteInfo(object : RetrofitApiCallback<HeaderInfo> {
                    override fun onError(t: Throwable) {

                        netProgress.dismiss()
                        CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
                    }

                    override fun onSuccess(code: Int, resultData: HeaderInfo) {

                        netProgress.dismiss()

                        val result = resultData as HeaderInfo

                        if (result.reqCode == 0) {


                            Toast.makeText(this@MainActivity, "즐거웠어요! 또 놀러오세요 ! :)", Toast.LENGTH_SHORT).show()

                            setLogOut()

                        } else {
                            CommonUtils.showErrorPopup(this@MainActivity, result.reqMsg, false)
                        }


                    }

                    override fun onFailed(code: Int) {

                        netProgress.dismiss()
                        CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
                    }

                }, BaseActivity.preferenceUtils.userKey, BaseActivity.preferenceUtils.userId, BaseActivity.preferenceUtils.userPw)
            }

            exitPopup.show()

        } else {

            val normalPopup = NormalPopup(this)
            normalPopup.setCancelable(true)
            normalPopup.setCancelText("싫은데요?")
            normalPopup.setCancelVisible(View.VISIBLE)
            normalPopup.setCancelClick { normalPopup.dismiss() }
            normalPopup.setPopupTitle("가입 주의사항")
            normalPopup.setOkText("인정합니다")
            normalPopup.setPopupText(resources.getString(R.string.join_info))
            normalPopup.setOKClick {
                normalPopup.dismiss()

                val joinPopup = JoinPopup(this@MainActivity)
                joinPopup.show()
            }

            normalPopup.show()
        }
    }


    fun clickStart() {

        if (BaseActivity.preferenceUtils.isLoginSuccess) {
            callOxData()
        } else {
            Toast.makeText(this, "로그인을 해주세요.", Toast.LENGTH_SHORT).show()
        }

    }

    fun clickRank() {
        startActivity(Intent(this, RankActivity::class.java))
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
            Toast.makeText(this, "로그인을 하면 이용 가능합니다.", Toast.LENGTH_SHORT).show()
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

        networkClient.callPostGetOX(object : RetrofitApiCallback<OxContentInfo> {
            override fun onError(t: Throwable) {

                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
            }

            override fun onSuccess(code: Int, resultData: OxContentInfo?) {

                netProgress.dismiss()

                if (resultData != null) {
                    val oxContentInfo = resultData as OxContentInfo?

                    if (oxContentInfo!!.reqCode == 0) {

                        //for test
                        val intent = Intent(this@MainActivity, OXActivity::class.java)
                        intent.putParcelableArrayListExtra("oxList", oxContentInfo.oxList)
                        startActivity(intent)

                    } else {

                        CommonUtils.showErrorPopup(this@MainActivity, oxContentInfo.reqMsg, false)
                    }
                }
            }

            override fun onFailed(code: Int) {

                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@MainActivity, resources.getString(R.string.error_network_unkonw), false)
            }
        }, BaseActivity.preferenceUtils.userSindex)
    }

    /**
     * 로그아웃 함수
     */
    fun setLogOut() {

        // 데이터 초기화
        BaseActivity.preferenceUtils.userPw = ""
        BaseActivity.preferenceUtils.userId = ""
        BaseActivity.preferenceUtils.userScore = 0
        BaseActivity.preferenceUtils.userSindex = 1
        BaseActivity.preferenceUtils.userFcmKey = ""
        BaseActivity.preferenceUtils.userKey = ""
        BaseActivity.preferenceUtils.userRank = -1
        BaseActivity.preferenceUtils.isLoginSuccess = false

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

            main_myScore.text = String.format("${BaseActivity.preferenceUtils.userId} 님의 점수 : ${BaseActivity.preferenceUtils.userScore} 점")
            main_myRank.text = String.format("( ${BaseActivity.preferenceUtils.userRank} 위 )")
        } else {
            setLogOut()
        }

    }
}
