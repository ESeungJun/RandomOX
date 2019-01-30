package com.seungjun.randomox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seungjun.randomox.R;
import com.seungjun.randomox.db.LetterDBData;
import com.seungjun.randomox.view.LetterPopup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReqMailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<LetterDBData> letterDBData = new ArrayList<>();


    private Context context;

    public ReqMailListAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_row_letter_list, viewGroup, false);
        return new LetterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if(letterDBData.size() > i){

            ((LetterViewHolder)viewHolder).title.setText("개발자의 " + i +"번째 편지");
            ((LetterViewHolder)viewHolder).date.setText(letterDBData.get(i).getLetter_req_date());

            ((LetterViewHolder)viewHolder).parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LetterPopup letterPopup = new LetterPopup(context);
                    letterPopup.setPopupText(letterDBData.get(i).getLetter_req_text());
                    letterPopup.show();

                }
            });
        }

    }


    public void setLetterDBData(ArrayList<LetterDBData> list){
        letterDBData = list;
    }


    @Override
    public int getItemCount() {
        return letterDBData.size();
    }


    public class LetterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.view_list_title)
        TextView title;

        @BindView(R.id.view_list_date)
        TextView date;

        @BindView(R.id.view_list_row)
        LinearLayout parent;

        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
