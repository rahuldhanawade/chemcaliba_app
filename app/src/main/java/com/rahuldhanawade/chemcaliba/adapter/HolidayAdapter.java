package com.rahuldhanawade.chemcaliba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.HolidayActivity;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.ViewHolder> {

    private ArrayList<HolidayPOJO> holidayPOJOS;
    private Context context;

    public HolidayAdapter(Context context, ArrayList<HolidayPOJO> holidayPOJOS) {
        this.context=context;
        this.holidayPOJOS=holidayPOJOS;
    }

    @NonNull
    @Override
    public HolidayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holiday,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayAdapter.ViewHolder holder, int position) {
        HolidayPOJO holidayPOJOS1= holidayPOJOS.get(position);
        holder.tv_create_HD.setText(holidayPOJOS1.getCreatedDate());
        holder.tv_title_HD.setText(holidayPOJOS1.getHolidayInformationTitle());
        holder.tv_category_HD.setText(holidayPOJOS1.getCourseCategoryName());
        holder.tv_from_HD.setText(holidayPOJOS1.getHolidayInformationFromDate());
        holder.tv_to_HD.setText(holidayPOJOS1.getHolidayInformationToDate());
    }

    @Override
    public int getItemCount() {
        return holidayPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_create_HD,tv_title_HD,tv_category_HD,tv_from_HD,tv_to_HD;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_create_HD = itemView.findViewById(R.id.tv_create_HD);
            tv_title_HD = itemView.findViewById(R.id.tv_title_HD);
            tv_category_HD = itemView.findViewById(R.id.tv_category_HD);
            tv_from_HD = itemView.findViewById(R.id.tv_from_HD);
            tv_to_HD = itemView.findViewById(R.id.tv_to_HD);
        }
    }
}
