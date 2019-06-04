package com.seungjun.randomox.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.view.WebViewPopup

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_app_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_top_bar.*

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
        val it = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("6951004@gmail.com"))
            putExtra(Intent.EXTRA_TEXT, "문의나 건의사항을 보내주세요")
            type = "text/plain"
        }

        try {

            it.setPackage("com.google.android.gm")
            startActivity(it)

        } catch (e: Exception) {
            e.printStackTrace()
            startActivity(Intent.createChooser(it, "이메일을 보낼 앱을 선택해주세요."))
        }

    }


    fun clickSendEmailQuiz() {
        val it = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("6951004@gmail.com"))
            putExtra(Intent.EXTRA_TEXT, "여러분의 OX 퀴즈를 보내주세요!\n채택시 여러분의 닉네임과 함께 문제가 출제됩니다!")
            type = "text/plain"
        }

        try {

            it.setPackage("com.google.android.gm")
            startActivity(it)

        } catch (e: Exception) {
            e.printStackTrace()
            startActivity(Intent.createChooser(it, "이메일을 보낼 앱을 선택해주세요."))
        }

    }


}
