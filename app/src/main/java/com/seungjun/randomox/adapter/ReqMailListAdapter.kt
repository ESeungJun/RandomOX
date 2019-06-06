package com.seungjun.randomox.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seungjun.randomox.R
import com.seungjun.randomox.db.LetterDBData
import kotlinx.android.synthetic.main.view_row_letter_list.view.*
import java.util.*

class ReqMailListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var letterDBData = ArrayList<LetterDBData>()

    private var letterItemClick: View.OnClickListener? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_row_letter_list, viewGroup, false)
        return LetterViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        if (letterDBData.size > i) {

            val data = letterDBData[i];

            with(viewHolder as LetterViewHolder){
                title.text = String.format("개발자의 ${data._id}번째 편지")
                date.text = data.letter_req_date
                parent.setOnClickListener(letterItemClick)

                if(data.letter_read.equals("Y", true))
                    img.setImageDrawable(context.resources.getDrawable(R.drawable.letter))
                else
                    img.setImageDrawable(context.resources.getDrawable(R.drawable.mail))

                parent.tag = data
            }
        }

    }

    fun setLetterItemClick(letterItemClick: View.OnClickListener) {
        this.letterItemClick = letterItemClick
    }


    fun setLetterDBData(list: ArrayList<LetterDBData>) {
        letterDBData = list
    }


    override fun getItemCount() = letterDBData.size


    inner class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.view_list_title

        val date = itemView.view_list_date

        val parent = itemView.view_list_row

        val img = itemView.view_list_img

    }
}
