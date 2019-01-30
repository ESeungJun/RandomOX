package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NormalPopup extends Dialog {

    private Context context;

    @BindView(R.id.popup_text)
    TextView popupText;

    @BindView(R.id.popup_title)
    TextView popupTitle;

    @BindView(R.id.btn_ok)
    TextView btnOk;

    @BindView(R.id.btn_cancel)
    TextView btn_cancel;

    public NormalPopup(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_normal_dialog);

        this.context = context;

        ButterKnife.bind(this);

        this.setCancelable(false);
    }


    public void setPopupText(String text){
        popupText.setText(text);
    }


    public void setPopupTitle(String title){
        popupTitle.setText(title);
    }

    public void setOKClick(View.OnClickListener listener){
        btnOk.setOnClickListener(listener);
    }

    public void setCancelClick(View.OnClickListener listener){
        btn_cancel.setOnClickListener(listener);
    }

    public void setCancelVisible(int visible){
        btn_cancel.setVisibility(visible);
    }

    public void setCancelText(String text){
        btn_cancel.setText(text);
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
