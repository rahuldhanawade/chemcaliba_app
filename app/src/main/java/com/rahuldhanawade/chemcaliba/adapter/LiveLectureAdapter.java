package com.rahuldhanawade.chemcaliba.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahuldhanawade.chemcaliba.R;

import java.util.ArrayList;

public class LiveLectureAdapter extends RecyclerView.Adapter<LiveLectureAdapter.ViewHolder> {

    private ArrayList<LiveLecturePOJO> liveLecturePOJOS;
    private Context context;

    public LiveLectureAdapter(Context Context, ArrayList<LiveLecturePOJO> liveLecturePOJOS) {
        this.context=Context;
        this.liveLecturePOJOS=liveLecturePOJOS;
    }

    @NonNull
    @Override
    public LiveLectureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_lecture,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveLectureAdapter.ViewHolder holder, int position) {
        LiveLecturePOJO liveLecturePOJO1= liveLecturePOJOS.get(position);
        holder.tv_valid_date_LL.setText(liveLecturePOJO1.getValidUpto());
        holder.tv_course_title_LL.setText(liveLecturePOJO1.getCourseName());
        holder.tv_video_title_LL.setText(liveLecturePOJO1.getVideoTitle());
        holder.tv_course_category_name_LL.setText(liveLecturePOJO1.getCourseCategoryName());

    }

    @Override
    public int getItemCount() {
        return liveLecturePOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_valid_date_LL,tv_course_title_LL,tv_video_title_LL,tv_course_category_name_LL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_valid_date_LL = itemView.findViewById(R.id.tv_valid_date_LL);
            tv_course_title_LL = itemView.findViewById(R.id.tv_course_title_LL);
            tv_video_title_LL = itemView.findViewById(R.id.tv_video_title_LL);
            tv_course_category_name_LL = itemView.findViewById(R.id.tv_course_category_name_LL);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    LiveLecturePOJO liveLecturePOJO1 = liveLecturePOJOS.get(itemPosition);
                    String videoLink = liveLecturePOJO1.getVideoLink();
                    Log.d("TAG", "onClick: "+videoLink);
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(videoLink));
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }
    }
}
