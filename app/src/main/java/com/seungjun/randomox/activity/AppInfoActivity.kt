package com.seungjun.randomox.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.view.WebViewPopup
import kotlinx.android.synthetic.main.activity_app_info.*
import kotlinx.android.synthetic.main.view_top_bar.*
import org.jetbrains.anko.email
import org.jetbrains.anko.startActivity

class AppInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_info)

        top_title.text = "앱 정보"
        my_score.visibility = View.INVISIBLE

        top_back.setOnClickListener{
            finish()
        }

        info_opensource.setOnClickListener {
            WebViewPopup(this).apply {
                setPopupText("file:///android_asset/opensource.html")
                show()
            }
        }

        info_send_email.setOnClickListener {
            clickSendEmail()
        }

        info_send_email_quiz.setOnClickListener {
            clickSendEmailQuiz()
        }

        try {
            val i = packageManager.getPackageInfo(packageName, 0)
            info_version.text = String.format("App ver ${i.versionName}")

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

            info_version.visibility = View.INVISIBLE
        }

    }


    fun clickSendEmail() {
        email("6951004@gmail.com", "[문의 및 건의]", "문의나 건의사항을 보내주세요!\n문의 및 건의 내용 : ")
    }


    fun clickSendEmailQuiz() {
        email("6951004@gmail.com", "[문제 제의]", "여러분의 OX 퀴즈를 보내주세요! 채택시 여러분의 닉네임과 함께 문제가 출제됩니다!\n닉네임 : \n문제 내용 : \n답(o / x) : ")
    }


}
