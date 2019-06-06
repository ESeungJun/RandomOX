package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.adapter.ReqMailListAdapter
import com.seungjun.randomox.db.LetterDBData
import com.seungjun.randomox.db.LetterDBUtils
import com.seungjun.randomox.view.LetterPopup

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_req_mail.*
import kotlinx.android.synthetic.main.activity_sendmail.*
import kotlinx.android.synthetic.main.view_top_bar.*

class ReqMailActivity : BaseActivity() {

    companion object {
        private val TAG = "ReqMailActivity"
    }

    private val adapter by lazy {
        ReqMailListAdapter(this).apply {
            setLetterDBData(LetterDBUtils.allData)
            setLetterItemClick (View.OnClickListener{
                val date = it.tag as LetterDBData

                LetterDBUtils.updateLetterReadState(date._id, "Y")
                this.setLetterDBData(LetterDBUtils.allData)
                this.notifyDataSetChanged()

                LetterPopup(this@ReqMailActivity).apply {
                    setPopupText(date.letter_req_text)
                    show()
                }
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_req_mail)

        list_letter.apply {
            layoutManager = LinearLayoutManager(this@ReqMailActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = this@ReqMailActivity.adapter
        }

        top_back.setOnClickListener {
            finish()
        }

        send_mail.setOnClickListener {
            startActivity(Intent(this@ReqMailActivity, SendMailActivity::class.java))
        }

    }


}
