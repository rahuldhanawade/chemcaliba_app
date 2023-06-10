package com.rahuldhanawade.chemcaliba.adapter;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.IMG_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.setDateFormat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.CoursesDetailsActivity;

import java.util.ArrayList;

public class EnrolledAdapter  extends RecyclerView.Adapter<EnrolledAdapter.ViewHolder> {

    private ArrayList<EnrolledPOJO> enrolledPOJOS;
    private Context context;

    public EnrolledAdapter(Context context, ArrayList<EnrolledPOJO> enrolledPOJOS) {
        this.context=context;
        this.enrolledPOJOS=enrolledPOJOS;
    }

    @NonNull
    @Override
    public EnrolledAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enrolled_course,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrolledAdapter.ViewHolder holder, int position) {
        EnrolledPOJO enrolledPOJOS1= enrolledPOJOS.get(position);
        holder.tv_valid_date.setText(setDateFormat(enrolledPOJOS1.getValid_upto()));
        holder.tv_title_ec.setText(enrolledPOJOS1.getCourse_name());
        holder.tv_enrolled_date_ec.setText(setDateFormat(enrolledPOJOS1.getEnrollment_date()));
        holder.tv_duration_ec.setText(enrolledPOJOS1.getNo_of_days());
        String img_url = IMG_URL+enrolledPOJOS1.getCourse_master_id()+"/course-image/"+enrolledPOJOS1.getCourse_image();
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.logo_chemcaliba)
                        .error(R.drawable.logo_chemcaliba))
                .load(img_url)
                .into(holder.iv_img_ec);
    }

    @Override
    public int getItemCount() {
        return enrolledPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_valid_date,tv_title_ec,tv_enrolled_date_ec,tv_duration_ec;
        ImageView iv_img_ec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_valid_date = itemView.findViewById(R.id.tv_valid_date);
            tv_title_ec = itemView.findViewById(R.id.tv_title_ec);
            tv_enrolled_date_ec = itemView.findViewById(R.id.tv_enrolled_date_ec);
            tv_duration_ec = itemView.findViewById(R.id.tv_duration_ec);
            iv_img_ec = itemView.findViewById(R.id.iv_img_ec);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    EnrolledPOJO enrolledPOJO = enrolledPOJOS.get(itemPosition);
                    String course_id = enrolledPOJO.getCourse_master_id();
                    Log.d("TAG", "onClick: "+course_id);
                    Intent i = new Intent(context, CoursesDetailsActivity.class);
                    i.putExtra("course_id",course_id);
                    context.startActivity(i);
                }
            });
        }
    }
}
