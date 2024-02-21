// StudentAdapter.java
package com.hunre.dh10c6.yogaone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hunre.dh10c6.yogaone.ModelClassInfo.StudentInfo;

import java.util.List;

import androidx.annotation.NonNull;

import com.hunre.dh10c6.yogaone.R;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentInfo> students;

    public StudentAdapter(List<StudentInfo> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentInfo student = students.get(position);
        holder.textViewStudentName.setText(student.getName());

        // Gắn các trường dữ liệu khác nếu cần

        // Xử lý sự kiện khi người dùng chọn một sinh viên (nếu cần)
        holder.itemView.setOnClickListener(v -> {
            // Xử lý sự kiện khi người dùng chọn một sinh viên
            // Ví dụ: mở chi tiết sinh viên, ...
        });
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textStudentName);
        }
    }
}
