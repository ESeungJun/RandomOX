package com.seungjun.randomox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seungjun.randomox.R;
import com.seungjun.randomox.network.data.RankInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RankInfo.RankData> rankInfos = new ArrayList<>();

    private Context context;

    public RankListAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_row_rank_list, viewGroup, false);
        return new RankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if(rankInfos.size() > i){

            ((RankViewHolder)viewHolder).nick.setText(rankInfos.get(i).user_nick);
            ((RankViewHolder)viewHolder).score.setText(rankInfos.get(i).user_point);


            if(rankInfos.get(i).rank < 4){
                ((RankViewHolder)viewHolder).rank.setVisibility(View.GONE);
                ((RankViewHolder)viewHolder).img.setVisibility(View.VISIBLE);


                switch (rankInfos.get(i).rank){
                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;
                }

            }else{
                ((RankViewHolder)viewHolder).rank.setVisibility(View.VISIBLE);
                ((RankViewHolder)viewHolder).img.setVisibility(View.GONE);

                ((RankViewHolder)viewHolder).rank.setText(rankInfos.get(i).rank +"위");
            }

        }

    }


    public void setRankInfos(ArrayList<RankInfo.RankData> list){
        rankInfos = list;
    }


    @Override
    public int getItemCount() {
        return rankInfos.size();
    }


    public class RankViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.view_list_nickname)
        TextView nick;

        @BindView(R.id.view_list_score)
        TextView score;

        @BindView(R.id.view_list_rank)
        TextView rank;

        @BindView(R.id.view_list_rank_img)
        ImageView img;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
