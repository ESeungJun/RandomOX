package com.seungjun.randomox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.adapter.RankListAdapter;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.RankInfo;
import com.seungjun.randomox.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankActivity extends BaseActivity {

    @BindView(R.id.top_title)
    TextView topTitle;

    @BindView(R.id.my_score)
    TextView myScore;

    @BindView(R.id.my_rank_info)
    TextView myRank;

    @BindView(R.id.rank_1_text)
    TextView totalRank1;

    @BindView(R.id.rank_2_text)
    TextView totalRank2;

    @BindView(R.id.rank_3_text)
    TextView totalRank3;

    @BindView(R.id.list_rank)
    RecyclerView rankList;

    private RankListAdapter rankListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rank);

        ButterKnife.bind(this);

        topTitle.setText("TOP 100");
        myScore.setText("내 점수 : " + preferenceUtils.getUserScore() +"점");
        myRank.setText("현재 내 랭킹은\n" + preferenceUtils.getUserRank() +"위");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        rankList.setLayoutManager(manager);

        rankListAdapter = new RankListAdapter(this);
        rankList.setAdapter(rankListAdapter);

        callRankData();
    }


    @OnClick(R.id.top_back)
    public void back() {
        finish();
    }


    private void callRankData(){
        netProgress.setProgressText("랭킹 요청 중");
        netProgress.show();

        networkClient.callGetRankInfo(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                netProgress.dismiss();
                CommonUtils.showErrorPopup(RankActivity.this, getResources().getString(R.string.error_network_unkonw), true);
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                netProgress.dismiss();

                RankInfo rankInfo = (RankInfo) resultData;

                if(rankInfo.reqCode == 0){

                    totalRank1.setText(rankInfo.ranks.get(0).user_nick +"\n" + rankInfo.ranks.get(0).user_point +"점");
                    totalRank2.setText(rankInfo.ranks.get(1).user_nick +"\n" + rankInfo.ranks.get(1).user_point +"점");
                    totalRank3.setText(rankInfo.ranks.get(2).user_nick +"\n" + rankInfo.ranks.get(2).user_point +"점");

                    for(int i = 0; i < 3; i++){
                        rankInfo.ranks.remove(0);
                    }

                    rankListAdapter.setRankInfos(rankInfo.ranks);
                    rankListAdapter.notifyDataSetChanged();

                }else{
                    CommonUtils.showErrorPopup(RankActivity.this, rankInfo.reqMsg, true);
                }

            }

            @Override
            public void onFailed(int code) {


                netProgress.dismiss();

                CommonUtils.showErrorPopup(RankActivity.this, getResources().getString(R.string.error_network_unkonw), true);
            }
        });
    }
}
