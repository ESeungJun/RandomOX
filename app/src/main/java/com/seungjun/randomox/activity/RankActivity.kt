package com.seungjun.randomox.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.adapter.RankListAdapter
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.data.RankInfo
import com.seungjun.randomox.utils.CommonUtils

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.seungjun.randomox.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.view_top_bar.*

class RankActivity : BaseActivity() {

    private val rankListAdapter by lazy { RankListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rank)

        top_back.setOnClickListener {
            finish()
        }

        top_title.text = "TOP 100"
        my_score.text = String.format("내 점수 : ${preferenceUtils!!.userScore}점")
        my_rank_info.text = String.format("현재 내 랭킹은\n${preferenceUtils!!.userRank}위")

        list_rank.apply {
            layoutManager = LinearLayoutManager(this@RankActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = rankListAdapter
        }

        callRankData()
    }


    private fun callRankData() {

        with(netProgress){
            setProgressText("랭킹 요청 중")
            show()
        }

        RetrofitClient.callGetRankInfo(object : RetrofitApiCallback<RankInfo> {
            override fun onError(t: Throwable) {

                netProgress.dismiss()
                CommonUtils.showErrorPopup(this@RankActivity, resources.getString(R.string.error_network_unkonw), true)
            }

            override fun onSuccess(code: Int, resultData: RankInfo) {

                netProgress.dismiss()

                if (resultData.reqCode == 0) {

                    rank_1_text.text = String.format("${resultData.ranks[0].user_nick}\n ${resultData.ranks[0].user_point} 점")
                    rank_2_text.text = String.format("${resultData.ranks[1].user_nick}\n ${resultData.ranks[1].user_point} 점")
                    rank_3_text.text = String.format("${resultData.ranks[2].user_nick}\n ${resultData.ranks[2].user_point} 점")

                    for (i in 0..2) {
                        resultData.ranks.removeAt(0)
                    }

                    with(rankListAdapter){
                        setRankInfos(resultData.ranks)
                        notifyDataSetChanged()
                    }

                } else {
                    CommonUtils.showErrorPopup(this@RankActivity, resultData.reqMsg, true)
                }

            }

            override fun onFailed(code: Int) {


                netProgress.dismiss()

                CommonUtils.showErrorPopup(this@RankActivity, resources.getString(R.string.error_network_unkonw), true)
            }
        })
    }
}
