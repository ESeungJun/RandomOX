package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewPopup extends Dialog {

    private Context context;

    @BindView(R.id.popup_webview)
    WebView popupText;


    public WebViewPopup(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_webview_dialog);

        this.context = context;

        ButterKnife.bind(this);
    }


    public void setPopupText(String url){
        popupText.loadUrl(url);
    }

    @OnClick(R.id.btn_ok)
    public void close(){
        this.dismiss();
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
