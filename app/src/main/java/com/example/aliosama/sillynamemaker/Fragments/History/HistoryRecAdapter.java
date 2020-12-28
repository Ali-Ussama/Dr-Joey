package com.example.aliosama.sillynamemaker.Fragments.History;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aliosama.sillynamemaker.Activities.HistoryDayStatisticsActivity;
import com.example.aliosama.sillynamemaker.R;

import java.util.ArrayList;

/**
 * Created by ali Ussama on 4/29/2018.
 */

public class HistoryRecAdapter extends RecyclerView.Adapter<HistoryRecAdapter.viewHolder> {

    ArrayList<ArrayList<Float>>statusPercentData;
    ArrayList<String> statusLabelsData;
    ArrayList<String> DaysData;

    public HistoryRecAdapter(ArrayList<ArrayList<Float>> statusPercentData, ArrayList<String> statusLabelsData, ArrayList<String> daysData) {
        this.statusPercentData = statusPercentData;
        this.statusLabelsData = statusLabelsData;
        DaysData = daysData;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_fragment_expandable_listview_row_item, null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.Days.setText(DaysData.get(position));
    }

    @Override
    public int getItemCount() {
        return DaysData.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView Days;
        public viewHolder(final View itemView) {
            super(itemView);
            Days = itemView.findViewById(R.id.HistoryRec_vew_row_item_DaysTextView);

            Days.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), HistoryDayStatisticsActivity.class);
                    intent.putExtra("Percent",statusPercentData.get(getAdapterPosition()));
                    intent.putExtra("Labels",statusLabelsData);
                    intent.putExtra("day", DaysData.get(getAdapterPosition()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
