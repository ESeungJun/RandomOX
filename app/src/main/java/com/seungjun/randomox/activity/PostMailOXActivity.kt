package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_post_mail.*
import kotlinx.android.synthetic.main.view_top_bar.*

class PostMailOXActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_mail)

        btn_o.setOnClickListener {
            Intent(this, SendMailActivity::class.java).apply {
                startActivity(this)
            }

            finish()
        }

        my_score.setOnClickListener {
            Intent(this, SendMailActivity::class.java).apply {
                startActivity(this)
            }

            finish()
        }

        btn_x.setOnClickListener {

            Toast.makeText(this, "하고픈 이야기가 있다면 상담소를 찾아주세요 :)", Toast.LENGTH_SHORT).show()
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
