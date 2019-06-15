package com.seungjun.randomox.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seungjun.randomox.R
import com.seungjun.randomox.network.data.RankInfo
import kotlinx.android.synthetic.main.view_row_rank_list.view.*
import java.util.*

class RankListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var rankInfos: List<RankInfo.RankData> = listOf()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_row_rank_list, viewGroup, false)
        return RankViewHolder(v)
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        if (rankInfos.size > i) {

            val data = rankInfos[i]

            with(viewHolder as RankViewHolder){
                nick.text = data.user_nick
                score.text = String.format("${data.user_point} 점")
                rank.text = String.format("${data.rank} 위")
            }

        }
    }


    fun setRankInfos(list: ArrayList<RankInfo.RankData>) {
        rankInfos = list
    }


    override fun getItemCount() = rankInfos.size


    inner class RankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nick = itemView.view_list_nickname

        val score = itemView.view_list_score

        val rank = itemView.view_list_rank

    }
}
