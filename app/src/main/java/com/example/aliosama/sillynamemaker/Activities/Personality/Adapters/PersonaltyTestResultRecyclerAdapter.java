package com.example.aliosama.sillynamemaker.Activities.Personality.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTestResultModel;
import com.example.aliosama.sillynamemaker.R;

import java.util.ArrayList;

/**
 * Created by ali Ussama on 6/9/2018.
 */

public class PersonaltyTestResultRecyclerAdapter extends RecyclerView.Adapter<PersonaltyTestResultRecyclerAdapter.viewHolder> {
    private ArrayList<PersonalityTestResultModel.Categories> data;

    public PersonaltyTestResultRecyclerAdapter(ArrayList<PersonalityTestResultModel.Categories> data) {
        this.data = data;
        Log.i("PersonalityRecAdapter","Data Size = "+String.valueOf(data.size()));
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personality_test_result_rec_view_row_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        try {
            holder.CategoryTitle.setText(data.get(position).getCategoryName());
            holder.CategoryDescription.setText(data.get(position).getCategory_Description());

            Log.i("onBindViewHolder","Positions = "+String.valueOf(position));
            Log.i("onBindViewHolder","CategoryTitle = "+data.get(position).getCategoryName());
            Log.i("onBindViewHolder","CategoryDescription = "+data.get(position).getCategory_Description());

            LinearLayout.LayoutParams LeftLayoutParams = (LinearLayout.LayoutParams) holder.LeftLinearLayout.getLayoutParams();
            LinearLayout.LayoutParams RightLayoutParams = (LinearLayout.LayoutParams) holder.RightLinearLayout.getLayoutParams();
            int LeftPercentage = data.get(position).getLeastFieldPercentage();
            float Leftresult = (((LeftPercentage*12)/100)/2);
            int RightPercentage = data.get(position).getMostFieldPercentage();
            float Rightresult = (((RightPercentage*12)/100)/2);

            if (Leftresult/Rightresult >= 0.5){
                LeftLayoutParams.weight = Leftresult;
                holder.LeftLinearLayout.setLayoutParams(LeftLayoutParams);
                RightLayoutParams.weight = Rightresult;
                holder.RightLinearLayout.setLayoutParams(RightLayoutParams);
                holder.LeftFieldNameTextView.setVisibility(View.VISIBLE);
                holder.LeftFieldNameTextView.setText(data.get(position).getLeastFieldName());
                holder.RightFieldNameTextView.setVisibility(View.VISIBLE);
                holder.RightFieldNameTextView.setText(data.get(position).getMostFieldName());
                // Display Both Texts.
            }else{
                //Display Max % Text.
                if (Leftresult < 1)
                    Leftresult = 1;
                LeftLayoutParams.weight = Leftresult;
                holder.LeftLinearLayout.setLayoutParams(LeftLayoutParams);
                RightLayoutParams.weight = Rightresult;
                holder.RightLinearLayout.setLayoutParams(RightLayoutParams);
                holder.LeftFieldNameTextView.setVisibility(View.GONE);
                holder.RightFieldNameTextView.setVisibility(View.VISIBLE);
                holder.RightFieldNameTextView.setText(data.get(position).getMostFieldName());
            }
            String LeftPercent = String.valueOf(LeftPercentage).concat("%");
            String RightPercent = String.valueOf(RightPercentage).concat("%");
            holder.LeftPercentageTextView.setText(LeftPercent);
            holder.RightPercentageTextView.setText(RightPercent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<PersonalityTestResultModel.Categories> data){
        this.data = data;
        Log.i("PersonalityRecAdapter","Updated Data Size = "+String.valueOf(data.size()));
        notifyDataSetChanged();
    }
    class viewHolder extends RecyclerView.ViewHolder{
        TextView CategoryTitle, CategoryDescription;
        TextView LeftPercentageTextView, RightPercentageTextView;
        TextView LeftFieldNameTextView, RightFieldNameTextView;
        LinearLayout LeftLinearLayout, RightLinearLayout;

        viewHolder(View itemView) {
            super(itemView);
            CategoryTitle = itemView.findViewById(R.id.PTRRVRI_Category_Title_TextView);
            CategoryDescription = itemView.findViewById(R.id.PTRRVRI_Category_Description_TextView);
            LeftLinearLayout = itemView.findViewById(R.id.PTRRVRI_Left_LinearLayout);
            RightLinearLayout = itemView.findViewById(R.id.PTRRVRI_Right_LinearLayout);
            LeftPercentageTextView = itemView.findViewById(R.id.PTRRVRI_Left_Percentage_TextView);
            LeftFieldNameTextView = itemView.findViewById(R.id.PTRRVRI_Left_Field_Name_TextView);
            RightFieldNameTextView = itemView.findViewById(R.id.PTRRVRI_Right_Field_Name_TextView);
            RightPercentageTextView = itemView.findViewById(R.id.PTRRVRI_Right_Percentage_TextView);
        }
    }
}
