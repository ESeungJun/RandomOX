package com.seungjun.randomox

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.seungjun.randomox.utils.PreferenceUtils
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.view.NetworkProgressDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * 모든 액티비티에 부모가 될 액티비티
 * 화면에서 공통으로 처리해야하는 작업을 여기서 한다
 * ex) 권한 질의, 네트워크 체크 등등
 */
open class BaseActivity : AppCompatActivity() {

    protected val netProgress: NetworkProgressDialog by lazy {
        NetworkProgressDialog(this@BaseActivity)
    }

    protected var preferenceUtils: PreferenceUtils? = null
    protected var baseDisPosable: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferenceUtils == null)
            preferenceUtils = PreferenceUtils.getInstance(this)

        if(baseDisPosable == null)
            baseDisPosable = CompositeDisposable()

    }
}
