package com.at17.kma.yogaone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.ModelClassInfo.StudentInfo;
import com.at17.kma.yogaone.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentInfo> studentList;

    public StudentAdapter(List<StudentInfo> studentList) {
        this.studentList = studentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textStudentName;

        public ViewHolder(View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName); // Điều chỉnh ID tùy thuộc vào layout của bạn
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false); // Điều chỉnh layout tùy thuộc vào layout của bạn
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StudentInfo studentInfo = studentList.get(position);
        holder.textStudentName.setText(studentInfo.getName());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
