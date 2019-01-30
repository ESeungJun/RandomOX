package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.seungjun.randomox.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 네트워크 요청시 노출되는 애니메이션 프로그래스바
 * Created by SeungJun on 2016-09-30.
 */

public class NetworkProgressDialog extends Dialog {

    private Context context;

    @BindView(R.id.progress_text)
    TextView progressText;

    /**
     * 네트워크 프로그래스바 초기화
     * @param context
     */
    public NetworkProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.network_progress_dialog);

        ButterKnife.bind(this);

        this.context = context;

        this.setCancelable(false);
    }

    public void setProgressText(String text){
        progressText.setText(text);
    }



    @Override
    public void show() {

        if(((Activity)context).isFinishing())
            return;

        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
