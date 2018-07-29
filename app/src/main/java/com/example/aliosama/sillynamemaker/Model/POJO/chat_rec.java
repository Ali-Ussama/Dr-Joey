package com.example.aliosama.sillynamemaker.Model.POJO;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.aliosama.sillynamemaker.R;

/**
 * Created by aliosama on 2/7/2018.
 */

public class chat_rec extends RecyclerView.ViewHolder {
    public TextView leftText,rightText;
    public chat_rec(View itemView) {
        super(itemView);

        leftText = itemView.findViewById(R.id.leftText);
        rightText = itemView.findViewById(R.id.rightText);
    }
}
