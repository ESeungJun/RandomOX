package com.seungjun.randomox.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.adapter.ReqMailListAdapter
import com.seungjun.randomox.db.LetterDBData
import com.seungjun.randomox.db.LetterDBUtils
import com.seungjun.randomox.view.LetterPopup

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class ReqMailActivity : BaseActivity() {

    private var adapter: ReqMailListAdapter? = null

    @BindView(R.id.list_letter)
    internal var letterListView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_req_mail)

        ButterKnife.bind(this)

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL

        letterListView!!.layoutManager = manager

        adapter = ReqMailListAdapter(this)
        adapter!!.setLetterDBData(LetterDBUtils.getInstance(this).allData)
        adapter!!.setLetterItemClick { view ->
            val date = view.tag as LetterDBData

            LetterDBUtils.getInstance(this@ReqMailActivity).updateLetterReadState(date._id, "Y")
            adapter!!.setLetterDBData(LetterDBUtils.getInstance(this@ReqMailActivity).allData)
            adapter!!.notifyDataSetChanged()

            val letterPopup = LetterPopup(this@ReqMailActivity)
            letterPopup.setPopupText(date.letter_req_text)
            letterPopup.show()
        }

        letterListView!!.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

    }

    @OnClick(R.id.top_back)
    fun back() {
        finish()
    }

    @OnClick(R.id.send_mail)
    fun clickSendMail() {
        startActivity(Intent(this, SendMailActivity::class.java))
    }

    companion object {

        private val TAG = "ReqMailActivity"
    }
}
