package com.seungjun.randomox.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.seungjun.randomox.BaseActivity
import com.seungjun.randomox.R
import com.seungjun.randomox.adapter.ReqMailListAdapter
import com.seungjun.randomox.db.LetterDBData
import com.seungjun.randomox.view.LetterPopup
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_req_mail.*
import kotlinx.android.synthetic.main.activity_sendmail.*
import kotlinx.android.synthetic.main.view_top_bar.*
import org.jetbrains.anko.startActivity

class ReqMailActivity : BaseActivity() {

    companion object {
        private val TAG = "ReqMailActivity"
    }

    private val adapter by lazy {
        ReqMailListAdapter(this).apply {
            baseDisPosable.add(
                    letterDao.getAllData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            setLetterDBData(it)
                            notifyDataSetChanged()
                        })
            )

            setLetterItemClick(View.OnClickListener {
                val data = it.tag as LetterDBData

                if(data.letter_read == "N"){
                    data.letter_read = "Y"
                    baseDisPosable.add(
                            Completable.fromCallable { letterDao.changeReadState(data) }
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        letterDao.getAllData()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    setLetterDBData(it)
                                                    notifyDataSetChanged()
                                                })
                                    })
                    )
                }

                LetterPopup(this@ReqMailActivity).apply {
                    setPopupText(data.letter_req_text)
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
            startActivity<SendMailActivity>()
        }

    }


}
