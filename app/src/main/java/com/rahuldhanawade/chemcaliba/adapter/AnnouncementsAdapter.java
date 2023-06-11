package com.rahuldhanawade.chemcaliba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.AnnouncementsActivity;

import java.util.ArrayList;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder>{

    private ArrayList<AnnouncementsPOJO> announcementsPOJOS;
    private Context context;
    public AnnouncementsAdapter(Context context, ArrayList<AnnouncementsPOJO> announcementsPOJOS) {
        this.context=context;
        this.announcementsPOJOS=announcementsPOJOS;
    }

    @NonNull
    @Override
    public AnnouncementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcements,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementsAdapter.ViewHolder holder, int position) {
        AnnouncementsPOJO announcementsPOJOS1= announcementsPOJOS.get(position);
        holder.tv_create_AN.setText(announcementsPOJOS1.getCreatedDate());
        holder.tv_title_AN.setText(announcementsPOJOS1.getAnnouncementTitle());
        holder.tv_category_AN.setText(announcementsPOJOS1.getCourseCategoryName());
        holder.tv_description_AN.setText(announcementsPOJOS1.getAnnouncementDescription());

    }

    @Override
    public int getItemCount() {
        return announcementsPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_create_AN,tv_title_AN,tv_category_AN,tv_description_AN;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_create_AN = itemView.findViewById(R.id.tv_create_AN);
            tv_title_AN = itemView.findViewById(R.id.tv_title_AN);
            tv_category_AN = itemView.findViewById(R.id.tv_category_AN);
            tv_description_AN = itemView.findViewById(R.id.tv_description_AN);
        }
    }
}
