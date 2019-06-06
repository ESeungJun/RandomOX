package com.seungjun.randomox.activity

import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.EmojiInputFilter
import kotlinx.android.synthetic.main.activity_sendmail.*
import kotlinx.android.synthetic.main.view_top_bar.*
import org.jetbrains.anko.toast

class SendMailActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sendmail)

        top_back.setOnClickListener {
            finish()
        }

        send_mail.setOnClickListener {
            val story = input_story!!.text.toString()

            if (TextUtils.isEmpty(story)) {
                toast("이야기를 적어주시면 접수가 가능합니다.")

            }else{
                netProgress.setProgressText("편지를 보내는 중")
                netProgress.show()

                RetrofitClient.callPostSendLetter(object : RetrofitApiCallback<HeaderInfo> {
                    override fun onError(t: Throwable) {

                        netProgress.dismiss()

                        CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_network_unkonw), false)
                    }

                    override fun onSuccess(code: Int, resultData: HeaderInfo) {

                        netProgress.dismiss()

                        if (resultData.reqCode != 6001)
                            CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_send_mail), false)
                        else
                            CommonUtils.showErrorPopup(this@SendMailActivity, resultData.reqMsg, true)
                    }

                    override fun onFailed(code: Int) {

                        netProgress.dismiss()

                        CommonUtils.showErrorPopup(this@SendMailActivity, resources.getString(R.string.error_network_unkonw), false)

                    }

                }, preferenceUtils!!.userKey!!, preferenceUtils!!.userId!!, story)
            }
        }

        top_title.text = "랜덤OX 상담소"
        my_score.visibility = View.GONE

        input_story.filters = arrayOf<InputFilter>(EmojiInputFilter(this))

    }

}
