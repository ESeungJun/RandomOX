package com.seungjun.randomox.activity

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.OxContentInfo
import com.seungjun.randomox.utils.CommonUtils
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.view.NormalPopup

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.seungjun.randomox.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_ox.*
import kotlinx.android.synthetic.main.view_top_bar.*

class OXActivity : BaseActivity() {

    companion object {

        private val TAG = "OXActivity"
    }

    private var answerValue = ""

    private var oxList = ArrayList<OxContentInfo.OxData>()

    // 사용자가 몇문제나 풀었는지 계산하는 카운트
    private var count = 0

    // 서버에 요청할때 쓰이는 sIndex
    private var callSIndex = 0

    private var isError = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ox)

        ox_question.setOnClickListener {
            clickQuestion()
        }

        btn_o.setOnClickListener{
            clickO()
        }

        btn_x.setOnClickListener{
            clickX()
        }

        ox_next.setOnClickListener {
            clickNext()
        }

        top_back.setOnClickListener{
            finish()
        }

        oxList = intent.getParcelableArrayListExtra<OxContentInfo.OxData>("oxList") ?: ArrayList<OxContentInfo.OxData>()

        setNextOX(oxList[count])

        my_score.text = String.format("내 점수 : ${preferenceUtils!!.userScore} 점")

        callSIndex = preferenceUtils!!.userSindex
    }

    /**
     * 정답일 때
     *
     * @param goodText
     * @param goodImage
     */
    fun visibleGood(goodText: String, goodImage: String) {

        ox_answer.text = if (TextUtils.isEmpty(goodText)) "" else goodText

        if (TextUtils.isEmpty(goodImage))
            ox_answer_img.setImageDrawable(resources.getDrawable(R.drawable.emoji))
        else
            Glide.with(this).load(goodImage).into(ox_answer_img)

        ox_answer_check.text = resources.getString(R.string.default_good_text)
        ox_answer_check.setTextColor(resources.getColor(R.color.color_4482ff))

        answer_view.visibility = View.VISIBLE
        ox_content.visibility = View.GONE

        preferenceUtils!!.userScore += 1

        my_score.text = String.format("내 점수 : ${preferenceUtils!!.userScore} 점")
    }

    /**
     * 틀렸을 때
     *
     * @param badText
     * @param badImage
     */
    fun visibleBad(badText: String, badImage: String) {

        ox_answer.text = if (TextUtils.isEmpty(badText)) "" else badText

        if (TextUtils.isEmpty(badImage))
            ox_answer_img.setImageDrawable(resources.getDrawable(R.drawable.unhappy))
        else
            Glide.with(this).load(badImage).into(ox_answer_img)

        ox_answer_check.text = resources.getString(R.string.default_bad_text)
        ox_answer_check.setTextColor(resources.getColor(R.color.color_ff4f60))

        answer_view.visibility = View.VISIBLE
        ox_content.visibility = View.GONE
    }


    /**
     * 스페셜 문제 전용 view 처리
     * @param goodText
     * @param goodImage
     */
    fun visibleSpecial(goodText: String, goodImage: String, badText: String, badImage: String) {

        if (answerValue.equals("o", ignoreCase = true)) {

            ox_answer.text = if (TextUtils.isEmpty(goodText)) "" else goodText

            if (TextUtils.isEmpty(goodImage))
                ox_answer_img.setImageDrawable(resources.getDrawable(R.drawable.emoji))
            else
                Glide.with(this).load(goodImage).into(ox_answer_img)

        } else {

            ox_answer.text = if (TextUtils.isEmpty(badText)) "" else badText

            if (TextUtils.isEmpty(badImage))
                ox_answer_img.setImageDrawable(resources.getDrawable(R.drawable.unhappy))
            else
                Glide.with(this).load(badImage).into(ox_answer_img)
        }

        ox_answer_check.text = resources.getString(R.string.default_special_text)
        ox_answer_check.setTextColor(resources.getColor(R.color.color_8cba23))

        answer_view.visibility = View.VISIBLE
        ox_content.visibility = View.GONE

        preferenceUtils!!.userScore += 1


        my_score.text = String.format("내 점수 : ${preferenceUtils!!.userScore} 점")
    }


    fun clickQuestion() {
        NormalPopup(this).apply {
            setCancelable(true)
            setCancelVisible(View.GONE)
            setPopupTitle("문제 다시 보기")
            setOkText("확인")
            setPopupText(this@OXActivity.ox_content.text.toString())
            setOKClick(View.OnClickListener {
                this.dismiss()
            })
            show()
        }
    }


    fun clickO() {

        answerValue = "o"

        btn_x.isEnabled = false
        btn_o.isClickable = false

        checkAnswer()
    }



    fun clickX() {

        answerValue = "x"

        btn_o.isEnabled = false
        btn_x.isClickable = false

        checkAnswer()
    }


    fun checkAnswer() {

        // 스페셜 타입의 퀴즈인 경우
        if (oxList[count].quiz_special == 1) {
            visibleSpecial(oxList[count].quiz_g_coment, oxList[count].quiz_g_img, oxList[count].quiz_coment, oxList[count].quiz_img)
        } else {
            if (answerValue.equals(oxList[count].quiz_ox, ignoreCase = true)) {

                visibleGood(oxList[count].quiz_g_coment, oxList[count].quiz_g_img)

            } else {
                visibleBad(oxList[count].quiz_coment, oxList[count].quiz_img)
            }
        }// 일반 문제는 정답 가르기


        // 사용자가 정답을 체크하면 다음 문제 인덱스 대기
        count++
        D.log(TAG, "Next count > $count")

        // sIndex도 증가시킨다
        callSIndex += 1

        preferenceUtils!!.userSindex = callSIndex
        D.log(TAG, "set sIndex > ${preferenceUtils!!.userSindex} ")

        updateMyInfo()
    }


    override fun onDestroy() {
        // 에러 났을땐 비정상적으로 처리 됬으니 종료시
        // 점수랑 인덱스 -1 씩
        if (isError) {
            preferenceUtils!!.userScore =- 1
            preferenceUtils!!.userSindex =- 1
        }

        super.onDestroy()
    }


    fun clickNext() {

        answerValue = ""

        ox_next.visibility = View.GONE
        ox_question.visibility = View.GONE

        // 현재 가지고 있는 문제를 다 풀어서 더이상 가져올 게 없으면
        if (count == oxList.size) {

            //새롭게 oxList를 요청한다.
            netProgress.setProgressText("문제 요청 중")
            netProgress.show()

            RetrofitClient.callPostGetOX(object : RetrofitApiCallback<OxContentInfo> {
                override fun onError(t: Throwable) {

                    netProgress.dismiss()

                    CommonUtils.showErrorPopup(this@OXActivity, resources.getString(R.string.error_network_unkonw), true)
                }

                override fun onSuccess(code: Int, resultData: OxContentInfo) {
                    netProgress.dismiss()


                    if (resultData.reqCode == 0) {

                        isError = false

                        oxList.addAll(resultData.oxList)
                        setNextOX(oxList[count])

                    } else {

                        CommonUtils.showErrorPopup(this@OXActivity, resultData.reqMsg, true)
                    }

                }

                override fun onFailed(code: Int) {
                    netProgress.dismiss()

                    CommonUtils.showErrorPopup(this@OXActivity, resources.getString(R.string.error_network_unkonw), true)
                }
            }, callSIndex)


        } else {
            setNextOX(oxList[count])
        }

    }



    fun updateMyInfo() {

        RetrofitClient.callPostUpdateUserInfo(object : RetrofitApiCallback<HeaderInfo> {
            override fun onError(t: Throwable) {

                isError = true

                CommonUtils.showErrorPopup(this@OXActivity, resources.getString(R.string.error_network_unkonw), true)
            }

            override fun onSuccess(code: Int, resultData: HeaderInfo) {

                netProgress.dismiss()

                val headerInfo = resultData as HeaderInfo

                if (headerInfo.reqCode != 0) {

                    isError = true

                    CommonUtils.showErrorPopup(this@OXActivity, resources.getString(R.string.error_network_unkonw), true)

                } else {

                    isError = false

                    ox_next.visibility = View.VISIBLE
                    ox_question.visibility = View.VISIBLE
                }
            }

            override fun onFailed(code: Int) {

                isError = true

                CommonUtils.showErrorPopup(this@OXActivity, resources.getString(R.string.error_network_unkonw), true)
            }
        }, preferenceUtils!!.userKey!!, preferenceUtils!!.userScore, preferenceUtils!!.userSindex)
    }


    fun setNextOX(oxData: OxContentInfo.OxData) {

        //문제를 셋팅할 때마다 callSIndex에 문제의 인덱스 셋팅
        callSIndex = oxData.quiz_index

        ox_content.text = oxData.quiz_text
        ox_tag.text = oxData.quiz_tag

        answer_view.visibility = View.GONE
        ox_content.visibility = View.VISIBLE

        btn_x.isEnabled = true
        btn_x.isClickable = true
        btn_o.isEnabled = true
        btn_o.isClickable = true
    }

}
