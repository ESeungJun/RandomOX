package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.adapter.ReqMailListAdapter;
import com.seungjun.randomox.db.LetterDBUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReqMailActivity extends BaseActivity {

    private static final String TAG = "ReqMailActivity";

    private ReqMailListAdapter adapter;

    @BindView(R.id.list_letter)
    RecyclerView letterListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_req_mail);

        ButterKnife.bind(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        letterListView.setLayoutManager(manager);

        adapter = new ReqMailListAdapter(this);
        adapter.setLetterDBData(LetterDBUtils.getInstance(this).getAllData());

        letterListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick(R.id.top_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.send_mail)
    public void clickSendMail(){
        startActivity(new Intent(this, SendMailActivity.class));
    }
}
