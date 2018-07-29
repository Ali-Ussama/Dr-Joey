package com.example.aliosama.sillynamemaker.Fragments.PersonalityTypes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aliosama.sillynamemaker.Activities.PersonalityType.PersonalityTypeItemActivity;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTypesModel;
import com.example.aliosama.sillynamemaker.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali Ussama on 6/13/2018.
 */

public class PersonalityTypesRecAdapter extends RecyclerView.Adapter<PersonalityTypesRecAdapter.viewHolder> {

    private ArrayList<PersonalityTypesModel> data;

    public PersonalityTypesRecAdapter() {

    }

    public PersonalityTypesRecAdapter(ArrayList<PersonalityTypesModel> data) {
        this.data = data;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_personality_type_rec_view_row_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        try{
            Picasso.get().load(data.get(position).getCharacterImageUrl()).into(holder.CharacterImage);
            holder.CharacterTitle.setText(data.get(position).getCharacterTitle());
            holder.CharacterAcronym.setText(data.get(position).getCharacterAcronymText());
            holder.CharacterMinDescription.setText(data.get(position).getMinDescription());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void updateData(ArrayList<PersonalityTypesModel> mdata){
        this.data = mdata;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView CharacterImage;
        TextView CharacterTitle, CharacterAcronym, CharacterMinDescription;

        viewHolder(View itemView) {
            super(itemView);
            CharacterImage = itemView.findViewById(R.id.FPTRVRI_Character_ImageView);
            CharacterTitle = itemView.findViewById(R.id.FPTRVRI_Character_Title);
            CharacterAcronym = itemView.findViewById(R.id.FPTRVRI_Character_Acronym);
            CharacterMinDescription = itemView.findViewById(R.id.FPTRVRI_Character_MinDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try{
                Intent intent = new Intent(view.getContext(), PersonalityTypeItemActivity.class);
                intent.putExtra("PersonalityTypeRowItemData", data.get(getAdapterPosition()));
                view.getContext().startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
