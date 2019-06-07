package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import kotlinx.android.synthetic.main.activity_post_mail.*
import kotlinx.android.synthetic.main.view_top_bar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PostMailOXActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_mail)

        btn_o.setOnClickListener {
            startActivity<SendMailActivity>()
            finish()
        }

        my_score.setOnClickListener {
            startActivity<ReqMailActivity>()
            finish()
        }

        btn_x.setOnClickListener {
            toast("하고픈 이야기가 있다면 상담소를 찾아주세요 :)")
            finish()
        }

        top_back.setOnClickListener {
            finish()
        }

        my_score.text = "편지 목록"

        ox_content.alpha = 0f

        btn_o.alpha = 0f
        btn_x.alpha = 0f

        Handler().postDelayed({
            ox_content.startAnimation(AnimationUtils.loadAnimation(this@PostMailOXActivity, R.anim.fadein))
            ox_content.alpha = 1f
        }, 1000)

        Handler().postDelayed({
            btn_o.startAnimation(AnimationUtils.loadAnimation(this@PostMailOXActivity, R.anim.fadein))
            btn_x.startAnimation(AnimationUtils.loadAnimation(this@PostMailOXActivity, R.anim.fadein))

            btn_o.alpha = 1f
            btn_x.alpha = 1f
        }, 2000)
    }

}
