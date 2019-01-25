package com.seungjun.randomox;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_o)
    ImageView btnO;

    @BindView(R.id.btn_x)
    ImageView btnX;

    int[][] states = new int[][] {
            new int[] {android.R.attr.state_pressed},
            new int[] {android.R.attr.state_enabled}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        btnO.setImageTintList(new ColorStateList(states,
                new int[] {getResources().getColor(R.color.color_305bb2), getResources().getColor(R.color.color_4482ff)}));

        btnX.setImageTintList(new ColorStateList(states,
                new int[] {getResources().getColor(R.color.color_b23743), getResources().getColor(R.color.color_ff4f60)}));
    }

    @OnClick(R.id.btn_o)
    public void clickO(){

    }


    @OnClick(R.id.btn_x)
    public void clickX(){

    }
}
