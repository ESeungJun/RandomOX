package com.seungjun.randomox.activity

import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.EmojiInputFilter

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class SendMailActivity : BaseActivity() {

    @BindView(R.id.top_title)
    internal var topTitle: TextView? = null

    @BindView(R.id.my_score)
    internal var myScore: TextView? = null

    @BindView(R.id.input_story)
    internal var inputStory: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sendmail)

        ButterKnife.bind(this)

        topTitle!!.text = "랜덤OX 상담소"
        myScore!!.visibility = View.GONE

        inputStory!!.filters = arrayOf<InputFilter>(EmojiInputFilter(this))

    }


    @OnClick(R.id.top_back)
    fun back() {
        finish()
    }

    @OnClick(R.id.send_mail)
    fun clickSendMail() {
        //서버에 보내는 로직

        val story = inputStory!!.text.toString()

        if (TextUtils.isEmpty(story)) {
            Toast.makeText(this, "이야기를 적어주시면 접수가 가능합니다.", Toast.LENGTH_SHORT).show()
            return
        }


        netProgress.setProgressText("편지를 보내는 중")
        netProgress.show()

        networkClient.callPostSendLetter(object : RetrofitApiCallback<HeaderInfo> {
            override fun onError(t: Throwable) {

                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_network_unkonw), false)
            }

            override fun onSuccess(code: Int, resultData: HeaderInfo) {

                netProgress.dismiss()

                val result = resultData as HeaderInfo

                if (result.reqCode != 6001)
                    CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_send_mail), false)
                else
                    CommonUtils.showErrorPopup(this@SendMailActivity, result.reqMsg, true)
            }

            override fun onFailed(code: Int) {

                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_network_unkonw), false)

            }

        }, BaseActivity.preferenceUtils.userKey, BaseActivity.preferenceUtils.userId, story)
    }
}
