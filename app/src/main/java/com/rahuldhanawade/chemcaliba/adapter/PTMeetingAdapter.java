package com.rahuldhanawade.chemcaliba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.PTActivity;

import java.util.ArrayList;

public class PTMeetingAdapter  extends RecyclerView.Adapter<PTMeetingAdapter.ViewHolder>{

    private ArrayList<PTMeetingPOJO> ptMeetingPOJOS;
    private Context context;
    public PTMeetingAdapter(Context context, ArrayList<PTMeetingPOJO> ptMeetingPOJOS) {
        this.context=context;
        this.ptMeetingPOJOS=ptMeetingPOJOS;
    }

    @NonNull
    @Override
    public PTMeetingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pt_meeting,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PTMeetingAdapter.ViewHolder holder, int position) {
        PTMeetingPOJO ptMeetingPOJOS1= ptMeetingPOJOS.get(position);
        holder.tv_create_PT.setText(ptMeetingPOJOS1.getCreatedDate());
        holder.tv_title_PT.setText(ptMeetingPOJOS1.getPtMeetingsTitle());
        holder.tv_category_PT.setText(ptMeetingPOJOS1.getCourseCategoryName());
        holder.tv_meetingon_PT.setText(ptMeetingPOJOS1.getPtMeetingsDateTime());
    }

    @Override
    public int getItemCount() {
        return ptMeetingPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_create_PT,tv_title_PT,tv_category_PT,tv_meetingon_PT;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_create_PT = itemView.findViewById(R.id.tv_create_PT);
            tv_title_PT = itemView.findViewById(R.id.tv_title_PT);
            tv_category_PT = itemView.findViewById(R.id.tv_category_PT);
            tv_meetingon_PT = itemView.findViewById(R.id.tv_meetingon_PT);
        }
    }
}
