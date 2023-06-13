package com.rahuldhanawade.chemcaliba.adapter;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.IMG_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.returnValueChangeNull;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.setDateFormat;

import static java.util.Objects.isNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.CoursesDetailsActivity;
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
        holder.tv_date_oc.setText(ourCourcesPOJOS1.getCourseStartDate()+" To "+ourCourcesPOJOS1.getCourseStartDate());
        holder.tv_duration_oc.setText("("+ourCourcesPOJOS1.getCourseDurationNumberOfDays() + "Days)");
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
        boolean is_bought = ourCourcesPOJOS1.getIsAlreadyBought();
        boolean is_expired = ourCourcesPOJOS1.getIsCourseExpired();
        if(!is_bought && !is_expired){
            holder.linear_buy_now.setVisibility(View.VISIBLE);
            holder.linear_purchased.setVisibility(View.GONE);
            holder.linear_expired.setVisibility(View.GONE);
        }else if(is_bought && !is_expired){
            holder.linear_buy_now.setVisibility(View.GONE);
            holder.linear_purchased.setVisibility(View.VISIBLE);
            holder.linear_expired.setVisibility(View.GONE);
        }else if(is_expired){
            holder.linear_buy_now.setVisibility(View.GONE);
            holder.linear_purchased.setVisibility(View.GONE);
            holder.linear_expired.setVisibility(View.VISIBLE);
        }else {
            holder.linear_buy_now.setVisibility(View.GONE);
            holder.linear_purchased.setVisibility(View.GONE);
            holder.linear_expired.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ourCourcesPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category_oc,tv_course_category_info_oc,tv_date_oc,tv_duration_oc,
                tv_actual_price_oc,tv_sell_price_oc;
        ImageView iv_img_oc;
        LinearLayout linear_buy_now,linear_purchased,linear_expired;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_oc = itemView.findViewById(R.id.tv_category_oc);
            tv_course_category_info_oc = itemView.findViewById(R.id.tv_course_category_info_oc);
            tv_date_oc = itemView.findViewById(R.id.tv_date_oc);
            tv_duration_oc = itemView.findViewById(R.id.tv_duration_oc);
            tv_actual_price_oc = itemView.findViewById(R.id.tv_actual_price_oc);
            tv_sell_price_oc = itemView.findViewById(R.id.tv_sell_price_oc);
            iv_img_oc = itemView.findViewById(R.id.iv_img_oc);
            linear_buy_now = itemView.findViewById(R.id.linear_buy_now);
            linear_purchased = itemView.findViewById(R.id.linear_purchased);
            linear_expired = itemView.findViewById(R.id.linear_expired);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    OurCourcesPOJO ourCourcesPOJO1 = ourCourcesPOJOS.get(itemPosition);
                    String course_id = ourCourcesPOJO1.getCourseMasterId();
                    boolean is_bought = ourCourcesPOJO1.getIsAlreadyBought();

                    Log.d("TAG", "onClick: "+course_id+" "+is_bought);
                    Intent i = new Intent(context, CoursesDetailsActivity.class);
                    i.putExtra("course_id",course_id);
                    i.putExtra("is_bought",is_bought ? "1" : "0");
                    context.startActivity(i);
                }
            });
        }
    }
}
