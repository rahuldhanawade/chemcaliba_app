package com.rahuldhanawade.chemcaliba.adapter;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.IMG_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.returnValueChangeNull;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.setDateFormat;

import static java.util.Objects.isNull;

import android.content.Context;
import android.graphics.Paint;
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
import com.rahuldhanawade.chemcaliba.activity.OurCoursesActivity;

import java.util.ArrayList;

public class OurCourcesAdapter extends RecyclerView.Adapter<OurCourcesAdapter.ViewHolder> {

    private ArrayList<OurCourcesPOJO> ourCourcesPOJOS;
    private Context context;

    public OurCourcesAdapter(Context context, ArrayList<OurCourcesPOJO> ourCourcesPOJOS) {
        this.context=context;
        this.ourCourcesPOJOS=ourCourcesPOJOS;
    }

    @NonNull
    @Override
    public OurCourcesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_our_courses,parent,false);

        return new OurCourcesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OurCourcesAdapter.ViewHolder holder, int position) {
        OurCourcesPOJO ourCourcesPOJOS1= ourCourcesPOJOS.get(position);
        holder.tv_category_oc.setText(ourCourcesPOJOS1.getCourseCategoryName());
        holder.tv_course_category_info_oc.setText(ourCourcesPOJOS1.getCourseName());
        holder.tv_start_date_oc.setText(ourCourcesPOJOS1.getCourseStartDate());
        holder.tv_end_date_oc.setText(ourCourcesPOJOS1.getCourseStartDate());
        holder.tv_duration_oc.setText(ourCourcesPOJOS1.getCourseDurationNumberOfDays() + "Days");
        holder.tv_actual_price_oc.setText("₹"+ourCourcesPOJOS1.getCourseActualPrice());
        holder.tv_actual_price_oc.setPaintFlags(holder.tv_actual_price_oc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_sell_price_oc.setText("₹"+ourCourcesPOJOS1.getCourseSellPrice());
        String img_url = IMG_URL+ourCourcesPOJOS1.getCourseMasterId()+"/course-image/"+ourCourcesPOJOS1.getCourseImage();
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.logo_chemcaliba)
                        .error(R.drawable.logo_chemcaliba))
                .load(img_url)
                .into(holder.iv_img_oc);
    }

    @Override
    public int getItemCount() {
        return ourCourcesPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category_oc,tv_course_category_info_oc,tv_start_date_oc,tv_end_date_oc,tv_duration_oc,
                tv_actual_price_oc,tv_sell_price_oc;
        ImageView iv_img_oc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_oc = itemView.findViewById(R.id.tv_category_oc);
            tv_course_category_info_oc = itemView.findViewById(R.id.tv_course_category_info_oc);
            tv_start_date_oc = itemView.findViewById(R.id.tv_start_date_oc);
            tv_end_date_oc = itemView.findViewById(R.id.tv_end_date_oc);
            tv_duration_oc = itemView.findViewById(R.id.tv_duration_oc);
            tv_actual_price_oc = itemView.findViewById(R.id.tv_actual_price_oc);
            tv_sell_price_oc = itemView.findViewById(R.id.tv_sell_price_oc);
            iv_img_oc = itemView.findViewById(R.id.iv_img_oc);
        }
    }
}
