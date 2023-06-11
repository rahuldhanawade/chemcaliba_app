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
import com.rahuldhanawade.chemcaliba.activity.TestScheduleActivity;

import java.util.ArrayList;

public class TestScheduleAdapter extends RecyclerView.Adapter<TestScheduleAdapter.ViewHolder>{

    private ArrayList<TestSchedulePOJO> testSchedulePOJOS;
    private Context context;
    public TestScheduleAdapter(Context context, ArrayList<TestSchedulePOJO> testSchedulePOJOS) {
        this.context=context;
        this.testSchedulePOJOS=testSchedulePOJOS;
    }

    @NonNull
    @Override
    public TestScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_schedule,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestScheduleAdapter.ViewHolder holder, int position) {
        TestSchedulePOJO testSchedulePOJO1= testSchedulePOJOS.get(position);
        holder.tv_create_TS.setText(testSchedulePOJO1.getCreatedDate());
        holder.tv_test_title_TS.setText(testSchedulePOJO1.getTestScheduleTitle());
        holder.tv_category_TS.setText(testSchedulePOJO1.getCourseCategoryName());
        holder.tv_test_date_TS.setText(testSchedulePOJO1.getTestScheduleDateTime());
    }

    @Override
    public int getItemCount() {
        return testSchedulePOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_create_TS,tv_test_title_TS,tv_category_TS,tv_test_date_TS;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_create_TS = itemView.findViewById(R.id.tv_create_TS);
            tv_test_title_TS = itemView.findViewById(R.id.tv_test_title_TS);
            tv_category_TS = itemView.findViewById(R.id.tv_category_TS);
            tv_test_date_TS = itemView.findViewById(R.id.tv_test_date_TS);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    TestSchedulePOJO testSchedulePOJO1 = testSchedulePOJOS.get(itemPosition);
                    String scheduleLink = testSchedulePOJO1.getTestScheduleLink();
                    Log.d("TAG", "onClick: "+scheduleLink);
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(scheduleLink));
                    context.startActivity(browse);
                }
            });
        }
    }
}
