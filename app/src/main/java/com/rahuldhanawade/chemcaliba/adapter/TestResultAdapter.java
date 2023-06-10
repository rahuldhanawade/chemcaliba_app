package com.rahuldhanawade.chemcaliba.adapter;

import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.setDateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.TestResultActivity;

import java.util.ArrayList;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {

    private ArrayList<TestResultPOJO> testResultPOJOS;
    private Context context;
    public TestResultAdapter(Context context, ArrayList<TestResultPOJO> testResultPOJOS) {
        this.context=context;
        this.testResultPOJOS=testResultPOJOS;
    }

    @NonNull
    @Override
    public TestResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_results,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultAdapter.ViewHolder holder, int position) {
        TestResultPOJO testResultPOJO1= testResultPOJOS.get(position);
        holder.tv_create_TR.setText(testResultPOJO1.getCreatedDate());
        holder.tv_test_date_TR.setText(testResultPOJO1.getTestDate());
        holder.tv_result_TR.setText(testResultPOJO1.getMarksObtained()+"/"+testResultPOJO1.getTotalMarks());
        holder.tv_ques_TR.setText(testResultPOJO1.getNoOfWrongQuestions()+"/"+testResultPOJO1.getNoOfRightQuestions());
        String str_is_attempted = testResultPOJO1.getIsAttempted();
        if(str_is_attempted.contains("Yes")){
            holder.linear_data_TR.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.tv_is_attempted.setText("YES");
        }else {
            holder.linear_data_TR.setBackgroundColor(context.getResources().getColor(R.color.colorSecondary));
            holder.tv_is_attempted.setText("NO");
        }
    }

    @Override
    public int getItemCount() {
        return testResultPOJOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_create_TR,tv_test_date_TR,tv_result_TR,tv_ques_TR,tv_is_attempted;
        LinearLayout linear_data_TR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_create_TR = itemView.findViewById(R.id.tv_create_TR);
            tv_test_date_TR = itemView.findViewById(R.id.tv_test_date_TR);
            tv_result_TR = itemView.findViewById(R.id.tv_result_TR);
            tv_ques_TR = itemView.findViewById(R.id.tv_ques_TR);
            tv_is_attempted = itemView.findViewById(R.id.tv_is_attempted);
            linear_data_TR = itemView.findViewById(R.id.linear_data_TR);
        }
    }
}
