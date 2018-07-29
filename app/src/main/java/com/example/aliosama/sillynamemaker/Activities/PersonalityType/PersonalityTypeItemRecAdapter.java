package com.example.aliosama.sillynamemaker.Activities.PersonalityType;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTypesModel;
import com.example.aliosama.sillynamemaker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ali Ussama on 6/16/2018.
 */

public class PersonalityTypeItemRecAdapter extends RecyclerView.Adapter<PersonalityTypeItemRecAdapter.viewHolder> {

    private ArrayList<PersonalityTypesModel.YouMayKnow> data;

    public PersonalityTypeItemRecAdapter(ArrayList<PersonalityTypesModel.YouMayKnow> data) {
        this.data = data;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_personality_type_item_rec_view_row_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Picasso.get().load(data.get(position).getImageUrl()).into(holder.CharacterImage);
        if (data.get(position).getName().contains(",")){
            String [] result = data.get(position).getName().split(",");
            String Title = result[0];
            String Type = result[1];
            holder.CharacterTitle.setText(Title);
            holder.CharacterType.setText(Type);
            holder.CharacterType.setVisibility(View.VISIBLE);
        }else {
            holder.CharacterType.setVisibility(View.GONE);
            holder.CharacterTitle.setText(data.get(position).getName());
        }
    }

    public void UpdateAdapter(ArrayList<PersonalityTypesModel.YouMayKnow> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView CharacterImage;
        TextView CharacterTitle;
        TextView CharacterType;

        viewHolder(View itemView) {
            super(itemView);
            CharacterImage = itemView.findViewById(R.id.APTIRVRI_CharacterImage);
            CharacterTitle = itemView.findViewById(R.id.APTIRVRI_CharacterTitle_TextView);
            CharacterType = itemView.findViewById(R.id.APTIRVRI_CharacterType_TextView);

        }
    }
}
